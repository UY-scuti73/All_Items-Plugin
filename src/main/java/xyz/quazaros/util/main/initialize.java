package xyz.quazaros.util.main;

import xyz.quazaros.main;
import xyz.quazaros.structures.player.playerList;
import xyz.quazaros.util.commands.commands;
import xyz.quazaros.util.commands.tabComplete;
import xyz.quazaros.util.events.events;
import xyz.quazaros.extra.external.placeHolderAPI.placeHolder;
import xyz.quazaros.util.files.config.config;
import xyz.quazaros.util.files.config.lang;
import xyz.quazaros.util.files.file;
import xyz.quazaros.structures.meta.metaList;
import xyz.quazaros.extra.timer.timer;
import xyz.quazaros.extra.version.version;

import java.util.ArrayList;
import java.util.Arrays;

import static org.bukkit.Bukkit.getServer;

public class initialize {

    static main Main = main.getPlugin();

    public static void start() {
        initializeNew();
        initializeData();
        initializeSpigot();
    }

    public static void stop() {
        save_files(true);
    }

    public static void reset() {
        save_files(true);
        initializeNew();
        initializeData();
    }

    public static void initializeNew() {
        Main.version = new version();
        Main.data = new config();
        Main.lang = new lang();
        Main.file = new file();
        Main.meta_list = new metaList();
        Main.player_list = new playerList();
        Main.commands = new commands();
        Main.tabComplete = new tabComplete();
        Main.timer = new timer();

        Main.commandNames = new ArrayList<>(Arrays.asList("aitem", "amob", "atime", "areset", "ahelp"));
    }

    private static void initializeData() {
        Main.file.get_data();

        Main.commands.initialize();
        Main.tabComplete.setupLists();

        Main.player_list.initialize_score();

        Main.timer.startUp();
    }

    public static void save_files(boolean remove) {
        Main.file.send_data(remove);
    }

    //Registers commands and API's
    private static void initializeSpigot() {
        //Register PlaceholderAPI placeholders
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new placeHolder().register();
        }

        //Register Commands & Events
        getServer().getPluginManager().registerEvents(new events(), Main);

        commandSetup(Main.commandNames);
    }

    //Initialize The Commands
    private static void commandSetup(ArrayList<String> commands) {
        for (String c : commands) {
            Main.getCommand(c).setExecutor(Main.commands);
            Main.getCommand(c).setTabCompleter(Main.tabComplete);
        }
    }


}
