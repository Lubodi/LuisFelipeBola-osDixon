package com.lubodi.alpha.Instance;

import com.sk89q.worldedit.bukkit.BukkitAdapter;

import com.sk89q.worldguard.WorldGuard;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import com.lubodi.alpha.Alpha;

import com.lubodi.alpha.Enum.GameState;
import com.lubodi.alpha.Enum.GameStateManager;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class Metodos {
    private UI ui;
    private List<UUID> players;
    private Countdown countdown;
    private Alpha alpha;
    private Game game;
    private GameState state;
    private GameStateManager gameStateManager;

    public Metodos(Alpha alpha, GameStateManager gameStateManager) {
        this.alpha = alpha;
        this.countdown = new Countdown(50, this,alpha,gameStateManager);
        this.game = new Game(this,gameStateManager);
        this.players = new ArrayList<>();
        this.gameStateManager = gameStateManager;
    }
    public  void Start(){game.start();}

    public void iniciarEvento() {
        countdown.start();

    }
    public void reiniciar() {
        countdown.cancel();
        this.countdown = new Countdown(50, this,alpha,gameStateManager);
        this.game = new Game(this,gameStateManager);
        gameStateManager.setGameState(GameState.NoLive);
        UI.resetInstance(); // Esto reiniciará la instancia de UI
    }



            public Location getMonolitoLocation() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Villager) {
                    Villager villager = (Villager) entity;
                    if (villager.getName().equals("Monolito")) {
                        return villager.getLocation();
                    }
                }
            }
        }
        return null;
    }
    public Entity getMonolitoEntity() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Villager) {
                    Villager villager = (Villager) entity;
                    if (villager.getName().equals("Monolito")) {
                        return villager;
                    }
                }
            }
        }
        return null;
    }
    public void findTarget() {
        List<Monster> monsters = getMonstersInWorld();
        for (Monster monster : monsters) {
            Bukkit.getScheduler().runTaskLater(alpha, () -> {
                Entity target = getNearbyEntity(monster, EntityType.PLAYER);
                target = getNearbyEntity(monster, EntityType.VILLAGER, "Monolito");
                monster.setTarget((LivingEntity) target);
            }, 200L); // 200 ticks = 10 segundos (20 ticks por segundo)
        }
    }



    private static Entity getNearbyEntity(Entity entity, EntityType entityType) {
        return getNearbyEntity(entity, entityType, null);
    }

    private static Entity getNearbyEntity(Entity entity, EntityType entityType, String name) {
        // Obtener todas las entidades del tipo especificado cerca de la entidad dada
        Collection<Entity> entities = entity.getWorld().getNearbyEntities(entity.getLocation(), 400, 20, 400,
                e -> e.getType() == entityType);
        for (Entity e : entities) {
            if (name != null) {
                // Si se especificó un nombre, asegurarse de que la entidad tenga ese nombre
                if (e instanceof LivingEntity && ((LivingEntity) e).getCustomName() != null
                        && ((LivingEntity) e).getCustomName().equals(name)) {
                    return e;
                }
            } else {
                return e;
            }
        }
        return null;
    }

    public List<Monster> getMonstersInWorld() {
        List<Monster> monsters = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Monster) {
                    monsters.add((Monster) entity);
                }
            }
        }
        return monsters;
    }



    public void killAllMonolitos() {
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Villager) {
                    Villager villager = (Villager) entity;
                    if (villager.getName().equals("Monolito")) {
                        villager.setHealth(0);
                    }
                }
            }
        }
    }


    public void setState(GameState state) {
        gameStateManager.setGameState(state);
    }

    public GameState getState() {
        return gameStateManager.getGameState();
    }



    public void sendmessage(String message){
        for(UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }

    public void sendTitle(String title,String subtitle){
        for(UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendTitle(title,subtitle);
        }
    }

    public static void sendMessageToAllPlayers(String message) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

        }
    }
    public boolean isMonolithValid(Location goldBlockLocation) {
        World world = goldBlockLocation.getWorld();
        int requiredSolidBlockPercentageSameLevel = 70;
        int requiredSolidBlockPercentageAbove = 10;
        int radio1 = 10;
        int radio2 = 50;

        int goldBlockY = goldBlockLocation.getBlockY();

        // Check blocks within radio1 of the same level as the gold block
        for (int x = -radio1; x <= radio1; x++) {
            for (int z = -radio1; z <= radio1; z++) {
                Block block = world.getBlockAt(goldBlockLocation.getBlockX() + x, goldBlockY, goldBlockLocation.getBlockZ() + z);
                if (!block.getType().isSolid() && !block.getType().equals(Material.WATER) && !block.getType().equals(Material.LAVA)) {
                    return false;
                }
            }
        }

        // Count solid blocks on the same level as the gold block within radio2
        int solidBlockCountSameLevel = 0;
        int totalBlockCountSameLevel = 0;
        for (int x = -radio2; x <= radio2; x++) {
            for (int z = -radio2; z <= radio2; z++) {
                Block block = world.getBlockAt(goldBlockLocation.getBlockX() + x, goldBlockY, goldBlockLocation.getBlockZ() + z);
                if (block.getType().isSolid() || block.getType().isFuel()) {
                    solidBlockCountSameLevel++;
                }
                totalBlockCountSameLevel++;
            }
        }

        // Calculate the percentage of solid blocks on the same level as the gold block within radio2
        double solidBlockPercentageSameLevel = (double) solidBlockCountSameLevel / totalBlockCountSameLevel * 100;

        // Check if the percentage of solid blocks on the same level is below the required percentage
        if (solidBlockPercentageSameLevel <= requiredSolidBlockPercentageSameLevel) {
            return false;
        }

        // Count solid blocks above the gold block within radio2
        int solidBlockCountAbove = 0;
        int totalBlockCountAbove = 0;
        for (int y = goldBlockY + 1; y <= goldBlockY + 20; y++) {
            for (int x = -radio2; x <= radio2; x++) {
                for (int z = -radio2; z <= radio2; z++) {
                    Block block = world.getBlockAt(goldBlockLocation.getBlockX() + x, y, goldBlockLocation.getBlockZ() + z);
                    if (block.getType().isSolid() || block.getType().isFuel()) {
                        solidBlockCountAbove++;
                    }
                    totalBlockCountAbove++;
                }
            }
        }

        // Calculate the percentage of solid blocks above the gold block within radio2
        double solidBlockPercentageAbove = (double) solidBlockCountAbove / totalBlockCountAbove * 100;
        Bukkit.getPlayer("Lubodi").sendMessage("Percentage of solid blocks on same level: " + solidBlockPercentageSameLevel);
        Bukkit.getPlayer("Lubodi").sendMessage("Percentage of solid blocks above: " + solidBlockPercentageAbove);

        if (solidBlockPercentageAbove >= requiredSolidBlockPercentageAbove) {
            return false;
        }

        return true;
    }

    public void setGroundBlocksBreakability(Location monolithLocation, boolean breakable) {
        World world = monolithLocation.getWorld();
        int goldBlockY = monolithLocation.getBlockY();
        int radius = 50;

        WorldGuardPlugin worldGuardPlugin = alpha.getWorldGuardPlugin();
        if (worldGuardPlugin == null) {
            // WorldGuard plugin not found, handle the error or return
            return;
        }

        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        StateFlag.State flagState = breakable ? StateFlag.State.ALLOW : StateFlag.State.DENY;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Block block = world.getBlockAt(monolithLocation.getBlockX() + x, goldBlockY, monolithLocation.getBlockZ() + z);

                // Verificar si el bloque está protegido por WorldGuard
                if (regionManager != null) {
                    ProtectedRegion region = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(block.getLocation())).getRegions().stream()
                            .filter(r -> r.getFlag(Flags.BLOCK_BREAK) == flagState)
                            .findFirst()
                            .orElse(null);
                    if (region != null) {
                        // El bloque está protegido, no se puede cambiar la breakability
                        continue;
                    }
                }

                // Cambiar la breakability del bloque
                block.setMetadata("breakable", new FixedMetadataValue(alpha, breakable));
            }
        }
    }


    public UI getUIInstance() {
        return UI.getInstance(this, alpha);
    }

}
