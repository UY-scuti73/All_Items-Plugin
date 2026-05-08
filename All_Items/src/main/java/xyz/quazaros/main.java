package xyz.quazaros;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.quazaros.util.main.mainVariables;

import static xyz.quazaros.util.main.initialize.*;

public final class main extends JavaPlugin {

    private static main plugin;

    public mainVariables variables;

    public main() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        start();
        System.out.println("All-Items Plugin Has Started");
    }

    @Override
    public void onDisable() {
        stop();
        System.out.println("All-Items Plugin Has Stopped");
    }

    //Gets the plugin to use for the file path
    public static main getPlugin(){
        return plugin;
    }
}
