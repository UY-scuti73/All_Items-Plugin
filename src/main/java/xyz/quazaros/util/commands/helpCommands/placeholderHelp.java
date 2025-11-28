package xyz.quazaros.util.commands.helpCommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.quazaros.main;

public class placeholderHelp {

    String item_placeholders;
    String mob_placeholders;

    public void initialize() {
        ChatColor CC1 = ChatColor.GOLD;
        ChatColor CC2 = ChatColor.DARK_AQUA;
        ChatColor CC4 = ChatColor.DARK_PURPLE;
        String b = ChatColor.WHITE + "------------------------------------------------\n";
        String ti = main.getPlugin().lang.colorHelpTitle + "Item Placeholder Help:\n";
        String tm = main.getPlugin().lang.colorHelpTitle + "Mob Placeholder Help:\n";

        String topMessage = CC2 + "The '<>' are NOT needed for PLAYERS, if your name is 'B0WSER', input 'B0WSER' in place of <player> not '<B0WSER>'\n \nThe '{}' ARE needed for ITEMS/MOBS\n";

        String Prog = CC4 + "Progress\n";
        String Aprog1 = CC1 + "%allitems_itemprog%" + CC2 + " - Fetches the global item progress\n";
        String Aprog2 = CC1 + "%allitems_itemprogpersonal_<player>%" + CC2 + " - Fetches the progress of a players personal item list\n";
        String Aprog3 = CC1 + "%allitems_itemprogself%" + CC2 + " - Fetches the item progress of the player running the command\n";
        String Mprog1 = CC1 + "%allitems_mobprog%" + CC2 + " - Fetches the global mob progress";
        String Mprog2 = CC1 + "%allitems_mobprogpersonal_<player>%" + CC2 + " - Fetches the progress of a players personal mob list\n";
        String Mprog3 = CC1 + "%allitems_mobprogself%" + CC2 + " - Fetches the mob progress of the player running the command\n";
        String Aprog = Prog + Aprog1 + Aprog2 + Aprog3;
        String Mprog = Prog + Mprog1 + Mprog2 + Mprog3;

        String Score = CC4 + "Score\n";
        String Ascore1 = CC1 + "%allitems_itemscore_<player>%" + CC2 + " - Fetches the score of a player in the global item list\n";
        String Ascore2 = CC1 + "%allitems_itemscoreself%" + CC2 + " - Fetches the item score of the player running the command\n";
        String Mscore1 = CC1 + "%allitems_mobscore_<player>%" + CC2 + " - Fetches the score of a player in the global mob list\n";
        String Mscore2 = CC1 + "%allitems_mobscoreself%" + CC2 + " - Fetches the mob score of the player running the command\n";
        String Ascore = Score + Ascore1 + Ascore2;
        String Mscore = Score + Mscore1 + Mscore2;

        String Check = CC4 + "Check\n";
        String Acheck1 = CC1 + "%allitems_itemcheck_{item}%" + CC2 + " - Fetches whether or not a specific item has been found on the global item list\n";
        String Acheck2 = CC1 + "%allitems_itemcheckpersonal_{item}_<player>%" + CC2 + " - Fetches whether or not a specific item has been found on the personal item list\n";
        String Acheck3 = CC1 + "%allitems_itemcheckself_{item}%" + CC2 + " - Fetches whether or not a specific item has been found on the player running the command's item list\n";
        String Mcheck1 = CC1 + "%allitems_mobcheck_{mob}%" + CC2 + " - Fetches whether or not a specific mob has been killed on the global mob list\n";
        String Mcheck2 = CC1 + "%allitems_mobcheckpersonal_{mob}_<player>%" + CC2 + " - Fetches whether or not a specific mob has been killed on the personal mob list\n";
        String Mcheck3 = CC1 + "%allitems_mobcheckself_{mob}%" + CC2 + " - Fetches whether or not a specific mob has been killed on the player running the command's mob list\n";
        String Acheck = Check + Acheck1 + Acheck2 +  Acheck3;
        String Mcheck = Check + Mcheck1 + Mcheck2 +  Mcheck3;

        String Completed = CC4 + "Completed\n";
        String Acompleted1 = CC1 + "%allitems_itemcompleted%" + CC2 + " - Fetches whether or not the main item list has been completed\n";
        String Acompleted2 = CC1 + "%allitems_itemcompletedpersonal_<player>%" + CC2 + " - Fetches whether or not the personal item list has been completed\n";
        String Acompleted3 = CC1 + "%allitems_itemcompletedself%" + CC2 + " - Fetches whether or not the player running the command's item list has been completed\n";
        String Mcompleted1 = CC1 + "%allitems_mobcompleted%" + CC2 + " - Fetches whether or not the main mob list has been completed\n";
        String Mcompleted2 = CC1 + "%allitems_mobcompletedpersonal_<player>%" + CC2 + " - Fetches whether or not the personal mob list has been completed\n";
        String Mcompleted3 = CC1 + "%allitems_mobcompletedself%" + CC2 + " - Fetches whether or not the player running the command's mob list has been completed\n";
        String Acompleted = Completed + Acompleted1 + Acompleted2 +  Acompleted3;
        String Mcompleted = Completed + Mcompleted1 + Mcompleted2 +  Mcompleted3;

        String Leaderboard = CC4 + "Leaderboard\n";
        String Aleaderboard1 = CC1 + "%allitems_itemleaderboardplayer_<place>%" + CC2 + " - Fetches the player at the specified place for the main item leaderboard\n";
        String Aleaderboard2 = CC1 + "%allitems_itemleaderboardscore_<place>%" + CC2 + " - Fetches the score of the player at the specified place for the main item leaderboard\n";
        String Aleaderboard3 = CC1 + "%allitems_itemleaderboardplayerpersonal_<place>%" + CC2 + " - Fetches the player at the specified place for the personal item leaderboard\n";
        String Aleaderboard4 = CC1 + "%allitems_itemleaderboardscorepersonal_<place>%" + CC2 + " - Fetches the score of the player at the specified place for the personal item leaderboard\n";
        String Mleaderboard1 = CC1 + "%allitems_mobleaderboardplayer_<place>%" + CC2 + " - Fetches the player at the specified place for the main mob leaderboard\n";
        String Mleaderboard2 = CC1 + "%allitems_mobleaderboardscore_<place>%" + CC2 + " - Fetches the score of the player at the specified place for the main mob leaderboard\n";
        String Mleaderboard3 = CC1 + "%allitems_mobleaderboardplayerpersonal_<place>%" + CC2 + " - Fetches the player at the specified place for the personal mob leaderboard\n";
        String Mleaderboard4 = CC1 + "%allitems_mobleaderboardscorepersonal_<place>%" + CC2 + " - Fetches the score of the player at the specified place for the personal mob leaderboard\n";
        String Aleaderboard = Leaderboard + Aleaderboard1 + Aleaderboard2 + Aleaderboard3 + Aleaderboard4;
        String Mleaderboard = Leaderboard + Mleaderboard1 + Mleaderboard2 + Mleaderboard3 + Mleaderboard4;


        item_placeholders = b + ti + b + topMessage + b + Aprog + b + Ascore + b + Acheck + b + Acompleted + b + Aleaderboard + b;
        mob_placeholders = b + tm + b + Mprog + b + Mscore + b + Mcheck + b + Mcompleted + b + Mleaderboard + b;
    }

    //Handles placeholder help commands
    public void handle_placeholder(Player p, boolean mob) {
        if (!p.isOp()) {
            p.sendMessage(main.getPlugin().lang.colorBad + main.getPlugin().lang.noPermission);
            return;
        }

        String message = mob ? mob_placeholders : item_placeholders;
        p.sendMessage(message);
    }
}
