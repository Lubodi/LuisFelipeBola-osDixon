package com.lubodi.alpha.Listeners;

import com.lubodi.alpha.Alpha;
import com.lubodi.alpha.Enum.GameState;
import com.lubodi.alpha.Enum.GameStateManager;
import com.lubodi.alpha.Instance.Metodos;
import com.lubodi.alpha.Instance.UI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;



public class Invocador implements Listener {
    private final Alpha alpha;
    private Metodos metodos;

    public Invocador(Alpha alpha) {
        this.alpha = alpha;
        if (alpha != null) {
            this.metodos = alpha.metodos();

        }
    }

    private Location goldBlockLocation = null;

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        Player player = event.getPlayer();

        // Verificar si el item es una esmeralda y si hay exactamente 64
        if (item.getType() == Material.EMERALD && item.getAmount() == 64 && metodos.getState().equals(GameState.NoLive)) {
            // Obtener el bloque debajo del punto donde se tiró el item
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

            // Verificar si el bloque es de oro
            if (block.getType() == Material.GOLD_BLOCK) {
                goldBlockLocation = block.getLocation();

                if(metodos.isMonolithValid(goldBlockLocation)){
                    //Generar el sonido de wither
                    block.getWorld().playSound(block.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1f, 1f);
                    block.getWorld().spawnParticle(Particle.SMOKE_LARGE, block.getLocation().add(0.5, 0.5, 0.5), 50, 0.5, 0.5, 0.5, 0.1);
                    block.getWorld().spawnParticle(Particle.SPELL_WITCH, block.getLocation().add(0.5, 0.5, 0.5), 50, 0.5, 0.5, 0.5, 0.1);
                    // Invocar al aldeano Monolito
                    Location loc = block.getLocation().add(0.5, 1, 0.5);
                    Villager monolito = (Villager) block.getWorld().spawnEntity(loc, EntityType.VILLAGER);
                    monolito.setCustomName("Monolito");
                    monolito.setProfession(Villager.Profession.NITWIT);
                    player.sendMessage(ChatColor.RED + "Invocaste un Monolito");

                    // Eliminar las 64 esmeraldas
                    event.getItemDrop().remove();

                    // Establecer estado del juego a Live
                    GameStateManager gameStateManager = GameStateManager.getInstance();
                    gameStateManager.setGameState(GameState.Live);

                    Metodos metodos = alpha.metodos();
                    metodos.iniciarEvento();
                } else {
                    player.sendMessage(ChatColor.RED + "No se puede invocar el Monolito en esta ubicación.");
                }

            }
        }
    }



    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        GameStateManager gameStateManager = GameStateManager.getInstance();
        if (entity instanceof Villager && gameStateManager.getGameState().equals(GameState.Live)) {
            Villager villager = (Villager) entity;

            if (villager.getCustomName() != null && villager.getCustomName().equals("Monolito")) {
                UI ui = metodos.getUIInstance();
                ui.removeBossbar();
                metodos.reiniciar();

                Location location = entity.getLocation();
                float power = 30.0f; // Configurar la potencia de la explosión
                boolean setFire = true; // Indicar si se deben generar incendios
                location.getWorld().createExplosion(location, power, setFire);
                location.getWorld().playSound(location, Sound.ENTITY_WITHER_DEATH, 10, 20);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatColor.RED + "El Monolito ha muerto.");

                }

            }
        }


    }

}