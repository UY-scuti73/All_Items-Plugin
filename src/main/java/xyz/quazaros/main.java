package xyz.quazaros;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.quazaros.data.items.*;
import xyz.quazaros.data.player.*;
import xyz.quazaros.data.meta.*;
import xyz.quazaros.data.config.*;

import java.util.List;

public final class main extends JavaPlugin implements Listener, TabCompleter {

    private static main plugin;

    public file file;
    public commands commands;
    public events events;
    public metaList meta_list;
    public config data;
    public lang lang;
    public version version;
    public playerList player_list;
    public itemList emptyItemList;
    public itemList emptyMobList;

    public itemList all_items;
    public itemList all_mobs;

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
        plugin = this;
        getServer().getPluginManager().registerEvents(this, this);

        version = new version();
        data = new config();
        lang = new lang();
        file = new file();
        meta_list = new metaList(file.all_items_init, file.all_mobs_init);
        player_list = new playerList();
        commands = new commands();
        events = new events();

        file.get_data();

        commands.initialize();
    }

    //Handles what happens when the plugin disables
    public void save_files(boolean remove) {
        file.send_data(remove);
    }

    //Gets the plugin to use for the file path
    public static main getPlugin(){
        return plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return commands.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        return commands.onTabComplete(sender, command, alias, args);
    }

    //Adds a player to the list when they join if they aren't in already
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        events.playerJoin(e);
    }

    //Handles what happens when a player clickes on a GUI
    @EventHandler
    public void guiClickEvent(InventoryClickEvent e) {
        events.inventoryClick(e);
    }

    //Handles when a player picks up an item
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerPickupItemEvent(PlayerPickupItemEvent e) {
        events.itemPickup(e);
    }

    //Handles when a player kills a mob
    @EventHandler
    public void EntityDeathEvent(EntityDeathEvent e) {
        events.mobDies(e);
    }

    //Autosaves the plugin
    @EventHandler
    public void onWorldSave(WorldSaveEvent e) {
        events.autoSave();
    }
}
