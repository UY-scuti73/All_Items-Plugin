package xyz.quazaros.util.commands.helpCommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.quazaros.main;

import static xyz.quazaros.util.main.mainVariables.getVariables;

public class help {

    String help_string;
    String admin_help_string;
    String mob_help_string;
    String admin_mob_help_string;

    public void initialize() {
        ChatColor CC1 = ChatColor.GOLD;
        ChatColor CC2 = ChatColor.DARK_AQUA;
        ChatColor CC3 = ChatColor.DARK_RED;
        String b = ChatColor.WHITE + "------------------------------------------------\n";
        String ti = getVariables().lang.colorHelpTitle + "Item Help Menu:\n";
        String tm = getVariables().lang.colorHelpTitle + "Mob Help Menu:\n";

        String Alist1 = CC1 + "list: " + CC2 + "Lists the Items/Mobs You Have Found\n";
        String Alist2 = CC1 + "list <player_name>: " + CC2 + "Lists the Personal Items/Mobs a Player Has Found\n";
        String Alist3 = CC1 + "self: " + CC2 + "Lists the Personal Items/Mobs You Have Found\n\n";
        String Alist = Alist1 + Alist2 + Alist3;
        String Asend1 = CC1 + "send: " + CC2 + "Sends the Item in Your Hand\n";
        String Asend2 = CC1 + "send hotbar: " + CC2 + "Sends the Item in Your Hotbar\n";
        String Asend3 = CC1 + "send inventory: " + CC2 + "Sends the Item in Your Inventory\n\n";
        String Asend = Asend1 + Asend2 + Asend3;
        String Aprog1 = CC1 + "prog: " + CC2 + "Displays the Total Item/Mob Progress\n";
        String Aprog2 = CC1 + "prog <player_name>: " + CC2 + "Displays the Total Item/Mob Progress of a Players Personal List\n\n";
        String Aprog = Aprog1 + Aprog2;
        String Aplayer1 = CC1 + "player: " + CC2 + "Displays a List of All Players and Their Score\n";
        String Aplayer2 = CC1 + "player <player_name>: " + CC2 + "Displays the Score of a Player\n\n";
        String Aplayer = Aplayer1 + Aplayer2;
        String Acheck1 = CC1 + "check <item_name>: " + CC2 + "Displays Whether an Item/Mob Has Been Obtained or Not\n";
        String Acheck2 = CC1 + "check <item_name> <player_name>: " + CC2 + "Displays Whether an Item/Mob Has Been Obtained or Not in a Players Personal List\n\n";
        String Acheck = Acheck1 + Acheck2;
        String Asettings = CC1 + "settings " + CC2 + "Displays the Settings of the Plugin\n\n";
        String Ahelp = CC1 + "help: " + CC2 + "Displays This Help Message\n\n";
        String Areset = CC3 + "(ADMIN) " + CC1 + "reset: " + CC2 + "Resets the ItemData/MobData Upon a Server Reset\n\n";
        String Asubmit1 = CC3 + "(ADMIN) " + CC1 + "submit <item_name>: " + CC2 + "Submits the Item/Mob Listed as ADMIN\n";
        String Asubmit2 = CC3 + "(ADMIN) " + CC1 + "submit <item_name> <player_name>: " + CC2 + "Submits the Item/Mob Listed as the Player Listed\n";
        String Asubmit3 = CC3 + "(ADMIN) " + CC1 + "submit <player_name> <item_name>: " + CC2 + "Submits the Item/Mob Listed in the Specific Player's Personal List\n";
        String Asubmit4 = CC3 + "(ADMIN) " + CC1 + "submit personal <player_name> <item_name>: " + CC2 + "Same as Above\n\n";
        String Asubmit = Asubmit1 + Asubmit2 + Asubmit3 + Asubmit4;
        String Aunsubmit1 = CC3 + "(ADMIN) " + CC1 + "unsubmit <item_name>: " + CC2 + "Unsubmits the Item/Mob Listed\n";
        String Aunsubmit2 = CC3 + "(ADMIN) " + CC1 + "unsubmit <player_name> <item_name>: " + CC2 + "Unsubmits the Item/Mob Listed From the Specific Player's Personal List\n";
        String Aunsubmit = Aunsubmit1 + Aunsubmit2;
        String Aplaceholders = CC3 + "(ADMIN) " + CC1 + "placeholders: " + CC2 + "Placeholder Help Menu\n";

        String temp_string = Alist + b + Aprog + b + Aplayer + b + Acheck + b + Asettings + b + Ahelp + b;
        String temp_admin_string = Areset + b + Asubmit + b + Aunsubmit + b + Aplaceholders + b;

        help_string = b + ti + b + Asend + b + temp_string;
        admin_help_string = help_string + temp_admin_string;
        mob_help_string = b + tm + b + temp_string;
        admin_mob_help_string = mob_help_string + temp_admin_string;
    }

    //Handles help commands
    public void handle_help(Player p, boolean mob) {
        String helpMessage;
        if (p.isOp()) {
            helpMessage = !mob ? admin_help_string : admin_mob_help_string;
        } else {
            helpMessage = !mob ? help_string : mob_help_string;
        }
        p.sendMessage(helpMessage);
    }
}
