package xyz.quazaros.util.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xyz.quazaros.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class tabComplete implements TabCompleter {

    main Main;

    //Autofill lists
    List<String> send_list;
    List<String> timer_commands;
    List<String> timer_admin_commands;
    List<String> item_commands;
    List<String> item_admin_commands;
    List<String> mob_commands;
    List<String> mob_admin_commands;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return tabCompleteRun(sender, cmd, label, args);
    }

    public tabComplete() {
        Main = main.getPlugin();

        send_list = new ArrayList<>();

        timer_commands = new ArrayList<>();
        timer_admin_commands = new ArrayList<>();

        item_commands = new ArrayList<>();
        item_admin_commands = new ArrayList<>();
        mob_commands = new ArrayList<>();
        mob_admin_commands = new ArrayList<>();
    }

    //Initializes Lists
    public void setupLists() {
        //Send List
        send_list.add("inventory");
        send_list.add("hotbar");

        //Timer Commands
        timer_commands.add("get");
        timer_commands.add("active");
        timer_commands.add("help");

        timer_admin_commands.addAll(timer_commands);
        timer_admin_commands.add("start");
        timer_admin_commands.add("pause");
        timer_admin_commands.add("stop");
        timer_admin_commands.add("reset");
        timer_admin_commands.add("set");
        timer_admin_commands.add("placeholders");

        //Commands
        ArrayList<String> regular = new ArrayList<>();
        ArrayList<String> admin = new ArrayList<>();

        if (Main.data.general_global || Main.data.general_others) {regular.add("list");}
        if (Main.data.general_personal) {regular.add("self");}
        if (Main.data.general_progress) {regular.add("prog");}
        if (Main.data.general_player) {regular.add("player");}
        if (Main.data.general_check) {regular.add("check");}
        if (Main.data.general_settings) {regular.add("settings");}
        regular.add("help");

        admin.addAll(regular);
        admin.add("submit");
        admin.add("unsubmit");
        admin.add("reset");
        admin.add("placeholders");

        item_commands.add("send");
        item_admin_commands.add("send");

        item_commands.addAll(regular);
        item_admin_commands.addAll(admin);
        mob_commands.addAll(regular);
        mob_admin_commands.addAll(admin);
    }

    //Public Tab Complete Command
    public List<String> tabCompleteRun(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = getStrings(sender, command, args);
        list = limitSearch(list, args);
        return list;
    }

    //Adds the tab completions to specific commands
    private List<String> getStrings(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) {return null;}
        Player p = (Player) sender;

        String command_name = command.getName();
        
        if (command_name.equals("atime")) {
            if (args.length == 1) {
                return p.isOp() ? timer_admin_commands : timer_commands;
            }
        }

        Main.player_list.update_player_names();

        boolean isMob;

        if (command_name.equals("aitem")) {
            isMob = false;
            if (args.length <= 1) {
                if (p.isOp()) {
                    return item_admin_commands;
                } else {
                    return item_commands;
                }
            }
        } else if (command_name.equals("amob")) {
            isMob = true;
            if (args.length <= 1) {
                if (p.isOp()) {
                    return mob_admin_commands;
                } else {
                    return mob_commands;
                }
            }
        } else {
            return null;
        }

        //All Sub Commands

        ArrayList<String> itemPlusPlayer = new ArrayList<>();
        ArrayList<String> mobPlusPlayer = new ArrayList<>();

        //Shifts Arguments To Simplify Tab Complete
        String temp = !isMob ? "a" : "m";
        command_name = temp + args[0].toLowerCase();
        args = Arrays.copyOfRange(args, 1, args.length);

        if (command_name.equalsIgnoreCase("asubmit") || command_name.equalsIgnoreCase("aunsubmit")) {
            itemPlusPlayer.addAll(Main.player_list.player_names);
            itemPlusPlayer.addAll(Main.all_items.item_names);
        } else if (command_name.equalsIgnoreCase("msubmit") || command_name.equalsIgnoreCase("munsubmit")) {
            mobPlusPlayer.addAll(Main.player_list.player_names);
            mobPlusPlayer.addAll(Main.all_mobs.item_names);
        }

        if (command_name.equalsIgnoreCase("alist") || command_name.equalsIgnoreCase("mlist")) {
            return Main.player_list.player_names;
        } else if (command_name.equalsIgnoreCase("asubmit")) {
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

    //Limits The Search To Contain Only The Important Entries
    private List<String> limitSearch(List<String> list, String[] args) {
        if (list == null) {return null;}

        String curString = args[args.length-1];
        List<String> ret = new ArrayList<>();
        for (String s : list) {
            if (s.startsWith(curString.toLowerCase())) {
                ret.add(s);
            }
        }
        return ret;
    }
}
