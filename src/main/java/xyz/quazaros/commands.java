package xyz.quazaros;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import xyz.quazaros.data.items.item;
import xyz.quazaros.data.items.itemList;
import xyz.quazaros.data.player.player;

import java.util.ArrayList;
import java.util.List;

public class commands {

    main Main;

    //Autofill lists
    List<String> send_list;

    //Help list variables
    String help_string;
    String admin_help_string;
    String mob_help_string;
    String admin_mob_help_string;

    public commands() {
        Main = main.getPlugin();

        send_list = new ArrayList<>();
        send_list.add("inventory");
        send_list.add("hotbar");

        initialize_help_string();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;
        player pl = Main.player_list.get_player_from_string(p.getName());
        String command_name = command.getName();

        if (check_itmes(command_name) && !Main.data.item_toggle) {
            p.sendMessage(ChatColor.RED + "Item Functionality has Been Disabled");
            return true;
        }
        if (check_mobs(command_name) && !Main.data.mob_toggle) {
            p.sendMessage(ChatColor.RED + "Mob Functionality has Been Disabled");
            return true;
        }

        //Brings up the list of self items menu
        if (command_name.equalsIgnoreCase("aself")) {
            handle_list(false, true, p, pl, args);
        }

        //Brings up the list of items menu
        if (command_name.equalsIgnoreCase("alist")) {
            handle_list(false, false, p, pl, args);
        }

        //Brings up the list of self mobs menu
        if (command_name.equalsIgnoreCase("mself")) {
            handle_list(true, true, p, pl, args);
        }

        //Brings up the list of mobs menu
        if (command_name.equalsIgnoreCase("mlist")) {
            handle_list(true, false, p, pl, args);
        }

        //Sends the item in a players hand
        if (command_name.equalsIgnoreCase("asend")) {
            if (args.length >= 1 && args[0].equalsIgnoreCase("inventory")) {
                inv_check(p.getInventory(), p, p.getInventory().getSize());
            } else if (args.length >= 1 && args[0].equalsIgnoreCase("hotbar")) {
                inv_check(p.getInventory(), p, 9);
            } else {
                ItemStack current_item = p.getInventory().getItemInMainHand();
                String message_main = Main.all_items.check_items(current_item, p.getDisplayName(), false);
                String message_personal = pl.item_list.check_items(current_item, p.getDisplayName(), false);
                String message = Main.data.item_listPriority ? message_personal : message_main;
                if (message.contains("ubmitted")) {
                    if (Main.data.item_subtraction) {
                        ItemStack tempItem = new ItemStack(p.getInventory().getItemInMainHand().getType(), p.getInventory().getItemInMainHand().getAmount() - 1);
                        p.getInventory().setItemInMainHand(tempItem);
                    }
                }
                p.sendMessage(message);
            }
            Main.check_completed(false);
            Main.check_completed(false, pl);
        }

        //Sends the progress of the challenge
        if (command_name.equalsIgnoreCase("aprog")) {
            handle_prog(p, args, false);
        }

        //Sends the progress of the mob challenge
        if (command_name.equalsIgnoreCase("mprog")) {
            handle_prog(p, args, true);
        }

        //Sends the score of a player
        if (command_name.equalsIgnoreCase("aplayer")) {
            Main.player_list.initialize_score(Main.all_items, false);
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Please Enter Player Name");
            } else {
                player tempPlayer = Main.player_list.get_player_from_string(args[0]);
                if (tempPlayer == null) {
                    p.sendMessage(ChatColor.RED + "Player Does Not Exist");
                } else {
                    p.sendMessage(ChatColor.LIGHT_PURPLE + tempPlayer.name + ": " + ChatColor.AQUA + tempPlayer.score);
                }
            }
        }

        //Sends the score of a player
        if (command_name.equalsIgnoreCase("mplayer")) {
            Main.player_list.initialize_score(Main.all_mobs, true);
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Please Enter Player Name");
            } else {
                player tempPlayer = Main.player_list.get_player_from_string(args[0]);
                if (tempPlayer == null) {
                    p.sendMessage(ChatColor.RED + "Player Does Not Exist");
                } else {
                    p.sendMessage(ChatColor.LIGHT_PURPLE +tempPlayer.name + ": " + ChatColor.AQUA + tempPlayer.mobScore);
                }
            }
        }

        //Checks the item that the player sends
        if (command_name.equalsIgnoreCase("acheck")) {
            handle_check(p, args, false);
        }

        //Checks the mob that the player sends
        if (command_name.equalsIgnoreCase("mcheck")) {
            handle_check(p, args, true);
        }

        //Explains the item commands of the plugin
        if (command_name.equalsIgnoreCase("ahelp")) {
            if (p.isOp()) {
                p.sendMessage(admin_help_string);
            } else {
                p.sendMessage(help_string);
            }
        }

        //Explains the mob commands of the plugin
        if (command_name.equalsIgnoreCase("mhelp")) {
            if (p.isOp()) {
                p.sendMessage(admin_mob_help_string);
            } else {
                p.sendMessage(mob_help_string);
            }
        }

        //ADMIN COMMANDS

        //Sends the item settings of the plugin
        if (command_name.equalsIgnoreCase("asettings")) {
            if (p.isOp()) {
                String str_sub, str_auto, temp;
                if (Main.data.item_subtraction) {
                    str_sub = "True";
                } else str_sub = "False";
                if (Main.data.item_autoCollect) {
                    str_auto = "True";
                } else str_auto = "False";
                if (Main.data.item_toggle) {
                    temp = "Enabled";
                } else temp = "Disabled";
                String toggle = ChatColor.LIGHT_PURPLE +""+ ChatColor.BOLD + "Toggled: " + ChatColor.AQUA +""+ ChatColor.BOLD + temp;
                p.sendMessage(toggle + ChatColor.LIGHT_PURPLE + "\nFile: " + ChatColor.AQUA + Main.data.item_file + ChatColor.LIGHT_PURPLE + "\nItem Subtraction: " + ChatColor.AQUA + str_sub + ChatColor.LIGHT_PURPLE + "\nAuto Collect: " + ChatColor.AQUA + str_auto);
            } else {p.sendMessage(ChatColor.RED + "You Don't Have Permission");}
        }

        //Sends the mob settings of the plugin
        if (command_name.equalsIgnoreCase("msettings")) {
            if (p.isOp()) {
                String temp;
                if (Main.data.mob_toggle) {
                    temp = "Enabled";
                } else temp = "Disabled";
                String toggle = ChatColor.LIGHT_PURPLE +""+ ChatColor.BOLD + "Toggled: " + ChatColor.AQUA +""+ ChatColor.BOLD + temp;
                p.sendMessage(toggle + ChatColor.LIGHT_PURPLE + "\nFile: " + ChatColor.AQUA + Main.data.mob_file);
            } else {p.sendMessage(ChatColor.RED + "You Don't Have Permission");}
        }

        //Resets the item data
        if (command_name.equalsIgnoreCase("areset")) {
            if(p.isOp()) {
                if (Main.file.reset) {
                    p.sendMessage(ChatColor.DARK_GREEN + "Reset Cancelled");
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Items Will Be Reset After a Server Restart. To Cancel Run This Command Again");
                }
                Main.file.reset = !Main.file.reset;
            } else {p.sendMessage(ChatColor.RED + "You Don't Have Permission");}
        }

        //Resets the mob data
        if (command_name.equalsIgnoreCase("mreset")) {
            if(p.isOp()) {
                if (Main.file.mob_reset) {
                    p.sendMessage(ChatColor.DARK_GREEN + "Reset Cancelled");
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Mobs Will Be Reset After a Server Restart. To Cancel Run This Command Again");
                }
                Main.file.mob_reset = !Main.file.mob_reset;
            } else {p.sendMessage(ChatColor.RED + "You Don't Have Permission");}
        }

        //Submits an item from the plugin
        if (command_name.equalsIgnoreCase("asubmit")) {
            handle_submit(p, args, false, false);
        }

        //Unsubmits an item from the plugin
        if (command_name.equalsIgnoreCase("aunsubmit")) {
            handle_submit(p, args, false, true);
        }

        //Submits a mob from the plugin
        if (command_name.equalsIgnoreCase("msubmit")) {
            handle_submit(p, args, true, false);
        }

        //Unsubmits a mob from the plugin
        if (command_name.equalsIgnoreCase("munsubmit")) {
            handle_submit(p, args, true, true);
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
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Main.player_list.update_player_names();
        String command_name = command.getName();

        if (command_name.equalsIgnoreCase("asubmit") || command_name.equalsIgnoreCase("aunsubmit") || command_name.equalsIgnoreCase("acheck")) {
            if (args.length == 1) {
                return Main.all_items.item_names;
            }
            if (command_name.equalsIgnoreCase("asubmit") && args.length == 2) {
                return Main.player_list.player_names;
            }
        } else if (command_name.equalsIgnoreCase("aplayer")) {
            if (args.length == 1) {
                return Main.player_list.player_names;
            }
        } else if (command_name.equalsIgnoreCase("asend")) {
            if (args.length == 1) {
                return send_list;
            }
        }

        if (command_name.equalsIgnoreCase("msubmit") || command_name.equalsIgnoreCase("munsubmit") || command_name.equalsIgnoreCase("mcheck")) {
            if (args.length == 1) {
                return Main.all_mobs.item_names;
            }
            if (command_name.equalsIgnoreCase("msubmit") && args.length == 2) {
                return Main.player_list.player_names;
            }
        } else if (command_name.equalsIgnoreCase("mplayer")) {
            if (args.length == 1) {
                return Main.player_list.player_names;
            }
        }

        return null;
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

        player pl = Main.player_list.get_player_from_string(p.getName());
        String message;
        String message_main;
        String message_personal;
        int temp1 = 0;
        int temp2 = 0;
        for (int i = 0; i < size; i++) {
            if (inventory_list.get(i) != null) {
                message_main = Main.all_items.check_items(inventory_list.get(i), p.getDisplayName(), true);
                message_personal = pl.item_list.check_items(inventory_list.get(i), p.getDisplayName(), true);
                message = Main.data.item_listPriority ? message_personal : message_main;
                if (message.contains("ubmitted")) {
                    if(message.contains(ChatColor.GREEN.toString())) {
                        p.sendMessage(message);
                        Main.check_completed(false);
                        Main.check_completed(false, pl);
                        temp2++;
                        if (Main.data.item_subtraction) {
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

    //Returns a string depending on the outcome of list_setup
    private void handle_list(boolean mob, boolean self, Player p, player pl, String[] args) {
        boolean playerFound = list_setup(mob, self, p, pl, args);
        if (!playerFound) {
            p.sendMessage(ChatColor.RED + "Player Not Found");
        }
    }

    //Handles what happens with lists
    private boolean list_setup(boolean mob, boolean self, Player p, player pl, String[] args) {
        if (!Main.player_list.player_exists(p.getName())) {
            return false;
        }
        pl.invItt = 0;
        pl.sorted = false;
        pl.mobs = mob;
        Main.player_list.initialize_score(pl.item_list, false);

        player targetPlayer = null;
        boolean personal = false;
        if (self) {
            personal = true;
            targetPlayer = pl;
        }
        else if (args.length >= 1) {
            personal = true;
            if (Main.player_list.player_exists(args[0])) {
                targetPlayer = Main.player_list.get_player_from_string(args[0]);
            } else {
                return false;
            }
        }

        String temp = "";
        itemList tempList;
        if (personal && !mob) { //aself
            temp = targetPlayer.name + " " + "Items List";
            tempList = targetPlayer.item_list;
        } else if (!personal && !mob) { //alist
            temp = "All Items List";
            tempList = Main.all_items;
        } else if (personal && mob) { //mself
            temp = pl.name + " " + "Mobs List";
            tempList = targetPlayer.mob_list;
        } else if (!personal && mob) { //mlist
            temp = "All Mobs List";
            tempList = Main.all_mobs;
        } else {tempList = new itemList();}
        pl.inv.set_inventory(tempList, temp);
        p.openInventory(pl.inv.inventory_list.get(0));
        return true;
    }

    //Handles progress commands
    private void handle_prog(Player p, String[] args, boolean mob) {
        itemList tempList = null;
        if (args.length >= 1) {
            if (!Main.player_list.player_exists(args[0])) {
                p.sendMessage(ChatColor.RED + "Player Not Found");
                return;
            } else {
                tempList = !mob ? Main.player_list.get_player_from_string(args[0]).item_list : Main.player_list.get_player_from_string(args[0]).mob_list;
            }
        } else if  (args.length == 0) {
            tempList = !mob ? Main.all_items : Main.all_mobs;
        }

        if (tempList == null) {return;}

        p.sendMessage(ChatColor.LIGHT_PURPLE + "Progress: " + ChatColor.AQUA + tempList.progPer());
    }

    //Handles check commands
    private void handle_check(Player p, String[] args, boolean mob) {
        String item = "";
        itemList tempList = null;
        boolean is_public = args.length == 1;

        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "Please Enter Item Name");
            return;
        } else if (args.length >= 2) {
            if ( ( !mob && !Main.all_items.item_exists(args[0]) ) || ( mob && !Main.all_mobs.item_exists(args[0]) ) ) {
                if (!mob) {p.sendMessage(ChatColor.RED + "Item Does Not Exist");}
                else {p.sendMessage(ChatColor.RED + "Mob Does Not Exist");}
                return;
            } else {
                if (!Main.player_list.player_exists(args[1])) {
                    p.sendMessage(ChatColor.RED + "Player Not Found");
                    return;
                } else {
                    item = args[0];
                    tempList = !mob ? Main.player_list.get_player_from_string(args[1]).item_list : Main.player_list.get_player_from_string(args[1]).mob_list;
                }
            }
        } else if (args.length >= 1) {
            if ( ( !mob && !Main.all_items.item_exists(args[0]) ) || ( mob && !Main.all_mobs.item_exists(args[0]) ) ) {
                if (!mob) {p.sendMessage(ChatColor.RED + "Item Does Not Exist");}
                else {p.sendMessage(ChatColor.RED + "Mob Does Not Exist");}
                return;
            } else {
                item = args[0];
                tempList = !mob ? Main.all_items : Main.all_mobs;
            }
        }

        if (tempList == null || item.isEmpty()) {return;}

        int temp = tempList.get_item_index(item);
        if (temp == -1) {
            if (!mob) {p.sendMessage(ChatColor.RED + "Item Does Not Exist");}
            else {p.sendMessage(ChatColor.RED + "Mob Does Not Exist");}
            return;
        }

        item tempItem = tempList.items.get(temp);

        if (tempItem.isFound) {
            if (is_public) {p.sendMessage(ChatColor.GREEN + tempItem.item_display_name + " Has Been Found By " + tempItem.item_founder);}
            else {p.sendMessage(ChatColor.GREEN + tempItem.item_display_name + " Has Been Found");}
        } else {
            p.sendMessage(ChatColor.RED + tempItem.item_display_name + " Has Not Been Found");
        }
    }

    //Handles submit and unsubmit commands
    private void handle_submit(Player p, String[] args, boolean mob, boolean unsub) {
        if (!p.isOp()) {
            p.sendMessage(ChatColor.RED + "You Don't Have Permission");
            return;
        }
        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "Please Enter Item Name");
            return;
        }

        itemList tempList = null;
        player pl = null;
        String targetPlayer = "";
        String item = "";

        if (args.length >= 3 && args[0].equalsIgnoreCase("personal") && !unsub) {
            if (!Main.player_list.player_exists(args[1])) {
                p.sendMessage(ChatColor.RED + "Player Not Found");
                return;
            }
            pl = Main.player_list.get_player_from_string(args[1]);
            if (!pl.item_list.item_exists(args[2]) && !pl.mob_list.item_exists(args[2])) {
                if (!mob) {p.sendMessage(ChatColor.RED + "Item Does Not Exist");}
                else {p.sendMessage(ChatColor.RED + "Mob Does Not Exist");}
                return;
            }
            item = args[2];
            tempList = mob ? pl.item_list : pl.mob_list;
        } else if (args.length >= 2) {
            if ( (!unsub && ( ( ( !mob && !Main.all_items.item_exists(args[0]) ) || ( mob && !Main.all_mobs.item_exists(args[0]) ) ) && !Main.player_list.player_exists(args[0]) ) ) || ( unsub && !Main.player_list.player_exists(args[0]) ) ) {
                if (!unsub) {
                    if (!mob) {p.sendMessage(ChatColor.RED + "Item Does Not Exist");}
                    else {p.sendMessage(ChatColor.RED + "Mob Does Not Exist");}
                } else {
                    p.sendMessage(ChatColor.RED + "Player Not Found");
                }
                return;
            } else {
                if (!unsub && (Main.all_items.item_exists(args[0]) || Main.all_mobs.item_exists(args[0]))) {
                    if (Main.player_list.player_exists(args[1])) {
                        if (!mob) {tempList = Main.all_items;}
                        else {tempList = Main.all_mobs;}
                        item = args[0];
                        targetPlayer = args[1];
                    } else {
                        p.sendMessage(ChatColor.RED + "Player Not Found");
                        return;
                    }
                } else if (Main.player_list.player_exists(args[0])) {
                    pl = Main.player_list.get_player_from_string(args[0]);
                    tempList = !mob ? pl.item_list : pl.mob_list;
                    if (tempList.item_exists(args[1])) {
                        item = args[1];
                    } else {
                        if (!mob) {p.sendMessage(ChatColor.RED + "Item Does Not Exist");}
                        else {p.sendMessage(ChatColor.RED + "Mob Does Not Exist");}
                        return;
                    }
                }
            }
        } else if (args.length >= 1) {
            if ( ( !mob && Main.all_items.item_exists(args[0]) ) || ( mob && Main.all_mobs.item_exists(args[0]) ) ) {
                item = args[0];
                tempList = !mob ? Main.all_items : Main.all_mobs;
            } else {
                if (!mob) {p.sendMessage(ChatColor.RED + "Item Does Not Exist");}
                else {p.sendMessage(ChatColor.RED + "Mob Does Not Exist");}
                return;
            }
        }

        if (tempList == null) {return;}

        int temp = tempList.get_item_index(item);
        if (temp == -1) {
            if (!mob) {p.sendMessage(ChatColor.RED + "Item Does Not Exist");}
            else {p.sendMessage(ChatColor.RED + "Mob Does Not Exist");}
            return;
        }

        item tempItem = tempList.items.get(temp);

        if (!unsub && tempItem.isFound) {
            p.sendMessage(ChatColor.RED + tempItem.item_display_name + " Has Already Been Found");
            return;
        } else if (unsub && !tempItem.isFound) {
            p.sendMessage(ChatColor.RED + tempItem.item_display_name + " Has Not Been Found");
            return;
        }

        if (!unsub) {
            String targetText = targetPlayer.isEmpty() ? ChatColor.DARK_RED + "ADMIN" : targetPlayer;
            tempItem.submit(targetText, tempList.date());
            p.sendMessage(ChatColor.GREEN + tempItem.item_display_name + " Has Been Submitted");
            Main.check_completed(mob);
            Main.check_completed(mob, pl);
        } else {
            tempItem.unsubmit();
            p.sendMessage(ChatColor.GREEN + tempItem.item_display_name + " Has Been Unsubmitted");
            tempList.completed = false;
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
