package xyz.quazaros.util.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.quazaros.util.commands.helpCommands.help;
import xyz.quazaros.util.commands.helpCommands.mainHelp;
import xyz.quazaros.util.commands.helpCommands.settings;
import xyz.quazaros.util.files.config.config;
import xyz.quazaros.util.files.config.lang;
import xyz.quazaros.structures.player.player;
import xyz.quazaros.main;

import java.util.Arrays;

import static xyz.quazaros.util.commands.staticCommands.check.handle_check;
import static xyz.quazaros.util.commands.staticCommands.list.handle_list;
import static xyz.quazaros.util.commands.staticCommands.player.handle_player;
import static xyz.quazaros.util.commands.staticCommands.prog.handle_prog;
import static xyz.quazaros.util.commands.staticCommands.reset.handle_reset;
import static xyz.quazaros.util.commands.staticCommands.send.handle_send;
import static xyz.quazaros.util.commands.staticCommands.submit.handle_submit;

public class commands implements CommandExecutor {

    main Main;
    lang Lang;

    help Help;
    settings Settings;
    mainHelp MainHelp;

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
    }

    public void initialize() {
        Help.initialize();
        Settings.initialize();
        MainHelp.initialize();

        config Config = Main.data;
        if (Config.general_global && !Config.general_others) {list_priority_public = true;}
        else if (Config.general_others && !Config.general_global) {list_priority_public = false;}
        else {list_priority_public = !Config.general_listPriority;}
    }

    public boolean sendCommand(CommandSender sender, Command command, String label, String[] args) {

        //Initialize Player

        if (!(sender instanceof Player)) {return true;}
        Player p = (Player) sender;
        player pl = Main.player_list.get_player_from_string(p.getName());

        String command_name = command.getName();

        //Check For Help Command

        if (command_name.equalsIgnoreCase("ahelp")) {
            MainHelp.handle_help(p);
        }

        //Item Or Mob Checker

        if (!(command_name.equalsIgnoreCase("aitem") || command_name.equalsIgnoreCase("amob"))) {
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + Lang.enterCommand);
            return true;
        }

        boolean com_isMob = command_name.equalsIgnoreCase("amob");

        if (!com_isMob && !Main.data.item_toggle) {
            p.sendMessage(Lang.colorBad + Lang.itemDisabled);
            return true;
        }
        if (com_isMob && !Main.data.mob_toggle) {
            p.sendMessage(Lang.colorBad + Lang.mobDisabled);
            return true;
        }

        //Adjust Arguments

        command_name = args[0].toLowerCase();
        args = Arrays.copyOfRange(args, 1, args.length);

        //Commands

        //Sends the item in a players hand
        if (command_name.equalsIgnoreCase("send") && !com_isMob) {
            handle_send(p, args);
        }

        //Brings up the list of self items menu
        if (command_name.equalsIgnoreCase("self")) {
            handle_list(com_isMob, true, p, pl, args);
        }

        //Brings up the list of items menu
        if (command_name.equalsIgnoreCase("list")) {
            handle_list(com_isMob, false, p, pl, args);
        }

        //Sends the progress of the challenge
        if (command_name.equalsIgnoreCase("prog")) {
            handle_prog(p, args, com_isMob);
        }

        //Sends the score of a player
        if (command_name.equalsIgnoreCase("player")) {
            handle_player(p, args, com_isMob, list_priority_public);
        }

        //Checks the item that the player sends
        if (command_name.equalsIgnoreCase("check")) {
            handle_check(p, args, com_isMob);
        }

        //Explains the item commands of the plugin
        if (command_name.equalsIgnoreCase("help")) {
            Help.handle_help(p, com_isMob);
        }

        //Sends the item settings of the plugin
        if (command_name.equalsIgnoreCase("settings")) {
            Settings.handle_settings(p, com_isMob);
        }

        //ADMIN COMMANDS

        //Resets the item data
        if (command_name.equalsIgnoreCase("reset")) {
            handle_reset(p, com_isMob);
        }

        //Submits an item from the plugin
        if (command_name.equalsIgnoreCase("submit")) {
            handle_submit(p, args, com_isMob, false);
        }

        //Unsubmits an item from the plugin
        if (command_name.equalsIgnoreCase("unsubmit")) {
            handle_submit(p, args, com_isMob, true);
        }

        return true;
    }
}
