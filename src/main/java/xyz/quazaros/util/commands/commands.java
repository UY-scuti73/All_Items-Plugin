package xyz.quazaros.util.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.quazaros.util.commands.helpCommands.*;
import xyz.quazaros.util.files.config.config;
import xyz.quazaros.util.files.config.lang;
import xyz.quazaros.structures.player.player;
import xyz.quazaros.main;

import java.util.Arrays;

import static xyz.quazaros.util.commands.itemCommands.check.handle_check;
import static xyz.quazaros.util.commands.itemCommands.list.handle_list;
import static xyz.quazaros.util.commands.itemCommands.player.handle_player;
import static xyz.quazaros.util.commands.itemCommands.prog.handle_prog;
import static xyz.quazaros.util.commands.itemCommands.reset.handle_reset;
import static xyz.quazaros.util.commands.itemCommands.send.handle_send;
import static xyz.quazaros.util.commands.itemCommands.submit.handle_submit;
import static xyz.quazaros.util.commands.timeCommands.timeCommands.*;
import static xyz.quazaros.util.main.initialize.reset;

public class commands implements CommandExecutor {

    main Main;
    lang Lang;

    help Help;
    settings Settings;
    mainHelp MainHelp;
    placeholderHelp PlaceholderHelp;
    timerHelp TimerHelp;

    //Which list to prioritize
    boolean list_priority_public;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return sendCommand(sender, cmd, label, args);
    }

    public commands() {
        Main = main.getPlugin();
        Lang = Main.lang;

        Help = new help();
        Settings = new settings();
        MainHelp = new mainHelp();
        PlaceholderHelp = new placeholderHelp();
        TimerHelp = new timerHelp();
    }

    public void initialize() {
        Help.initialize();
        Settings.initialize();
        MainHelp.initialize();
        PlaceholderHelp.initialize();
        TimerHelp.initialize();

        config Config = Main.data;
        if (Config.general_global && !Config.general_others) {list_priority_public = true;}
        else if (Config.general_others && !Config.general_global) {list_priority_public = false;}
        else {list_priority_public = !Config.general_listPriority;}
    }

    public boolean sendCommand(CommandSender sender, Command command, String label, String[] args) {

        //Initialize Player And Check For Errors

        if (!(sender instanceof Player)) {return true;}
        Player p = (Player) sender;
        player pl = Main.player_list.get_player_from_string(p.getName());

        String command_name = command.getName();

        if (!Main.commandNames.contains(command_name)) {return false;}

        //Return the right logic for the right command

        switch(command_name) {
            case "ahelp":
                aHelp(p);
                break;
            case "atime":
                aTime(p, args);
                break;
            case "areset":
                aReset(p);
                break;
            case "aitem", "amob":
                aItemMob(p, pl, command_name, args);
                break;
            default:
                p.sendMessage(Main.lang.colorBad + Lang.enterCommand);
        }
        return true;
    }

    private void aHelp(Player p) {
        MainHelp.handle_help(p);
    }

    private void aReset(Player p) {
        if (!p.isOp()) {
            p.sendMessage(Main.lang.colorBad + Lang.noPermission);
        }

        reset();
        p.sendMessage(Main.lang.colorGood + Lang.pluginReset);
    }

    private void aTime(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Main.lang.colorBad + Lang.enterCommand);
            return;
        }

        //Adjust Arguments

        String command_name = args[0].toLowerCase();
        args = Arrays.copyOfRange(args, 1, args.length);

        switch (command_name) {
            case "start":
                timeStart(p);
                break;
            case "pause":
                timePause(p);
                break;
            case "stop":
                timeStop(p);
                break;
            case "reset":
                timeReset(p);
                break;
            case "set":
                timeSet(p, args);
                break;
            case "placeholders":
                PlaceholderHelp.handle_timer(p);
                break;
            case "get":
                timeGet(p);
                break;
            case "active":
                timeActive(p);
                break;
            case "help":
                TimerHelp.handle_help(p);
                break;
            default:
                p.sendMessage(Main.lang.colorBad + Lang.enterCommand);
        }
    }

    private void aItemMob(Player p, player pl, String command_name, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Main.lang.colorBad + Lang.enterCommand);
            return;
        }

        boolean com_isMob = command_name.equalsIgnoreCase("amob");

        if (!com_isMob && !Main.data.item_toggle) {
            p.sendMessage(Lang.colorBad + Lang.itemDisabled);
            return;
        }
        if (com_isMob && !Main.data.mob_toggle) {
            p.sendMessage(Lang.colorBad + Lang.mobDisabled);
            return;
        }

        //Adjust Arguments

        command_name = args[0].toLowerCase();
        args = Arrays.copyOfRange(args, 1, args.length);

        //Sub Commands

        switch (command_name) {
            //Sends the item(s) in hand/hotbar/inventory
            case "send":
                if (!com_isMob) {
                    handle_send(p, args);
                }
                break;

            //Brings up the list of items menu
            case "list":
                handle_list(com_isMob, false, p, pl, args);
                break;

            //Brings up the list of self items menu
            case "self":
                handle_list(com_isMob, true, p, pl, args);
                break;

            //Sends the progress of the challenge
            case "prog":
                handle_prog(p, args, com_isMob);
                break;

            //Sends the score of a player
            case "player":
                handle_player(p, args, com_isMob, list_priority_public);
                break;

            //Checks the item that the player sends
            case "check":
                handle_check(p, args, com_isMob);
                break;

            //Sends the item settings of the plugin
            case "settings":
                Settings.handle_settings(p, com_isMob);
                break;

            //Explains the item commands of the plugin
            case "help":
                Help.handle_help(p, com_isMob);
                break;

            //ADMIN COMMANDS

            //Resets the item data
            case "reset":
                handle_reset(p, com_isMob);
                break;

            //Submits an item from the plugin
            case "submit":
                handle_submit(p, args, com_isMob, false);
                break;

            //Unsubmits an item from the plugin
            case "unsubmit":
                handle_submit(p, args, com_isMob, true);
                break;

            //Prints a help message for placeholders
            case "placeholders":
                PlaceholderHelp.handle_placeholder(p, com_isMob);
                break;

            //Default if no subcommand has been typed
            default:
                p.sendMessage(Main.lang.colorBad + Lang.enterCommand);
        }
    }
}
