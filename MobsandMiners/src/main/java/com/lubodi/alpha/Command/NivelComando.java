package com.lubodi.alpha.Command;




import com.lubodi.alpha.Alpha;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class NivelComando implements CommandExecutor {

   Alpha plugin;

    public NivelComando(Alpha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage("El uso correcto es: /novel <nivel>");
            return true;
        }
        String nivelStr = args[0];
        int nuevoNivel;
        try {
            nuevoNivel = Integer.parseInt(nivelStr);
            if (nuevoNivel < 1 || nuevoNivel > 4) {
                player.sendMessage("El nivel especificado no es válido.");
                return true;
            }
        } catch (NumberFormatException e) {
            player.sendMessage("El nivel especificado no es un número válido.");
            return true;
        }

        // Modificar el nivel en el archivo config.yml
        FileConfiguration config = plugin.getConfig(); // Obtener la configuración del plugin
        config.set("nivelActual", nuevoNivel); // Actualizar el valor del nivel en la configuración
        plugin.saveConfig(); // Guardar la configuración en el archivo config.yml
        player.sendMessage("El nivel actual ha sido cambiado a: " + nuevoNivel);
        return true;
    }

}


