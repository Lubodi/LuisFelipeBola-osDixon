package com.lubodi.alpha.Instance;

import com.lubodi.alpha.Alpha;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class UI {
    private static UI instance;
    private final Metodos metodos;
    private final Alpha alpha;
    private boolean bossbarActive = false;
    private String bossbarTitle;
    BossBar bossbar = Bukkit.createBossBar(bossbarTitle, BarColor.RED, BarStyle.SOLID);
    public UI(Metodos metodos, Alpha alpha) {
        this.metodos = metodos;
        this.alpha = alpha;
    }


    public static UI getInstance(Metodos metodos, Alpha alpha) {
        if (instance == null) {
            instance = new UI(metodos, alpha);
        }
        return instance;
    }
    public void displayBossbarNearMonolith(String bossbarTitle, int durationInSeconds) {
        if (bossbarActive) {
            return;
        }


        bossbar.setVisible(true);
        bossbarActive = true;

        // Obtener a los jugadores cercanos al monolito
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distance(metodos.getMonolitoLocation()) <= 50) {
                bossbar.addPlayer(player);
            }
        }

        new BukkitRunnable() {
            int remainingSeconds = durationInSeconds;
            double progress = 1.0;

            @Override
            public void run() {
                if (remainingSeconds <= 0) {
                    bossbar.setVisible(false);
                    bossbar.removeAll();
                    bossbarActive = false;
                    cancel();
                    return;
                }

                if (remainingSeconds >= 30) {
                    bossbar.setColor(BarColor.YELLOW);
                } else if (remainingSeconds >= 10) {
                    bossbar.setColor(BarColor.RED);
                } else {
                    bossbar.setColor(BarColor.WHITE);
                }
                // Actualizar la BossBar
                bossbar.setTitle(bossbarTitle + " - " + remainingSeconds + "s");
                bossbar.setProgress(progress);

                // Reducir el tiempo restante y el progreso de la BossBar
                remainingSeconds--;
                progress = (double) remainingSeconds / (double) durationInSeconds;

                // Ocultar la BossBar si el jugador está fuera del rango del Monolito
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getLocation().distance(metodos.getMonolitoLocation()) > 50) {
                        bossbar.removePlayer(player);
                    } else {
                        bossbar.addPlayer(player);
                    }
                }
            }
        }.runTaskTimer(alpha, 0, 20);



    }

    public void removeBossbar() {
    
        if (bossbar != null) {
            bossbar.setVisible(false);
            bossbar.removeAll();
            bossbar = null;
        }
    }
    public static void playParticleEffect(Location monolitoLocation, int timeLeft) {
        World world = monolitoLocation.getWorld();
        double x = monolitoLocation.getX();
        double y = monolitoLocation.getY() + 2;
        double z = monolitoLocation.getZ();

        new BukkitRunnable() {
            double radius = 10.0;
            double intensity = 0.0;
            final double delta = 0.1;
            Particle particle = Particle.REDSTONE;
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);

            @Override
            public void run() {
                // Calcular la dirección del vector desde la ubicación de las partículas hasta el monolito
                Vector direction = monolitoLocation.subtract(x, y, z).toVector().normalize();

                // Partículas liberadas en la ubicación del monolito
                for (double i = 0; i < Math.PI * 2; i += 0.1) {
                    double x1 = Math.cos(i) * radius;
                    double z1 = Math.sin(i) * radius;

                    // Calcular la velocidad de la partícula en dirección al monolito
                    Vector velocity = direction.multiply(-1).multiply(0.1);

                    // Cambiar el color de las partículas según el timeLeft
                    if (timeLeft >= 50) {
                        particle = Particle.REDSTONE;
                        dustOptions = new Particle.DustOptions(Color.ORANGE, 1);
                    } else if (timeLeft >= 20) {
                        particle = Particle.REDSTONE;
                        dustOptions = new Particle.DustOptions(Color.ORANGE, 1);
                    } else if (timeLeft <= 10) {
                        particle = Particle.REDSTONE;
                        dustOptions = new Particle.DustOptions(Color.WHITE, 1);
                    }

                    world.spawnParticle(particle, x + x1, y - (radius - 4.0), z + z1, 0, dustOptions);
                }

                // Aumentamos o disminuimos la intensidad y el radio del efecto
                if (radius > 1.0) {
                    radius -= delta;
                }
                if (intensity < 1.0) {
                    intensity += delta;
                } else {
                    intensity = 0.0;
                }

                // Verificar si la partícula está lo suficientemente cerca del centro del monolito
                if (radius <= 1.0) {
                    // Cancelar la tarea
                    cancel();
                }
            }
        }.runTaskTimer(instance.alpha, 0L, 2L);
    }
    public static void emitFarolSignal(Location location, int timeleft) {
        // Emitir partículas de farol de Minecraft
        if (timeleft <= 20) {
            location.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, location, 150, 0.5, 0.5, 0.5);
            location.getWorld().spawnParticle(Particle.WATER_BUBBLE, location, 150, 0.5, 0.5, 0.5);
            location.getWorld().playSound(location, Sound.BLOCK_BEACON_ACTIVATE, 2f, 5f);
        } else if (timeleft <= 10) {
            location.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, location, 200, 0.5, 0.5, 0.5);
            location.getWorld().spawnParticle(Particle.WATER_SPLASH, location, 200, 0.5, 0.5, 0.5);
            location.getWorld().spawnParticle(Particle.SOUL, location, 200, 0.5, 0.5, 0.5);
            location.getWorld().playSound(location, Sound.BLOCK_BEACON_ACTIVATE, 2f, 5f);
            location.getWorld().playSound(location, Sound.BLOCK_BEACON_ACTIVATE, 5f, 10f);
            location.getWorld().playSound(location, Sound.BLOCK_BEACON_ACTIVATE, 10f, 20f);
        }else {
            location.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, location, 100, 0.5, 0.5, 0.5);
        }

        // Emitir sonido de farol de Minecraft
        location.getWorld().playSound(location, Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
    }



    public static void efectosVictoria(Location location) {
        // Añadir sonido de victoria
        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);

        // Generar línea de partículas
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        for (int i = 0; i < 50; i++) {
            location.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, x, y + i, z, 0, 0, 0, 0, 0);
        }
        for (int i = 0; i < 20; i++) {
            double radius = i / 10.0;
            for (double angle = 0; angle < 2 * Math.PI; angle += Math.PI / 8) {
                x = location.getX() + radius * Math.cos(angle);
                z = location.getZ() + radius * Math.sin(angle);
                location.getWorld().spawnParticle(Particle.SMOKE_LARGE, x, location.getY() + 1, z, 1, 0, 0, 0, 0);
            }
            // Añadir efectos de destellos de explosiones
            location.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location, 10, 0.5, 0.5, 0.5, 0.1);

            // Añadir efectos de sonido de explosiones
            location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
            location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 1, 1);

            // Esperar un poco y detener los efectos
            new BukkitRunnable() {
                @Override
                public void run() {
                    this.cancel();
                }
            }.runTaskLater(instance.alpha, 200);
        }
    }




    public static void resetInstance() {
        instance = null;
    }





}



