package xyz.quazaros;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.quazaros.data.items.*;
import xyz.quazaros.data.player.*;
import xyz.quazaros.data.meta.*;
import xyz.quazaros.data.config.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class main extends JavaPlugin implements Listener, TabCompleter {

    private static main plugin;

    public file file;
    public commands commands;
    public metaList meta_list;
    public itemList emptyItemList;
    public itemList emptyMobList;
    public playerList player_list;
    public config data;
    public lang lang;

    public itemList all_items;
    public itemList all_mobs;

    @Override
    public void onEnable() {
        enable();
        System.out.println("All-Items Plugin Has Started");
    }

    @Override
    public void onDisable() {
        save_files();
        System.out.println("All-Items Plugin Has Stopped");
    }

    //Handles what happens when the plugin enables
    private void enable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(this, this);

        file = new file();
        meta_list = new metaList(file.all_items_init, file.all_mobs_init);
        player_list = new playerList();
        data = new config();
        lang = new lang();
        commands = new commands();

        file.get_data();

        commands.initialize();
    }

    //Handles what happens when the plugin disables
    private void save_files() {
        file.send_data();
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
        Player p = e.getPlayer();
        int temp=0;
        for(int i=0; i<player_list.players.size(); i++) {
            if (player_list.players.get(i).name.equals(p.getName())) {
                temp++;
            }
        }
        if (temp==0) {
            player_list.players.add(new player(p.getName()));
        }

        set_permissions(p);
    }

    //Handles what happens when a player clickes on a GUI
    @EventHandler
    public void guiClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        player pl = player_list.get_player_from_string(p.getName());
        if(pl.inv.inventory_list.contains(e.getInventory()) || pl.inv.sorted_list.contains(e.getInventory())) {
            //Prevents the player from moving any items in the menu
            e.setCancelled(true);

            switch (e.getSlot()) {
                //Goes to the last page if the back arrow is pressed, unless it is on the first page
                case 45: {
                    if (!e.getInventory().equals(pl.inv.inventory_list.get(0)) && !e.getInventory().equals(pl.inv.sorted_list.get(0))) {
                        pl.invItt -= 1;
                    }
                    else{
                        pl.invItt = pl.inv.size - 1;
                    }

                    if (!pl.sorted) {
                        p.openInventory(pl.inv.inventory_list.get(pl.invItt));
                    } else {
                        p.openInventory(pl.inv.sorted_list.get(pl.invItt));
                    }

                    break;
                }
                //Goes to the next page if the forward arrow is pressed, unless it is on the last page
                case 53: {
                    if (!e.getInventory().equals(pl.inv.inventory_list.get(pl.inv.size-1)) && !e.getInventory().equals(pl.inv.sorted_list.get(pl.inv.size-1))) {
                        pl.invItt += 1;
                    }
                    else{
                        pl.invItt = 0;
                    }

                    if (!pl.sorted) {
                        p.openInventory(pl.inv.inventory_list.get(pl.invItt));
                    } else {
                        p.openInventory(pl.inv.sorted_list.get(pl.invItt));
                    }

                    break;
                }
                //Handles what happens when the sort button is presses
                case 47: {
                    pl.invItt = 0;
                    pl.sorted = !pl.sorted;
                    if (pl.sorted) {
                        p.openInventory(pl.inv.sorted_list.get(0));
                    } else {
                        p.openInventory(pl.inv.inventory_list.get(0));
                    }
                    break;
                }
            }
        }
    }

    //Handles when a player picks up an item
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerPickupItemEvent(PlayerPickupItemEvent e) {
        if (!data.item_toggle) {
            return;
        }

        player pl = player_list.get_player_from_string(e.getPlayer().getName());

        if (data.item_autoCollect) {
            Player p = e.getPlayer();
            ItemStack tempStack = e.getItem().getItemStack();
            String message_main = all_items.check_items(tempStack, p.getDisplayName(), true);
            String message_personal = pl.item_list.check_items(tempStack, p.getDisplayName(), true);
            String message = data.general_listPriority ? message_personal : message_main;
            if (message.endsWith(lang.itemSubmitted)) {
                if (data.item_subtraction) {
                    if (tempStack.getAmount() <= 1) {
                        e.getItem().setItemStack(new ItemStack(Material.AIR));
                    } else {
                        e.getItem().setItemStack(new ItemStack(tempStack.getType(), tempStack.getAmount() - 1));
                    }
                }
                p.sendMessage(message);
            }
            check_completed(false);
            check_completed(false, pl);
        }
    }

    //Handles when a player kills a mob
    @EventHandler
    public void EntityDeathEvent(EntityDeathEvent e) {
        if (!data.mob_toggle) {
            return;
        }

        LivingEntity mob = (LivingEntity) e.getEntity();
        if(mob.getKiller() instanceof Player) {
            Player p = (Player) mob.getKiller();
            player pl = player_list.get_player_from_string(p.getName());
            String name = mob.getName();
            int temp = 0;

            for (int i=0; i<all_mobs.items.size(); i++) {
                if (all_mobs.items.get(i).item_display_name.equalsIgnoreCase(name)) {
                    temp = i;
                }
            }

            String message_main = "";
            String message_personal = "";

            if (!all_mobs.items.get(temp).isFound && all_mobs.is_in_indexes(temp)) {
                message_main = ChatColor.GREEN + name + " " + lang.mobKilledBy + " " + p.getName();
                all_mobs.items.get(temp).submit(p.getName(), all_mobs.date());
                check_completed(true);
            }
            if (!pl.mob_list.items.get(temp).isFound && pl.mob_list.is_in_indexes(temp)) {
                message_personal = ChatColor.GREEN + name + " " + lang.mobKilled;
                pl.mob_list.items.get(temp).submit(pl.mob_list.date());
                check_completed(true, pl);
            }

            String message = data.general_listPriority ? message_personal : message_main;
            if (!message.isEmpty()) {
                p.sendMessage(message);
            }
        }
    }

    //Gets the plugin to use for the file path
    public static main getPlugin(){
        return plugin;
    }

    //Sets the permissions of every player
    private void set_all_permissions() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            set_permissions(p);
        }
    }

    //Sets the permission of a player
    private void set_permissions(Player player) {
        player.addAttachment(getPlugin(), "all_items.items", false);
        player.addAttachment(getPlugin(), "all_items.mobs", false);

        if (player.isOp()) {
            player.addAttachment(getPlugin(), "all_items.items", true);
            player.addAttachment(getPlugin(), "all_items.mobs", true);
            return;
        }

        if (data.item_toggle) {
            player.addAttachment(getPlugin(), "all_items.items", true);
        }
        if (data.mob_toggle) {
            player.addAttachment(getPlugin(), "all_items.mobs", true);
        }
    }

    //Checks if the list is completed
    public void check_completed(boolean is_mob) {
        check_completed(is_mob, null);
    }

    public void check_completed(boolean is_mob, player pl) {
        String message;
        itemList temp;

        if (pl == null) {
            if (!is_mob) {
                message = ChatColor.GREEN + lang.allItems;
                temp = all_items;
            } else {
                message = ChatColor.GREEN + lang.allMobs;
                temp = all_mobs;
            }
        } else {
            if (!is_mob) {
                message = ChatColor.GREEN + pl.name + " " + lang.completeItemSuffix;
                temp = pl.item_list;
            } else {
                message = ChatColor.GREEN + pl.name + " " + lang.completeMobSuffix;
                temp = pl.mob_list;
            }
        }

        if (temp.complete()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle(ChatColor.YELLOW + lang.congrats, message);
            }
        }
    }
}
