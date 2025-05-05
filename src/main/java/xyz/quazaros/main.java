package xyz.quazaros;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class main extends JavaPlugin implements Listener, TabCompleter {

    private static main plugin;

    file obj_file;
    metaList meta_list;
    inventory inv;
    inventory mob_inv;
    playerList player_list;

    itemList all_items;
    itemList all_mobs;

    List<String> config_list;
    List<String> send_list;

    String help_string;
    String admin_help_string;
    String mob_help_string;
    String admin_mob_help_string;

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

    ////Handles what happens when the plugin enables
    private void enable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(this, this);

        obj_file = new file();
        meta_list = new metaList();

        inv = new inventory(false);
        mob_inv = new inventory(true);
        player_list = new playerList();

        all_items = new itemList();
        all_items.total();

        all_mobs = new itemList();
        all_mobs.total_mobs();

        try {
            obj_file.get_data();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        all_items = obj_file.total_items;
        all_mobs = obj_file.total_mobs;
        player_list = obj_file.player_list;

        inv.set_inventory(all_items.get_sub_items(), get_progress(false), player_list);
        mob_inv.set_inventory(all_mobs.get_sub_items(), get_progress(true), player_list);

        config_list = new ArrayList<>();
        send_list = new ArrayList<>();
        send_list.add("inventory");
        send_list.add("hotbar");
        config_list.add("file");
        config_list.add("autocollect");
        config_list.add("subtract");

        initialize_help_string();
    }

    //Handles what happens when the plugin disables
    private void save_files() {
        try {
            obj_file.send_data(all_items, all_mobs, player_list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Handles the commands
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;

        if (check_itmes(command.getName()) && !obj_file.toggle_items) {
            p.sendMessage(ChatColor.RED + "Item Functionality has Been Disabled");
            return true;
        }
        if (check_mobs(command.getName()) && !obj_file.toggle_mobs) {
            p.sendMessage(ChatColor.RED + "Mob Functionality has Been Disabled");
            return true;
        }

        //Brings up the list of items menu
        if (command.getName().equalsIgnoreCase("alist")) {
            //Resets the menu counter to 0
            player_list.players.get(player_list.get_player_place(p.getName())).invItt = 0;
            player_list.players.get(player_list.get_player_place(p.getName())).sorted = false;
            player_list.players.get(player_list.get_player_place(p.getName())).mobs = false;
            player_list.initialize_score(all_items, false);
            inv.set_inventory(all_items.get_sub_items(), get_progress(false), player_list);
            p.openInventory(inv.inventory_list.get(0));
        }

        //Brings up the list of mobs menu
        if (command.getName().equalsIgnoreCase("mlist")) {
            player_list.players.get(player_list.get_player_place(p.getName())).invItt = 0;
            player_list.players.get(player_list.get_player_place(p.getName())).sorted = false;
            player_list.players.get(player_list.get_player_place(p.getName())).mobs = true;
            player_list.initialize_score(all_mobs, true);
            mob_inv.set_inventory(all_mobs.get_sub_items(), get_progress(true), player_list);
            p.openInventory(mob_inv.inventory_list.get(0));
        }

        //Sends the item in a players hand
        if (command.getName().equalsIgnoreCase("asend")) {
            if (args.length >= 1 && args[0].equalsIgnoreCase("inventory")) {
                inv_check(p.getInventory(), p, p.getInventory().getSize());
            } else if (args.length >= 1 && args[0].equalsIgnoreCase("hotbar")) {
                inv_check(p.getInventory(), p, 9);
            } else {
                String message = (all_items.check_items(p.getInventory().getItemInMainHand(), p.getDisplayName(), false));
                if (message.contains("ubmitted")) {
                    if (obj_file.sub_item) {
                        ItemStack tempItem = new ItemStack(p.getInventory().getItemInMainHand().getType(), p.getInventory().getItemInMainHand().getAmount() - 1);
                        p.getInventory().setItemInMainHand(tempItem);
                        if (message.contains(ChatColor.GREEN.toString())) {
                            check_completed(false);
                        }
                    }
                }
                p.sendMessage(message);
            }
        }

        //Sends the progress of the challenge
        if (command.getName().equalsIgnoreCase("aprog")) {
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Progress: " + ChatColor.AQUA + Integer.toString(get_progress(false)) + "/" + all_items.indexes.size());
        }

        //Sends the progress of the mob challenge
        if (command.getName().equalsIgnoreCase("mprog")) {
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Progress: " + ChatColor.AQUA + Integer.toString(get_progress(true)) + "/" + all_mobs.indexes.size());
        }

        //Sends the score of a player
        if (command.getName().equalsIgnoreCase("aplayer")) {
            player_list.initialize_score(all_items, false);
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Please Enter Player Name");
            } else {
                player pl = player_list.get_player_from_string(args[0]);
                if (pl == null) {
                    p.sendMessage(ChatColor.RED + "Player Does Not Exist");
                } else {
                    p.sendMessage(ChatColor.LIGHT_PURPLE +pl.name + ": " + ChatColor.AQUA + pl.score);
                }
            }
        }

        //Sends the score of a player
        if (command.getName().equalsIgnoreCase("mplayer")) {
            player_list.initialize_score(all_mobs, true);
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Please Enter Player Name");
            } else {
                player pl = player_list.get_player_from_string(args[0]);
                if (pl == null) {
                    p.sendMessage(ChatColor.RED + "Player Does Not Exist");
                } else {
                    p.sendMessage(ChatColor.LIGHT_PURPLE +pl.name + ": " + ChatColor.AQUA + pl.mobScore);
                }
            }
        }

        //Checks the item that the player sends
        if (command.getName().equalsIgnoreCase("acheck")) {
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Please Enter Item Name");
            } else {
                int temp = -1;
                for (int i=0; i<all_items.items.size(); i++) {
                    if(all_items.items.get(i).item_name.equalsIgnoreCase(args[0])) {
                        temp = i;
                    }
                }
                if (temp == -1) {
                    p.sendMessage(ChatColor.RED + "Item Does Not Exist");
                } else {
                    if (all_items.items.get(temp).isFound) {
                        p.sendMessage(ChatColor.GREEN + all_items.items.get(temp).item_display_name + " Was Found By " + all_items.items.get(temp).item_founder);
                    } else {
                        p.sendMessage(ChatColor.RED + all_items.items.get(temp).item_display_name + " Has Not Been Found");
                    }
                }
            }
        }

        //Checks the mob that the player sends
        if (command.getName().equalsIgnoreCase("mcheck")) {
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Please Enter Item Name");
            } else {
                int temp = -1;
                for (int i=0; i<all_mobs.items.size(); i++) {
                    if(all_mobs.items.get(i).item_name.equalsIgnoreCase(args[0])) {
                        temp = i;
                    }
                }
                if (temp == -1) {
                    p.sendMessage(ChatColor.RED + "Mob Does Not Exist");
                } else {
                    if (all_mobs.items.get(temp).isFound) {
                        p.sendMessage(ChatColor.GREEN + all_mobs.items.get(temp).item_display_name + " Was Found By " + all_mobs.items.get(temp).item_founder);
                    } else {
                        p.sendMessage(ChatColor.RED + all_mobs.items.get(temp).item_display_name + " Has Not Been Found");
                    }
                }
            }
        }

        //Explains the item commands of the plugin
        if (command.getName().equalsIgnoreCase("ahelp")) {
            if (p.isOp()) {
                p.sendMessage(admin_help_string);
            } else {
                p.sendMessage(help_string);
            }
        }

        //Explains the mob commands of the plugin
        if (command.getName().equalsIgnoreCase("mhelp")) {
            if (p.isOp()) {
                p.sendMessage(admin_mob_help_string);
            } else {
                p.sendMessage(mob_help_string);
            }
        }


        //ADMIN COMMANDS

        //Sends the item settings of the plugin
        if (command.getName().equalsIgnoreCase("asettings")) {
            if (p.isOp()) {
                String str_sub, str_auto, temp;
                if (obj_file.sub_item) {
                    str_sub = "True";
                } else str_sub = "False";
                if (obj_file.auto_collect) {
                    str_auto = "True";
                } else str_auto = "False";
                if (obj_file.toggle_items) {
                    temp = "Enabled";
                } else temp = "Disabled";
                String toggle = ChatColor.LIGHT_PURPLE +""+ ChatColor.BOLD + "Toggled: " + ChatColor.AQUA +""+ ChatColor.BOLD + temp;
                p.sendMessage(toggle + ChatColor.LIGHT_PURPLE + "\nFile: " + ChatColor.AQUA + obj_file.file_name + ChatColor.LIGHT_PURPLE + "\nItem Subtraction: " + ChatColor.AQUA + str_sub + ChatColor.LIGHT_PURPLE + "\nAuto Collect: " + ChatColor.AQUA + str_auto);
            } else {p.sendMessage(ChatColor.RED + "You Don't Have Permission");}
        }

        //Sends the mob settings of the plugin
        if (command.getName().equalsIgnoreCase("msettings")) {
            if (p.isOp()) {
                String temp;
                if (obj_file.toggle_mobs) {
                    temp = "Enabled";
                } else temp = "Disabled";
                String toggle = ChatColor.LIGHT_PURPLE +""+ ChatColor.BOLD + "Toggled: " + ChatColor.AQUA +""+ ChatColor.BOLD + temp;
                p.sendMessage(toggle + ChatColor.LIGHT_PURPLE + "\nFile: " + ChatColor.AQUA + obj_file.mob_file_name);
            } else {p.sendMessage(ChatColor.RED + "You Don't Have Permission");}
        }

        //Resets the item data
        if (command.getName().equalsIgnoreCase("areset")) {
            if(p.isOp()) {
                if (obj_file.reset) {
                    p.sendMessage(ChatColor.DARK_GREEN + "Reset Cancelled");
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Items Will Be Reset After a Server Restart. To Cancel Run This Command Again");
                }
                obj_file.reset = !obj_file.reset;
            } else {p.sendMessage(ChatColor.RED + "You Don't Have Permission");}
        }

        //Resets the mob data
        if (command.getName().equalsIgnoreCase("mreset")) {
            if(p.isOp()) {
                if (obj_file.mob_reset) {
                    p.sendMessage(ChatColor.DARK_GREEN + "Reset Cancelled");
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Mobs Will Be Reset After a Server Restart. To Cancel Run This Command Again");
                }
                obj_file.mob_reset = !obj_file.mob_reset;
            } else {p.sendMessage(ChatColor.RED + "You Don't Have Permission");}
        }

        //Enables/Disables the item feature of the plugin
        if (command.getName().equalsIgnoreCase("atoggle")) {
            if(p.isOp()) {
                if (obj_file.toggle_items) {
                    p.sendMessage(ChatColor.DARK_RED + "All Items has Been Disabled");
                } else {
                    p.sendMessage(ChatColor.DARK_GREEN + "All Items has Been Enabled");
                }
                obj_file.toggle_items = !obj_file.toggle_items;
                set_all_permissions();
            }
        }

        //Enables/Disables the mob feature of the plugin
        if (command.getName().equalsIgnoreCase("mtoggle")) {
            if(p.isOp()) {
                if (obj_file.toggle_mobs) {
                    p.sendMessage(ChatColor.DARK_RED + "All Mobs has Been Disabled");
                } else {
                    p.sendMessage(ChatColor.DARK_GREEN + "All Mobs has Been Enabled");
                }
                obj_file.toggle_mobs = !obj_file.toggle_mobs;
                set_all_permissions();
            }
        }

        //Modifies the config
        if (command.getName().equalsIgnoreCase("aconfig")) {
            if (p.isOp()) {
                if (!(args.length == 0)) {
                    //Swaps the item file
                    if (args[0].equalsIgnoreCase("file")) {
                        if (args.length == 1) {
                            p.sendMessage(ChatColor.RED + "Please Enter Name of File");
                        } else {
                            obj_file.file_name = args[1];
                            p.sendMessage(ChatColor.DARK_GREEN + "File is Now Set to \"" + obj_file.file_name + "\" Restart Server to Make Change to the List");
                        }
                    //Toggles the auto collect setting
                    } else if (args[0].equalsIgnoreCase("autocollect")) {
                        if (obj_file.auto_collect) {
                            p.sendMessage(ChatColor.DARK_RED + "Auto-Collect Disabled");
                        } else {
                            p.sendMessage(ChatColor.DARK_GREEN + "Auto-Collect Enabled");
                        }
                        obj_file.auto_collect = !obj_file.auto_collect;
                    //Toggles the item subtraction setting
                    } else if (args[0].equalsIgnoreCase("subtract")) {
                        if (obj_file.sub_item) {
                            p.sendMessage(ChatColor.DARK_RED + "Item Subtraction Disabled");
                        } else {
                            p.sendMessage(ChatColor.DARK_GREEN + "Item Subtraction Enabled");
                        }
                        obj_file.sub_item = !obj_file.sub_item;
                    }
                } else {p.sendMessage(ChatColor.RED + "Please Enter A Setting to Change");}
            } else {p.sendMessage(ChatColor.RED + "You Don't Have Permission");}
        }

        //Modifies the config
        if (command.getName().equalsIgnoreCase("mconfig")) {
            if (p.isOp()) {
                if (!(args.length == 0)) {
                    //Swaps the item file
                    if (args[0].equalsIgnoreCase("file")) {
                        if (args.length == 1) {
                            p.sendMessage(ChatColor.RED + "Please Enter Name of File");
                        } else {
                            obj_file.mob_file_name = args[1];
                            p.sendMessage(ChatColor.DARK_GREEN + "File is Now Set to \"" + obj_file.mob_file_name + "\" Restart Server to Make Change to the List");
                        }
                    }
                } else {p.sendMessage(ChatColor.RED + "Please Enter A Setting to Change");}
            } else {p.sendMessage(ChatColor.RED + "You Don't Have Permission");}
        }

        //Submits an item from the plugin
        if (command.getName().equalsIgnoreCase("asubmit")) {
            if (p.isOp()) {
                if (args.length == 0) {
                    p.sendMessage(ChatColor.RED + "Please Enter Item Name");
                } else {
                    int temp = -1;
                    for (int i = 0; i < all_items.items.size(); i++) {
                        if (all_items.items.get(i).item_name.equalsIgnoreCase(args[0])) {
                            temp = i;
                        }
                    }
                    if (temp == -1) {
                        p.sendMessage(ChatColor.RED + "Item Does Not Exist");
                    } else {
                        if (all_items.items.get(temp).isFound) {
                            p.sendMessage(ChatColor.RED + all_items.items.get(temp).item_display_name + " Has Already Been Found");
                        } else {
                            String temp_text = "";
                            if (args.length == 1) {
                                temp_text = ChatColor.DARK_RED + "ADMIN";
                                all_items.items.get(temp).submit(temp_text, all_items.date());
                                p.sendMessage(ChatColor.GREEN + all_items.items.get(temp).item_display_name + " Has Been Submitted");
                                check_completed(false);
                            } else {
                                if (player_list.in_list(args[1])) {
                                    temp_text = args[1];
                                    all_items.items.get(temp).submit(player_list.get_player_from_string(temp_text).name, all_items.date());
                                    p.sendMessage(ChatColor.GREEN + all_items.items.get(temp).item_display_name + " Has Been Submitted");
                                    check_completed(false);
                                } else {
                                    p.sendMessage(ChatColor.RED + args[1] + " is Not in The Player List");
                                }
                            }
                        }
                    }
                }
            }
        }

        //Unsubmits an item from the plugin
        if (command.getName().equalsIgnoreCase("aunsubmit")) {
            if (p.isOp()) {
                if (args.length == 0) {
                    p.sendMessage(ChatColor.RED + "Please Enter Item Name");
                } else {
                    int temp = -1;
                    for (int i = 0; i < all_items.items.size(); i++) {
                        if (all_items.items.get(i).item_name.equalsIgnoreCase(args[0])) {
                            temp = i;
                        }
                    }
                    if (temp == -1) {
                        p.sendMessage(ChatColor.RED + "Item Does Not Exist");
                    } else {
                        if (!all_items.items.get(temp).isFound) {
                            p.sendMessage(ChatColor.RED + all_items.items.get(temp).item_display_name + " Has Not Been Found");
                        } else {
                            all_items.items.get(temp).unsubmit();
                            p.sendMessage(ChatColor.GREEN + all_items.items.get(temp).item_display_name + " Has Been Unsubmitted");
                        }
                    }
                }
            }
        }

        //Submits a mob from the plugin
        if (command.getName().equalsIgnoreCase("msubmit")) {
            if (p.isOp()) {
                if (args.length == 0) {
                    p.sendMessage(ChatColor.RED + "Please Enter Mob Name");
                } else {
                    int temp = -1;
                    for (int i = 0; i < all_mobs.items.size(); i++) {
                        if (all_mobs.items.get(i).item_name.equalsIgnoreCase(args[0])) {
                            temp = i;
                        }
                    }
                    if (temp == -1) {
                        p.sendMessage(ChatColor.RED + "Mob Does Not Exist");
                    } else {
                        if (all_mobs.items.get(temp).isFound) {
                            p.sendMessage(ChatColor.RED + all_mobs.items.get(temp).item_display_name + " Has Already Been Found");
                        } else {
                            String temp_text = "";
                            if (args.length == 1) {
                                temp_text = ChatColor.DARK_RED + "ADMIN";
                                all_mobs.items.get(temp).submit(temp_text, all_mobs.date());
                                p.sendMessage(ChatColor.GREEN + all_mobs.items.get(temp).item_display_name + " Has Been Submitted");
                                check_completed(true);
                            } else {
                                if (player_list.in_list(args[1])) {
                                    temp_text = args[1];
                                    all_mobs.items.get(temp).submit(player_list.get_player_from_string(temp_text).name, all_mobs.date());
                                    p.sendMessage(ChatColor.GREEN + all_mobs.items.get(temp).item_display_name + " Has Been Submitted");
                                    check_completed(true);
                                } else {
                                    p.sendMessage(ChatColor.RED + args[1] + " is Not in The Player List");
                                }
                            }
                        }
                    }
                }
            }
        }

        //Unsubmits a mob from the plugin
        if (command.getName().equalsIgnoreCase("munsubmit")) {
            if (p.isOp()) {
                if (args.length == 0) {
                    p.sendMessage(ChatColor.RED + "Please Enter Item Name");
                } else {
                    int temp = -1;
                    for (int i = 0; i < all_mobs.items.size(); i++) {
                        if (all_mobs.items.get(i).item_name.equalsIgnoreCase(args[0])) {
                            temp = i;
                        }
                    }
                    if (temp == -1) {
                        p.sendMessage(ChatColor.RED + "Item Does Not Exist");
                    } else {
                        if (!all_mobs.items.get(temp).isFound) {
                            p.sendMessage(ChatColor.RED + all_mobs.items.get(temp).item_display_name + " Has Not Been Found");
                        } else {
                            all_mobs.items.get(temp).unsubmit();
                            p.sendMessage(ChatColor.GREEN + all_mobs.items.get(temp).item_display_name + " Has Been Unsubmitted");
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean check_itmes(String command) {
        if (command.equalsIgnoreCase("alist") || command.equalsIgnoreCase("asend") || command.equalsIgnoreCase("acheck") || command.equalsIgnoreCase("aplayer") || command.equalsIgnoreCase("aprog")) {
            return true;
        }
        return false;
    }

    private boolean check_mobs(String command) {
        if (command.equalsIgnoreCase("mlist") || command.equalsIgnoreCase("mcheck") || command.equalsIgnoreCase("mplayer") || command.equalsIgnoreCase("mprog")) {
            return true;
        }
        return false;
    }

    //Adds the tab completions to specific commands
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        player_list.update_player_names();

        if (command.getName().equalsIgnoreCase("asubmit") || command.getName().equalsIgnoreCase("aunsubmit") || command.getName().equalsIgnoreCase("acheck")) {
            if (args.length == 1) {
                return all_items.item_names;
            }
            if (command.getName().equalsIgnoreCase("asubmit") && args.length == 2) {
                return player_list.player_names;
            }
        } else if (command.getName().equalsIgnoreCase("aplayer")) {
            if (args.length == 1) {
                return player_list.player_names;
            }
        } else if (command.getName().equalsIgnoreCase("asend")) {
            if (args.length == 1) {
                return send_list;
            }
        } else if (command.getName().equalsIgnoreCase("aconfig")) {
            if (args.length == 1) {
                return config_list;
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("file")) {
                return obj_file.file_list;
            }
        }

        if (command.getName().equalsIgnoreCase("msubmit") || command.getName().equalsIgnoreCase("munsubmit") || command.getName().equalsIgnoreCase("mcheck")) {
            if (args.length == 1) {
                return all_mobs.item_names;
            }
            if (command.getName().equalsIgnoreCase("msubmit") && args.length == 2) {
                return player_list.player_names;
            }
        } else if (command.getName().equalsIgnoreCase("mplayer")) {
            if (args.length == 1) {
                return player_list.player_names;
            }
        } else if (command.getName().equalsIgnoreCase("mconfig")) {
            if (args.length == 1) {
                return Arrays.asList("file");
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("file")) {
                return obj_file.mob_file_list;
            }
        }

        return null;
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
        if(inv.inventory_list.contains(e.getInventory()) || inv.sorted_list.contains(e.getInventory()) || mob_inv.inventory_list.contains(e.getInventory()) || mob_inv.sorted_list.contains(e.getInventory())) {
            //Prevents the player from moving any items in the menu
            e.setCancelled(true);

            int temp_player = player_list.get_player_place(p.getName());

            switch (e.getSlot()) {
                //Goes to the last page if the back arrow is pressed, unless it is on the first page
                case 45: {
                    if (!e.getInventory().equals(inv.inventory_list.get(0)) && !e.getInventory().equals(inv.sorted_list.get(0)) && !e.getInventory().equals(mob_inv.inventory_list.get(0)) && !e.getInventory().equals(mob_inv.sorted_list.get(0))) {
                        player_list.players.get(temp_player).invItt -= 1;
                    }
                    else{
                        if (!player_list.players.get(temp_player).mobs) {
                            player_list.players.get(temp_player).invItt = inv.size - 1;
                        } else {
                            player_list.players.get(temp_player).invItt = mob_inv.size - 1;
                        }
                    }

                    if (!player_list.get_player_from_string(p.getName()).mobs) {
                        if (!player_list.get_player_from_string(p.getName()).sorted) {
                            p.openInventory(inv.inventory_list.get(player_list.players.get(temp_player).invItt));
                        } else {
                            p.openInventory(inv.sorted_list.get(player_list.players.get(temp_player).invItt));
                        }
                    } else {
                        if (!player_list.get_player_from_string(p.getName()).sorted) {
                            p.openInventory(mob_inv.inventory_list.get(player_list.players.get(temp_player).invItt));
                        } else {
                            p.openInventory(mob_inv.sorted_list.get(player_list.players.get(temp_player).invItt));
                        }
                    }
                    break;
                }
                //Goes to the next page if the forward arrow is pressed, unless it is on the last page
                case 53: {
                    if (!e.getInventory().equals(inv.inventory_list.get(inv.size-1)) && !e.getInventory().equals(inv.sorted_list.get(inv.size-1)) && !e.getInventory().equals(mob_inv.inventory_list.get(mob_inv.size-1)) && !e.getInventory().equals(mob_inv.sorted_list.get((mob_inv.size-1)))) {
                        player_list.players.get(temp_player).invItt += 1;
                    }
                    else{
                        player_list.players.get(temp_player).invItt = 0;
                    }

                    if (!player_list.get_player_from_string(p.getName()).mobs) {
                        if (!player_list.get_player_from_string(p.getName()).sorted) {
                            p.openInventory(inv.inventory_list.get(player_list.players.get(temp_player).invItt));
                        } else {
                            p.openInventory(inv.sorted_list.get(player_list.players.get(temp_player).invItt));
                        }
                    } else {
                        if (!player_list.get_player_from_string(p.getName()).sorted) {
                            p.openInventory(mob_inv.inventory_list.get(player_list.players.get(temp_player).invItt));
                        } else {
                            p.openInventory(mob_inv.sorted_list.get(player_list.players.get(temp_player).invItt));
                        }
                    }

                    break;
                }
                //Handles what happens when the sort button is presses
                case 47: {
                    if (!player_list.get_player_from_string(p.getName()).mobs) {
                        player_list.players.get(temp_player).invItt = 0;
                        player_list.players.get(temp_player).sorted = !player_list.players.get(temp_player).sorted;
                        if (player_list.get_player_from_string(p.getName()).sorted) {
                            p.openInventory(inv.sorted_list.get(0));
                        } else {
                            p.openInventory(inv.inventory_list.get(0));
                        }
                    } else {
                        player_list.players.get(temp_player).invItt = 0;
                        player_list.players.get(temp_player).sorted = !player_list.players.get(temp_player).sorted;
                        if (player_list.get_player_from_string(p.getName()).sorted) {
                            p.openInventory(mob_inv.sorted_list.get(0));
                        } else {
                            p.openInventory(mob_inv.inventory_list.get(0));
                        }
                    }
                    break;
                }
            }
        }
    }

    //Handles when a player picks up an item
    @EventHandler
    public void PlayerPickupItemEvent(PlayerPickupItemEvent e) {
        if (!obj_file.toggle_items) {
            return;
        }

        if (obj_file.auto_collect) {
            if (obj_file.sub_item) {
                e.setCancelled(true);
            }
            Player p = e.getPlayer();
            String message = all_items.check_items(e.getItem().getItemStack(), p.getDisplayName(), true);
            if (message.contains("ubmitted")) {
                if (message.contains(ChatColor.GREEN.toString())) {
                    if (obj_file.sub_item) {
                        e.getItem().setItemStack(new ItemStack(Material.AIR));
                    }
                    p.sendMessage(message);
                    check_completed(false);
                }
            } else {
                e.setCancelled(false);
            }
        }
    }

    //Handles when a player kills a mob
    @EventHandler
    public void EntityDeathEvent(EntityDeathEvent e) {
        if (!obj_file.toggle_mobs) {
            return;
        }

        LivingEntity mob = (LivingEntity) e.getEntity();
        if(mob.getKiller() instanceof Player) {
            Player p = (Player) mob.getKiller();
            String name = mob.getName();
            int temp = 0;

            for (int i=0; i<all_mobs.items.size(); i++) {
                if (all_mobs.items.get(i).item_display_name.equalsIgnoreCase(name)) {
                    temp = i;
                }
            }
            if (!all_mobs.items.get(temp).isFound && all_mobs.is_in_indexes(temp)) {
                p.sendMessage(ChatColor.GREEN + name + " Was Killed By " + p.getName());
                all_mobs.items.get(temp).submit(p.getName(), all_mobs.date());
                check_completed(true);
            }
        }
    }

    //Gets the plugin to use for the file path
    public static main getPlugin(){
        return plugin;
    }

    //Checks the inventory for the /asendhot and /asendinv commands
    private void inv_check(PlayerInventory player_items, Player p, int size) {
        ArrayList<ItemStack> inventory_list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (player_items.getItem(i) == null) {
                inventory_list.add(null);
            } else {
                inventory_list.add(player_items.getItem(i));
            }
        }

        String message;
        int temp1 = 0;
        int temp2 = 0;
        for (int i = 0; i < size; i++) {
            if (inventory_list.get(i) != null) {
                message = all_items.check_items(inventory_list.get(i), p.getDisplayName(), true);
                if (message.contains("ubmitted")) {
                    if(message.contains(ChatColor.GREEN.toString())) {
                        p.sendMessage(message);
                        check_completed(false);
                        temp2++;
                        if (obj_file.sub_item) {
                            ItemStack tempItem = new ItemStack(p.getInventory().getItem(i).getType(), p.getInventory().getItem(i).getAmount() - 1);
                            p.getInventory().setItem(i, tempItem);
                        }
                    }
                    temp1++;
                }
            }
        }
        if (temp1 == 0 || temp2 == 0) {
            p.sendMessage(ChatColor.RED + "You Don't Have Any Items");
        }
    }

    //Gets the total progress
    private int get_progress(boolean is_mob) {
        ArrayList<item> temp;
        if (!is_mob) {
            temp = all_items.get_sub_items();
        } else {
            temp = all_mobs.get_sub_items();
        }
        int cnt = 0;
        for (item i : temp) {
            if (i.isFound) {
                cnt++;
            }
        }
        return cnt;
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

        if (obj_file.toggle_items) {
            player.addAttachment(getPlugin(), "all_items.items", true);
        }
        if (obj_file.toggle_mobs) {
            player.addAttachment(getPlugin(), "all_items.mobs", true);
        }
    }

    //Checks if the list is completed
    private void check_completed(boolean is_mob) {
        int total;
        String message;
        if (!is_mob) {
            total = all_items.indexes.size();
            message = ChatColor.GREEN + "All Items Have Been Collected";
        } else {
            total = all_mobs.indexes.size();
            message = ChatColor.GREEN + "All Mobs Have Been Collected";
        }
        boolean is_complete = complete(get_progress(is_mob), total);
        if (is_complete) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle(ChatColor.YELLOW + "CONGRATULATIONS", message);
            }
        }
    }

    private boolean complete(int prog, int total) {
        int temp = prog/total;
        if (temp == 1) {
            return true;
        } else {
            return false;
        }
    }

    //Initializes the string when /ahelp is called
    private void initialize_help_string() {
        ChatColor CC1 = ChatColor.GOLD;
        ChatColor CC2 = ChatColor.DARK_AQUA;
        ChatColor CC3 = ChatColor.DARK_RED;
        String b = ChatColor.WHITE + "------------------------------------------------\n";
        String t = ChatColor.GREEN + "Help Menu: ";
        String d = ChatColor.LIGHT_PURPLE + "Join The Discord: " + ChatColor.AQUA +""+ ChatColor.UNDERLINE + "https://discord.gg/zHzFgWX8KW\n";

        String Alist = CC1 + "/alist: " + CC2 + "Lists the Items You Have Found\n\n";
        String Asend1 = CC1 + "/asend: " + CC2 + "Sends the Item in Your Hand\n";
        String Asend2 = CC1 + "/asend hotbar: " + CC2 + "Sends the Item in Your Hotbar\n";
        String Asend3 = CC1 + "/asend inventory: " + CC2 + "Sends the Item in Your Inventory\n\n";
        String Asend = Asend1 + Asend2 + Asend3;
        String Aprog = CC1 + "/aprog: " + CC2 + "Displays the Total Item Progress\n\n";
        String Aplayer = CC1 + "/aplayer <player_name>: " + CC2 + "Displays the Score of a Player\n\n";
        String Acheck = CC1 + "/acheck <item_name>: " + CC2 + "Displays Whether an Item Has Been Obtained or Not\n\n";
        String Ahelp = CC1 + "/ahelp: " + CC2 + "Displays This Help Message\n\n";
        String Asettings = CC3 + "(ADMIN) " + CC1 + "/asettings " + CC2 + "Displays the Settings of the Plugin\n\n";
        String Areset = CC3 + "(ADMIN) " + CC1 + "/areset: " + CC2 + "Resets the ItemData Upon a Server Reset\n\n";
        String Aconfig1 = CC3 + "(ADMIN) " + CC1 + "/aconfig file <file_name>: " + CC2 + "Swaps the File the ItemList is Derived From\n";
        String Aconfig2 = CC3 + "(ADMIN) " + CC1 + "/aconfig autocollect: " + CC2 + "Toggles the Auto-Collection Feature\n";
        String Aconfig3 = CC3 + "(ADMIN) " + CC1 + "/aconfig subtract: " + CC2 + "Toggles the Subtraction Feature\n\n";
        String Aconfig = Aconfig1 + Aconfig2 + Aconfig3;
        String Atoggle = CC3 + "(ADMIN)" + CC1 + "/atoggle: " + CC2 + "Enables/Disables the item functionality of the plugin\n\n";
        String Asubmit = CC3 + "(ADMIN) " + CC1 + "/asubmit <item_name> <player_name (optional)>: " + CC2 + "Submits the Item Listed as the Player Listed\n\n";
        String Aunsubmit = CC3 + "(ADMIN) " + CC1 + "/aunsubmit <item_name>: " + CC2 + "Unsubmits the Item Listed\n";

        String Mlist = CC1 + "/mlist: " + CC2 + "Lists the Mobs You Have Found\n\n";
        String Mprog = CC1 + "/mprog: " + CC2 + "Displays the Total Mob Progress\n\n";
        String Mplayer = CC1 + "/mplayer <player_name>: " + CC2 + "Displays the Score of a Player\n\n";
        String Mcheck = CC1 + "/mcheck <item_name>: " + CC2 + "Displays Whether a Mob Has Been Killed or Not\n\n";
        String Mhelp = CC1 + "/mhelp: " + CC2 + "Displays This Help Message\n\n";
        String Msettings = CC3 + "(ADMIN) " + CC1 + "/msettings " + CC2 + "Displays the Settings of the Plugin\n\n";
        String Mreset = CC3 + "(ADMIN) " + CC1 + "/mreset: " + CC2 + "Resets the MobData Upon a Server Reset\n\n";
        String Mconfig = CC3 + "(ADMIN) " + CC1 + "/mconfig file <file_name>: " + CC2 + "Swaps the File the MobList is Derived From\n\n";
        String Mtoggle = CC3 + "(ADMIN)" + CC1 + "/mtoggle: " + CC2 + "Enables/Disables the mob functionality of the plugin\n\n";
        String Msubmit = CC3 + "(ADMIN) " + CC1 + "/msubmit <item_name> <player_name (optional)>: " + CC2 + "Submits the Mob Listed as the Player Listed\n\n";
        String Munsubmit = CC3 + "(ADMIN) " + CC1 + "/munsubmit <item_name>: " + CC2 + "Unsubmits the Mob Listed\n";

        help_string = b + Alist + b + Asend + b + Aprog + b + Aplayer + b + Acheck + b + Ahelp + b;
        admin_help_string = help_string + Asettings + b + Areset + b + Aconfig + b + Atoggle + b + Asubmit + b + Aunsubmit + b;
        mob_help_string = b + Mlist + b + Mprog + b + Mplayer + b + Mcheck + b + Mhelp + b;
        admin_mob_help_string = mob_help_string + Msettings + b + Mreset + b + Mconfig + b + Mtoggle + b + Msubmit + b + Munsubmit + b;

        help_string = help_string + d + b;
        admin_help_string = admin_help_string + d + b;
        mob_help_string = mob_help_string + d + b;
        admin_mob_help_string = admin_mob_help_string + d + b;
    }
}
