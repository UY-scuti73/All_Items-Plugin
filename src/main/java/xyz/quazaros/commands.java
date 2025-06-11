package xyz.quazaros;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import xyz.quazaros.data.config.config;
import xyz.quazaros.data.config.lang;
import xyz.quazaros.data.items.item;
import xyz.quazaros.data.items.itemList;
import xyz.quazaros.data.player.player;

import java.util.ArrayList;
import java.util.List;

public class commands {

    main Main;
    lang Lang;

    //Autofill lists
    List<String> send_list;

    //Help list variables
    String help_string;
    String admin_help_string;
    String mob_help_string;
    String admin_mob_help_string;

    //Settings variables
    String item_settings;
    String mob_settings;

    public commands() {
        Main = main.getPlugin();
        Lang = Main.lang;

        send_list = new ArrayList<>();
        send_list.add("inventory");
        send_list.add("hotbar");
    }

    public void initialize() {
        initialize_help_string();
        initialize_setting_string();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;
        player pl = Main.player_list.get_player_from_string(p.getName());
        String command_name = command.getName();

        if (check_itmes(command_name) && !Main.data.item_toggle) {
            p.sendMessage(Lang.bad + Lang.itemDisabled);
            return true;
        }
        if (check_mobs(command_name) && !Main.data.mob_toggle) {
            p.sendMessage(Lang.bad + Lang.mobDisabled);
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
                String message = Main.data.general_listPriority ? message_personal : message_main;
                if (message.contains(Lang.good.toString())) {
                    if (Main.data.item_subtraction) {
                        ItemStack tempItem = new ItemStack(p.getInventory().getItemInMainHand().getType(), p.getInventory().getItemInMainHand().getAmount() - 1);
                        p.getInventory().setItemInMainHand(tempItem);
                    }
                }
                p.sendMessage(message);
            }
            Main.events.checkCompleted(false, null);
            Main.events.checkCompleted(false, pl);
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
            handle_player(p, args, false);
        }

        //Sends the score of a player
        if (command_name.equalsIgnoreCase("mplayer")) {
            handle_player(p, args, true);
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

        //Sends the item settings of the plugin
        if (command_name.equalsIgnoreCase("asettings")) {
            handle_settings(p, false);
        }

        //Sends the mob settings of the plugin
        if (command_name.equalsIgnoreCase("msettings")) {
            handle_settings(p, true);
        }

        //ADMIN COMMANDS

        //Resets the item data
        if (command_name.equalsIgnoreCase("areset")) {
            if(p.isOp()) {
                if (Main.file.reset) {
                    p.sendMessage(ChatColor.DARK_GREEN + Lang.resetCancel);
                } else {
                    p.sendMessage(ChatColor.DARK_RED + Lang.areset);
                }
                Main.file.reset = !Main.file.reset;
            } else {p.sendMessage(Lang.bad + Lang.noPermission);}
        }

        //Resets the mob data
        if (command_name.equalsIgnoreCase("mreset")) {
            if(p.isOp()) {
                if (Main.file.mob_reset) {
                    p.sendMessage(ChatColor.DARK_GREEN + Lang.resetCancel);
                } else {
                    p.sendMessage(ChatColor.DARK_RED + Lang.mreset);
                }
                Main.file.mob_reset = !Main.file.mob_reset;
            } else {p.sendMessage(Lang.bad + Lang.noPermission);}
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

        ArrayList<String> itemPlusPlayer = new ArrayList<>();
        ArrayList<String> mobPlusPlayer = new ArrayList<>();

        if (command_name.equalsIgnoreCase("asubmit") || command_name.equalsIgnoreCase("aunsubmit")) {
            itemPlusPlayer.addAll(Main.all_items.item_names);
            itemPlusPlayer.addAll(Main.player_list.player_names);
        } else if (command_name.equalsIgnoreCase("msubmit") || command_name.equalsIgnoreCase("munsubmit")) {
            mobPlusPlayer.addAll(Main.all_mobs.item_names);
            mobPlusPlayer.addAll(Main.player_list.player_names);
        }

        if (command_name.equalsIgnoreCase("asubmit")) {
            if (args.length == 1) {
                itemPlusPlayer.add("personal");
                return itemPlusPlayer;
            } else if (args.length == 2) {
                if (Main.all_items.item_exists(args[0]) || args[0].equalsIgnoreCase("personal")) {
                    return Main.player_list.player_names;
                } else if (Main.player_list.player_exists(args[0])) {
                    return Main.all_items.item_names;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("personal") && Main.player_list.player_exists(args[1])) {
                    return Main.all_items.item_names;
                }
            }
        } else if (command_name.equalsIgnoreCase("aunsubmit")) {
            if (args.length == 1) {
                return itemPlusPlayer;
            } else if (args.length == 2) {
                if (Main.player_list.player_exists(args[0])) {
                    return Main.all_items.item_names;
                }
            }
        } else if (command_name.equalsIgnoreCase("acheck")) {
            if (args.length == 1) {
                return Main.all_items.item_names;
            } else if (args.length == 2 && Main.all_items.item_exists(args[0])) {
                return Main.player_list.player_names;
            }
        } else if (command_name.equalsIgnoreCase("aplayer") || command_name.equalsIgnoreCase("aprog")) {
            if (args.length == 1) {
                return Main.player_list.player_names;
            }
        } else if (command_name.equalsIgnoreCase("asend")) {
            if (args.length == 1) {
                return send_list;
            }
        }

        if (command_name.equalsIgnoreCase("msubmit")) {
            if (args.length == 1) {
                mobPlusPlayer.add("personal");
                return mobPlusPlayer;
            } else if (args.length == 2) {
                if (Main.all_mobs.item_exists(args[0]) || args[0].equalsIgnoreCase("personal")) {
                    return Main.player_list.player_names;
                } else if (Main.player_list.player_exists(args[0])) {
                    return Main.all_mobs.item_names;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("personal") && Main.player_list.player_exists(args[1])) {
                    return Main.all_mobs.item_names;
                }
            }
        } else if (command_name.equalsIgnoreCase("munsubmit")) {
            if (args.length == 1) {
                return mobPlusPlayer;
            } else if (args.length == 2) {
                if (Main.player_list.player_exists(args[0])) {
                    return Main.all_mobs.item_names;
                }
            }
        } else if (command_name.equalsIgnoreCase("mcheck")) {
            if (args.length == 1) {
                return Main.all_mobs.item_names;
            } else if (args.length == 2 && Main.all_mobs.item_exists(args[0])) {
                return Main.player_list.player_names;
            }
        } else if (command_name.equalsIgnoreCase("mplayer") || command_name.equalsIgnoreCase("mprog")) {
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
                message = Main.data.general_listPriority ? message_personal : message_main;
                if (message.contains(Lang.good.toString())) {
                    if(message.endsWith(Lang.itemSubmitted)) {
                        p.sendMessage(message);
                        Main.events.checkCompleted(false, null);
                        Main.events.checkCompleted(false, pl);
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
            p.sendMessage(Lang.bad + Lang.youHaveNoItems);
        }
    }

    //Returns a string depending on the outcome of list_setup
    private void handle_list(boolean mob, boolean self, Player p, player pl, String[] args) {
        boolean playerFound = list_setup(mob, self, p, pl, args);
        if (!playerFound) {
            p.sendMessage(Lang.bad + Lang.playerNotFound);
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
        boolean is_public = true;
        if (personal && !mob) { //aself
            temp = targetPlayer.name + " " + Lang.itemPersonalSuffix;
            tempList = targetPlayer.item_list;
            is_public = false;
        } else if (!personal && !mob) { //alist
            temp = Lang.itemListMenu;
            tempList = Main.all_items;
        } else if (personal && mob) { //mself
            temp = pl.name + " " + Lang.mobPersonalSuffix;
            tempList = targetPlayer.mob_list;
            is_public = false;
        } else if (!personal && mob) { //mlist
            temp = Lang.mobListMenu;
            tempList = Main.all_mobs;
        } else {tempList = new itemList();}

        if (is_public) {
            if (!Main.data.general_global) {
                p.sendMessage(Lang.bad + Lang.commandDisabled);
                return true;
            }
        } else {
            if (targetPlayer.name.equalsIgnoreCase(p.getName()) && !Main.data.general_personal) {
                p.sendMessage(Lang.bad + Lang.commandDisabled);
                return true;
            } else if (!targetPlayer.name.equalsIgnoreCase(p.getName()) && !Main.data.general_others) {
                p.sendMessage(Lang.bad + Lang.commandDisabled);
                return true;
            }
        }

        pl.inv.set_inventory(tempList, temp);
        p.openInventory(pl.inv.inventory_list.get(0));
        return true;
    }

    //Handles progress commands
    private void handle_prog(Player p, String[] args, boolean mob) {
        if (!Main.data.general_progress) {
            p.sendMessage(Lang.bad + Lang.commandDisabled);
            return;
        }

        boolean is_public = true;

        itemList tempList = null;
        if (args.length >= 1) {
            if (!Main.player_list.player_exists(args[0])) {
                p.sendMessage(Lang.bad + Lang.playerNotFound);
                return;
            } else {
                tempList = !mob ? Main.player_list.get_player_from_string(args[0]).item_list : Main.player_list.get_player_from_string(args[0]).mob_list;
                is_public = false;
            }
        } else if  (args.length == 0) {
            tempList = !mob ? Main.all_items : Main.all_mobs;
            is_public = true;
        }

        if (tempList == null) {return;}

        if (is_public) {
            if (!Main.data.general_global) {
                p.sendMessage(Lang.bad + Lang.commandDisabled);
                return;
            }
        } else {
            if (args[0].equalsIgnoreCase(p.getName()) && !Main.data.general_personal) {
                p.sendMessage(Lang.commandDisabled);
                return;
            } else if (!args[0].equalsIgnoreCase(p.getName()) && !Main.data.general_others) {
                p.sendMessage(Lang.bad + Lang.commandDisabled);
                return;
            }
        }

        p.sendMessage(Lang.dom + Lang.progress + ": " + Lang.sec + tempList.progPer());
    }

    //Handles check commands
    private void handle_check(Player p, String[] args, boolean mob) {
        if (!Main.data.general_check) {
            p.sendMessage(Lang.bad + Lang.commandDisabled);
            return;
        }

        String item = "";
        itemList tempList = null;
        boolean is_public = args.length == 1;

        if (args.length == 0) {
            if (!mob) {p.sendMessage(Lang.bad + Lang.enterItem);}
            else {p.sendMessage(Lang.bad + Lang.enterMob);}
            return;
        } else if (args.length >= 2) {
            if ( ( !mob && !Main.all_items.item_exists(args[0]) ) || ( mob && !Main.all_mobs.item_exists(args[0]) ) ) {
                if (!mob) {p.sendMessage(Lang.bad + Lang.itemNotFound);}
                else {p.sendMessage(Lang.bad + Lang.mobNotFound);}
                return;
            } else {
                if (!Main.player_list.player_exists(args[1])) {
                    p.sendMessage(Lang.bad + Lang.playerNotFound);
                    return;
                } else {
                    item = args[0];
                    tempList = !mob ? Main.player_list.get_player_from_string(args[1]).item_list : Main.player_list.get_player_from_string(args[1]).mob_list;
                }
            }
        } else if (args.length >= 1) {
            if ( ( !mob && !Main.all_items.item_exists(args[0]) ) || ( mob && !Main.all_mobs.item_exists(args[0]) ) ) {
                if (!mob) {p.sendMessage(Lang.bad + Lang.itemNotFound);}
                else {p.sendMessage(Lang.bad + Lang.mobNotFound);}
                return;
            } else {
                item = args[0];
                tempList = !mob ? Main.all_items : Main.all_mobs;
            }
        }

        if (tempList == null || item.isEmpty()) {return;}

        if (is_public) {
            if (!Main.data.general_global) {
                p.sendMessage(Lang.bad + Lang.commandDisabled);
                return;
            }
        } else {
            if (args[1].equalsIgnoreCase(p.getName()) && !Main.data.general_personal) {
                p.sendMessage(Lang.bad + Lang.commandDisabled);
                return;
            } else if (!args[1].equalsIgnoreCase(p.getName()) && !Main.data.general_others) {
                p.sendMessage(Lang.bad + Lang.commandDisabled);
                return;
            }
        }

        int temp = tempList.get_item_index(item);
        if (temp == -1) {
            if (!mob) {p.sendMessage(Lang.bad + Lang.itemNotFound);}
            else {p.sendMessage(Lang.bad + Lang.mobNotFound);}
            return;
        }

        item tempItem = tempList.items.get(temp);

        if (tempItem.isFound) {
            if (is_public) {p.sendMessage(Lang.good + tempItem.item_display_name + " " + Lang.hasBeenFoundBy + " " + tempItem.item_founder);}
            else {p.sendMessage(Lang.good + tempItem.item_display_name + " " + Lang.hasBeenFound);}
        } else {
            p.sendMessage(Lang.bad + tempItem.item_display_name + " " + Lang.hasNotBeenFound);
        }
    }

    //Handles player commands
    private void handle_player(Player p, String[] args, boolean mob) {
        if (!Main.data.general_player) {
            p.sendMessage(Lang.bad + Lang.commandDisabled);
            return;
        }

        if (args.length == 0) {
            p.sendMessage(Lang.bad + Lang.enterPlayer);
            return;
        }

        if (!Main.data.general_global) {
            p.sendMessage(Lang.bad + Lang.commandDisabled);
            return;
        }

        if (!mob) {
            Main.player_list.initialize_score(Main.all_items, false);
        } else {
            Main.player_list.initialize_score(Main.all_mobs, true);
        }

        player tempPlayer = Main.player_list.get_player_from_string(args[0]);
        if (tempPlayer == null) {
            p.sendMessage(Lang.bad + Lang.playerNotFound);
        } else {
            p.sendMessage(Lang.dom + tempPlayer.name + ": " + Lang.sec + tempPlayer.score);
        }
    }

    //Handles setting commands
    private void handle_settings(Player p, boolean mob) {
        if (!Main.data.general_settings) {
            p.sendMessage(Lang.bad + Lang.commandDisabled);
            return;
        }

        if (!mob) {
            p.sendMessage(item_settings);
        } else {
            p.sendMessage(mob_settings);
        }
    }

    //Handles submit and unsubmit commands
    private void handle_submit(Player p, String[] args, boolean mob, boolean unsub) {
        if (!p.isOp()) {
            p.sendMessage(Lang.bad + Lang.noPermission);
            return;
        }
        if (args.length == 0) {
            if (!mob) {p.sendMessage(Lang.bad + Lang.enterItem);}
            else {p.sendMessage(Lang.bad + Lang.enterMob);}
            return;
        }

        itemList tempList = null;
        player pl = null;
        String targetPlayer = "";
        String item = "";

        if (args.length >= 3 && args[0].equalsIgnoreCase("personal") && !unsub) {
            if (!Main.player_list.player_exists(args[1])) {
                p.sendMessage(Lang.bad + Lang.playerNotFound);
                return;
            }
            pl = Main.player_list.get_player_from_string(args[1]);
            if ( ( !mob && !pl.item_list.item_exists(args[2]) ) || ( mob && !pl.mob_list.item_exists(args[2]) ) ) {
                if (!mob) {p.sendMessage(Lang.bad + Lang.itemNotFound);}
                else {p.sendMessage(Lang.bad + Lang.mobKilled);}
                return;
            }
            item = args[2];
            tempList = !mob ? pl.item_list : pl.mob_list;
        } else if (args.length >= 2) {
            if ( (!unsub && ( ( ( !mob && !Main.all_items.item_exists(args[0]) ) || ( mob && !Main.all_mobs.item_exists(args[0]) ) ) && !Main.player_list.player_exists(args[0]) ) ) || ( unsub && !Main.player_list.player_exists(args[0]) ) ) {
                if (!unsub) {
                    if (!mob) {p.sendMessage(Lang.bad + Lang.playerNotFound);}
                    else {p.sendMessage(Lang.bad + Lang.playerNotFound);}
                } else {
                    p.sendMessage(Lang.bad + Lang.playerNotFound);
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
                        p.sendMessage(Lang.bad + Lang.playerNotFound);
                        return;
                    }
                } else if (Main.player_list.player_exists(args[0])) {
                    pl = Main.player_list.get_player_from_string(args[0]);
                    tempList = !mob ? pl.item_list : pl.mob_list;
                    if (tempList.item_exists(args[1])) {
                        item = args[1];
                    } else {
                        if (!mob) {p.sendMessage(Lang.bad + Lang.itemNotFound);}
                        else {p.sendMessage(Lang.bad + Lang.mobNotFound);}
                        return;
                    }
                }
            }
        } else if (args.length >= 1) {
            if ( ( !mob && Main.all_items.item_exists(args[0]) ) || ( mob && Main.all_mobs.item_exists(args[0]) ) ) {
                item = args[0];
                tempList = !mob ? Main.all_items : Main.all_mobs;
            } else {
                if (!mob) {p.sendMessage(Lang.bad + Lang.itemNotFound);}
                else {p.sendMessage(Lang.bad + Lang.mobNotFound);}
                return;
            }
        }

        if (tempList == null) {return;}

        int temp = tempList.get_item_index(item);
        if (temp == -1) {
            if (!mob) {p.sendMessage(Lang.bad + Lang.itemNotFound);}
            else {p.sendMessage(Lang.bad + Lang.mobNotFound);}
            return;
        }

        item tempItem = tempList.items.get(temp);

        if (!unsub && tempItem.isFound) {
            p.sendMessage(Lang.bad + tempItem.item_display_name + " " + Lang.subAlreadyFound);
            return;
        } else if (unsub && !tempItem.isFound) {
            p.sendMessage(Lang.bad + tempItem.item_display_name + " " + Lang.subNotFound);
            return;
        }

        if (!unsub) {
            String targetText = targetPlayer.isEmpty() ? ChatColor.DARK_RED + Lang.admin : targetPlayer;
            tempItem.submit(targetText, tempList.date());
            p.sendMessage(Lang.good + tempItem.item_display_name + " " + Lang.submit);
            Main.events.checkCompleted(mob, null);
            Main.events.checkCompleted(mob, pl);
        } else {
            tempItem.unsubmit();
            p.sendMessage(Lang.good + tempItem.item_display_name + " " + Lang.unsubmit);
            tempList.completed = false;
        }
    }

    //Initializes the help string
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

    //Initialize the setting string
    private void initialize_setting_string() {
        String str_item, str_mob, str_sub, str_auto, str_global, str_personal, str_others, str_priority, str_progress, str_check, str_player, str_settings;
        config Data = Main.data;

        str_item = Data.item_toggle ? "Enabled" :  "Disabled";
        str_mob = Data.mob_toggle ? "Enabled" :  "Disabled";
        str_sub = Data.item_subtraction ? "True" :  "False";
        str_auto = Data.item_autoCollect ? "True" :  "False";
        str_global = Data.general_global ? "True" :  "False";
        str_personal = Data.general_personal ? "True" :  "False";
        str_others = Data.general_others ? "True" :  "False";
        str_priority = Data.general_listPriority ? "Personal" : "Global";
        str_progress = Data.general_progress ? "True" :  "False";
        str_check = Data.general_check ? "True" :  "False";
        str_player = Data.general_player ? "True" :  "False";
        str_settings = Data.general_settings ? "True" :  "False";

        String p1 = Lang.dom +""+ ChatColor.BOLD + "Items Toggled: " + Lang.sec +""+ ChatColor.BOLD + str_item;
        String p2 = Lang.dom +""+ ChatColor.BOLD + "Mobs Toggled: " + Lang.sec +""+ ChatColor.BOLD + str_mob;
        String p3 = Lang.dom + "Item File: " + Lang.sec + Data.item_file;
        String p4 = Lang.dom + "Mob File: " + Lang.sec + Data.mob_file;
        String p5 = Lang.dom + "Subtraction: " + Lang.sec + str_sub;
        String p6 = Lang.dom + "Auto Collection: " + Lang.sec + str_auto;
        String p7 = Lang.dom + "Global List: " + Lang.sec + str_global;
        String p8 = Lang.dom + "Personal Lists: " + Lang.sec + str_personal;
        String p9 = Lang.dom + "Other Lists: " + Lang.sec + str_others;
        String p10 = Lang.dom + "List Priority: " + Lang.sec + str_priority;
        String p11 = Lang.dom + "Progress Command: " + Lang.sec + str_progress;
        String p12 = Lang.dom + "Check Command: " + Lang.sec + str_check;
        String p13 = Lang.dom + "Player Command: " + Lang.sec + str_player;
        String p14 = Lang.dom + "Settings Command: " + Lang.sec + str_settings;

        String general = p7 + "\n" + p8 + "\n" + p9 + "\n" + p10 + "\n" + p11 + "\n" + p12 + "\n" + p13 + "\n" + p14;

        item_settings = p1 + "\n" + p2 + "\n" + p3 + "\n" + p5 + "\n" + p6 + "\n" + general;
        mob_settings = p1 + "\n" + p2 + "\n" + p4 + general;
    }
}
