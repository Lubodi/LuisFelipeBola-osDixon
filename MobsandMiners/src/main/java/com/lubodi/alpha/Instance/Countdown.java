package com.lubodi.alpha.Instance;

import com.lubodi.alpha.Alpha;
import com.lubodi.alpha.Clases.MonsterSpawn;
import com.lubodi.alpha.Enum.GameState;
import com.lubodi.alpha.Enum.GameStateManager;
import com.lubodi.alpha.Enum.Niveles;
import com.lubodi.alpha.Manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;


public class Countdown extends BukkitRunnable {
    private AtomicInteger timeLeft;
    private Metodos metodos;
    private Alpha alpha;
    private GameStateManager gameStateManager;
    private BukkitTask particleTask;
    private UI ui;
    public Countdown(int timeLeft, Metodos metodos, Alpha alpha, GameStateManager gameStateManager) {
        this.timeLeft = new AtomicInteger(timeLeft);
        this.metodos = metodos;
        this.alpha = alpha;
        this.gameStateManager = gameStateManager;

    }

    public void start() {
        metodos.setGroundBlocksBreakability(metodos.getMonolitoLocation(),false);
        ui = UI.getInstance(metodos, alpha);
        metodos.Start();
        runTaskTimer(alpha, 1, 20);

    }

    @Override
    public void run() {
        int currentTimeLeft = timeLeft.decrementAndGet();

        if (currentTimeLeft == 0) {
            int nivelActual = ConfigManager.getNivelActual();
            if (nivelActual != 4) {
                ConfigManager.setNivelActual(nivelActual + 1);
                Metodos.sendMessageToAllPlayers("&c¡EL MONOLITO HA PREVALECIDO, SE HAN LIBRADO MATERIALES DE LA CORRUPCIÓN!");
            } else {
                Metodos.sendMessageToAllPlayers("&c¡EL MONOLITO HA PREVALECIDO, FINALMENTE LIBERAMOS LA CORRUPCION DE TODOS LOS MATERIALES!");
            }

            this.cancel();
            UI.efectosVictoria(metodos.getMonolitoLocation());
            metodos.setGroundBlocksBreakability(metodos.getMonolitoLocation(),true);
            metodos.reiniciar();
            metodos.killAllMonolitos();

            return;
        }

        if (currentTimeLeft > 5 && gameStateManager.getGameState().equals(GameState.Live)) {
            MonsterSpawn.spawnRandomMonsters(metodos.getMonolitoLocation(), ConfigManager.getNivelActual());
            metodos.findTarget();
        }

        ui.displayBossbarNearMonolith("Tiempo restante: %d segundos", currentTimeLeft);
        UI.emitFarolSignal(metodos.getMonolitoLocation(), currentTimeLeft);
        UI.playParticleEffect(metodos.getMonolitoLocation(),currentTimeLeft);
        ui.displayBossbarNearMonolith("Tiempo restante: %d segundos", currentTimeLeft);
    }
}

