package xyz.quazaros.util.commands.helpCommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.quazaros.main;

import static xyz.quazaros.util.main.mainVariables.getVariables;

public class mainHelp {
    String main_help_string;

    public void initialize() {
        ChatColor CC1 = ChatColor.GOLD;
        ChatColor CC2 = ChatColor.DARK_AQUA;
        String b = ChatColor.WHITE + "------------------------------------------------\n";
        String d = ChatColor.LIGHT_PURPLE + "Join The Discord: " + ChatColor.AQUA +""+ ChatColor.UNDERLINE + "https://discord.gg/zHzFgWX8KW\n";

        String intro = getVariables().lang.colorHelpTitle + "All Items Help Menu:\n";

        String strA = CC1 + "/aitem: " + CC2 + "All Of The Commands For The Items Section of This Plugin\n";
        String strB = CC1 + "/amob: " + CC2 + "All Of The Commands For The Mobs Section of This Plugin\n";
        String strC = CC1 + "/atime: " + CC2 + "All Of The Commands For The Time Section of This Plugin\n";
        String strD = CC1 + "/areset" + CC2 + "Reset The Files For The Plugin\n";
        String strE = CC1 + "/ahelp: " + CC2 + "Shows This Message! For More Specific Help Use '/aitem help' or '/amob help'\n";

        main_help_string = b + intro + b + strA + strB + strC + strD + strE + b + d + b;
    }

    public void handle_help(Player p) {
        p.sendMessage(main_help_string);
    }
}
