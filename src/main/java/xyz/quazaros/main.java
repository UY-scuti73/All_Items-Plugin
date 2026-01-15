package xyz.quazaros;

import org.bukkit.entity.Boss;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.quazaros.items.itemList;
import xyz.quazaros.sprites.version;

public final class main extends JavaPlugin {

    private static main plugin;

    public file File;
    public itemList ItemList;
    public bossbar BossBar;
    public version version;

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
        ItemList = new itemList();
        File = new file();
        BossBar = new bossbar();
        version = new version();

        File.get();

        BossBar.updateBossBar();
        BossBar.updatePlayers();

        getServer().getPluginManager().registerEvents(new events(), getPlugin());
    }

    private void stop() {
        File.send();
        BossBar.removeBossBar();
    }

    //Gets the plugin to use for the file path
    public static main getPlugin(){
        return plugin;
    }
}
