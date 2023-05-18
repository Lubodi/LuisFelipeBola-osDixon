package com.lubodi.alpha.Instance;

import com.lubodi.alpha.Alpha;
import com.lubodi.alpha.Enum.GameState;
import com.lubodi.alpha.Enum.GameStateManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class Game {
    private GameStateManager gameStateManager;

    public Game(Metodos metodos, GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    public void start(){

    }
}
