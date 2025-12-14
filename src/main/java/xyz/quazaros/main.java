package xyz.quazaros;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.quazaros.util.commands.commands;
import xyz.quazaros.util.commands.tabComplete;
import xyz.quazaros.util.files.file;
import xyz.quazaros.structures.items.*;
import xyz.quazaros.structures.player.*;
import xyz.quazaros.structures.meta.*;
import xyz.quazaros.util.files.config.*;
import xyz.quazaros.extra.timer.timer;
import xyz.quazaros.extra.version.version;

import java.util.ArrayList;

import static xyz.quazaros.util.main.initialize.*;

public final class main extends JavaPlugin {

    private static main plugin;

    public file file;
    public commands commands;
    public tabComplete tabComplete;
    public metaList meta_list;
    public config data;
    public lang lang;
    public version version;
    public timer timer;
    public playerList player_list;
    public itemList emptyItemList;
    public itemList emptyMobList;

    public itemList all_items;
    public itemList all_mobs;

    public ArrayList<String> commandNames;

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
