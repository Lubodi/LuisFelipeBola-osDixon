package com.lubodi.alpha.Listeners;

import com.lubodi.alpha.Manager.ConfigManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;
import java.util.List;
public class NivelListener implements Listener {
    List<Material> nivel1Materials = Arrays.asList(Material.IRON_ORE, Material.DIAMOND_ORE, Material.ANCIENT_DEBRIS, Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_IRON_ORE);
    List<Material> nivel2Materials = Arrays.asList(Material.DIAMOND_ORE, Material.ANCIENT_DEBRIS, Material.DEEPSLATE_DIAMOND_ORE);
    List<Material> nivel3Materials = Arrays.asList(Material.ANCIENT_DEBRIS);
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        // Obtener el nivel actual del archivo de configuración
        int nivelActual = ConfigManager.getNivelActual();

        // Crear una lista de materiales para cada nivel


        // Verificar el nivel actual y aplicar las acciones correspondientes
        switch (nivelActual) {
            case 1:
                if (nivel1Materials.contains(block.getType())) {
                    event.setCancelled(true);
                    player.sendMessage("No puedes romper bloques de hierro en este nivel.");
                }
                break;
            case 2:
                if (nivel2Materials.contains(block.getType())) {
                    event.setCancelled(true);
                    player.sendMessage("No puedes romper bloques de diamante en este nivel.");
                }
                // Acciones para el nivel 2
                break;
            case 3:
                if (nivel3Materials.contains(block.getType())) {
                    event.setCancelled(true);
                    player.sendMessage("No puedes romper bloques de roca ancestral en este nivel.");
                    // Acciones para el nivel 3
                }
                break;
            default:

                break;
        }
    }

}
