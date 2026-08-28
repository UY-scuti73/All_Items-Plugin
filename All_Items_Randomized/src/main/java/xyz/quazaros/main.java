package xyz.quazaros;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.quazaros.items.itemList;

public final class main extends JavaPlugin {

    private static main plugin;

    public file File;
    public itemList ItemList;
    public bossbar BossBar;

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

    //Commands
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {return true;}
        if (p.isOp() && cmd.getName().equalsIgnoreCase("skip")) {
            getPlugin().ItemList.skip();
        }
        return true;
    }
}
