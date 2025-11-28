package xyz.quazaros;

import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.quazaros.util.commands.commands;
import xyz.quazaros.util.commands.tabComplete;
import xyz.quazaros.util.events.events;
import xyz.quazaros.util.external.placeHolderAPI.placeHolder;
import xyz.quazaros.util.files.file;
import xyz.quazaros.structures.items.*;
import xyz.quazaros.structures.player.*;
import xyz.quazaros.util.meta.*;
import xyz.quazaros.util.files.config.*;
import xyz.quazaros.util.version.version;

public final class main extends JavaPlugin {

    private static main plugin;

    public file file;
    public commands commands;
    public tabComplete tabComplete;
    public metaList meta_list;
    public config data;
    public lang lang;
    public version version;
    public playerList player_list;
    public itemList emptyItemList;
    public itemList emptyMobList;

    public itemList all_items;
    public itemList all_mobs;

    public main() {
        plugin = this;

        version = new version();
        data = new config();
        lang = new lang();
        file = new file();
        meta_list = new metaList();
        player_list = new playerList();
        commands = new commands();
        tabComplete = new tabComplete();
    }

    @Override
    public void onEnable() {
        enable();
        System.out.println("All-Items Plugin Has Started");
    }

    @Override
    public void onDisable() {
        save_files(true);
        System.out.println("All-Items Plugin Has Stopped");
    }

    //Handles what happens when the plugin enables
    private void enable() {
        file.get_data();

        commands.initialize();
        tabComplete.setupLists();

        player_list.initialize_score();

        //Register PlaceholderAPI placeholders
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new placeHolder().register();
        }

        //Register Commands & Events
        getServer().getPluginManager().registerEvents(new events(), this);

        String[] commandNames = {"aitem", "amob", "ahelp"};
        commandSetup(commandNames);
    }

    //Handles what happens when the plugin disables
    public void save_files(boolean remove) {
        file.send_data(remove);
    }

    //Gets the plugin to use for the file path
    public static main getPlugin(){
        return plugin;
    }

    //Initialized The Commands
    private void commandSetup(String[] commands) {
        for (String c : commands) {
            getCommand(c).setExecutor(getPlugin().commands);
            getCommand(c).setTabCompleter(getPlugin().tabComplete);
        }
    }
}
