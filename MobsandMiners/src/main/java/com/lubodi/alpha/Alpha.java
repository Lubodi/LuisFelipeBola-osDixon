package com.lubodi.alpha;

import com.lubodi.alpha.Command.NivelComando;
import com.lubodi.alpha.Enum.GameStateManager;
import com.lubodi.alpha.Instance.Metodos;
import com.lubodi.alpha.Listeners.NivelListener;
import com.lubodi.alpha.Listeners.Invocador;
import com.lubodi.alpha.Listeners.RomperBloques;
import com.lubodi.alpha.Manager.ConfigManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public final class Alpha extends JavaPlugin {
    Metodos metodos;
    GameStateManager gameStateManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        ConfigManager.setupConfig(this);
        gameStateManager = GameStateManager.getInstance();
        metodos = new Metodos(this,gameStateManager);
        getCommand("novel").setExecutor(new NivelComando(this));
        Bukkit.getPluginManager().registerEvents(new Invocador(this), this);
        Bukkit.getPluginManager().registerEvents(new RomperBloques(this), this);
        Bukkit.getPluginManager().registerEvents(new NivelListener(), this);
    }

    public Metodos metodos() {
        return  metodos;
    }

    public WorldGuardPlugin getWorldGuardPlugin() {
        org.bukkit.plugin.Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }
}
