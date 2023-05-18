package com.lubodi.alpha.Listeners;

import com.lubodi.alpha.Alpha;
import com.lubodi.alpha.Enum.GameState;
import com.lubodi.alpha.Instance.Metodos;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RomperBloques implements Listener {

    Alpha alpha;

    public RomperBloques(Alpha alpha) {
        this.alpha = alpha;
    }
    private final Set<Material> materialesDuros = new HashSet<>(Arrays.asList(
            Material.ANDESITE,
            Material.DIORITE,
            Material.GRANITE,
            Material.STONE_BRICKS,
            Material.MOSSY_STONE_BRICKS,
            Material.CRACKED_STONE_BRICKS,
            Material.CHISELED_STONE_BRICKS,
            Material.OBSIDIAN
    ));

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        Entity entidad = event.getEntity();
        GameState metodos = alpha.metodos().getState();



        if (entidad instanceof Monster && metodos.equals(GameState.Live)) {
            Monster monstruo = (Monster) entidad;

            Bukkit.getScheduler().scheduleSyncRepeatingTask(alpha, new Runnable() {
                @Override
                public void run() {

                    Location loc = monstruo.getLocation();
                    Location targetLoc1 = loc.clone().add(loc.getDirection().setY(0.2));
                    Location targetLoc2 = loc.clone().add(loc.getDirection().setY(1));
                    Material blockType1 = targetLoc1.getBlock().getType();
                    Material blockType2 = targetLoc2.getBlock().getType();

                    // Verificar si el bloque es un material duro para hacer que el monstruo tarde más en romperlo
                    if (materialesDuros.contains(blockType1) && blockType1 != Material.BEDROCK) {
                        Bukkit.getScheduler().runTaskLater(alpha, new Runnable() {
                            @Override
                            public void run() {
                                breakBlockEffects(targetLoc1, blockType1);
                                targetLoc1.getBlock().setType(Material.AIR);
                            }
                        }, 200); // Esperar 10 segundos (20 ticks) antes de romper el bloque
                    }
                    if (materialesDuros.contains(blockType2) && blockType2 != Material.BEDROCK) {
                        Bukkit.getScheduler().runTaskLater(alpha, new Runnable() {
                            @Override
                            public void run() {
                                breakBlockEffects(targetLoc1, blockType1);
                                targetLoc2.getBlock().setType(Material.AIR);
                            }
                        }, 200); // Esperar 10 segundos (20 ticks) antes de romper el bloque

                    }
                    if (blockType1 != Material.AIR && blockType1 != Material.BEDROCK) {
                        targetLoc1.getBlock().setType(Material.AIR);
                        breakBlockEffects(targetLoc1, blockType1);
                    }
                    if (blockType1 != Material.AIR && blockType2 != Material.BEDROCK) {
                        targetLoc2.getBlock().setType(Material.AIR);
                        breakBlockEffects(targetLoc1, blockType1);
                    }
                }
            }, 0, 50);


        }
    }
    private void breakBlockEffects(Location loc, Material blockType) {
        loc.getWorld().playEffect(loc, Effect.STEP_SOUND, blockType);
        loc.getWorld().playSound(loc, blockType.createBlockData().getSoundGroup().getBreakSound(), 1, 1);
    }


}
