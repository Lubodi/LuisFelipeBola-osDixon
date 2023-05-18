package com.lubodi.alpha.Manager;

import com.lubodi.alpha.Alpha;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private static FileConfiguration config;
    public static  void  setupConfig(Alpha minigame){
        ConfigManager.config = minigame.getConfig();
        minigame.saveDefaultConfig();
    }

    public static int getNivelActual() {return config.getInt("nivelActual");}

    public static void setNivelActual(int nivel) {
        config.set("nivelActual", nivel);
    }
}
