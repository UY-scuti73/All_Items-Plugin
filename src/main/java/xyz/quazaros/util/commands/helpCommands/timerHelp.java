package xyz.quazaros.util.commands.helpCommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.quazaros.main;

public class timerHelp {
    public String timer_help;
    public String timer_admin_help;

    public void initialize() {
        ChatColor CC1 = ChatColor.GOLD;
        ChatColor CC2 = ChatColor.DARK_AQUA;
        ChatColor CC3 = ChatColor.DARK_RED;
        String b = ChatColor.WHITE + "------------------------------------------------\n";
        String t = main.getPlugin().lang.colorHelpTitle + "Timer Help Menu:\n";

        String get = CC1 + "get: " + CC2 + "Gets the Current Time (Seconds)\n";
        String active = CC1 + "active: " + CC2 + "Returns Whether or Not The Timer is Active\n";
        String help = CC1 + "help: " + CC2 + "Displays This Help Message\n";

        String start = CC3 + "(ADMIN) " + CC1 + "start: " + CC2 + "Starts the Timer\n";
        String pause = CC3 + "(ADMIN) " + CC1 + "pause: " + CC2 + "Pauses the Timer\n";
        String stop = CC3 + "(ADMIN) " + CC1 + "stop: " + CC2 + "Stops the Timer\n";
        String reset = CC3 + "(ADMIN) " + CC1 + "reset: " + CC2 + "Resets the Timer\n";
        String set = CC3 + "(ADMIN) " + CC1 + "set: " + CC2 + "Sets the Timer To A Value (Seconds)\n";

        timer_help = b + t + b + get + b + active + b + help + b;
        timer_admin_help = timer_help + start + b + pause + b + stop + b + reset + b + set + b;
    }

    public void handle_help(Player p) {
        String temp = p.isOp() ? timer_admin_help : timer_help;
        p.sendMessage(temp);
    }
}
