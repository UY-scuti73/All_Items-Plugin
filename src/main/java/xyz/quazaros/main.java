package xyz.quazaros;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.quazaros.events.commands;
import xyz.quazaros.events.events;
import xyz.quazaros.inventory.inventory;
import xyz.quazaros.items.itemList;
import xyz.quazaros.player.playerList;

public final class main extends JavaPlugin {

    private static main plugin;

    public file File;
    public commands Commands;
    public playerList PlayerList;
    public itemList ItemList;
    public inventory Inventory;
    public version Version;

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

    private void start() {
        Version = new version();
        File = new file();
        Commands = new commands();
        PlayerList = new playerList();
        ItemList = new itemList();
        Inventory = new inventory();

        File.get();

        getServer().getPluginManager().registerEvents(new events(), getPlugin());
        getCommand("alist").setExecutor(Commands);
    }

    private void stop() {
        File.send();
    }

    //Gets the plugin to use for the file path
    public static main getPlugin(){
        return plugin;
    }
}
