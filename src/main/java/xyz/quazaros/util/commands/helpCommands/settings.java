package xyz.quazaros.util.commands.helpCommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.quazaros.main;
import xyz.quazaros.util.files.config.config;
import xyz.quazaros.util.files.config.lang;
import xyz.quazaros.util.main.mainVariables;

public class settings {

    String item_settings;
    String mob_settings;

    public void initialize() {
        config Data = main.getPlugin().variables.data;
        lang Lang = main.getPlugin().variables.lang;

        String str_item, str_mob, str_sub, str_auto, str_global, str_personal, str_others, str_priority, str_progress, str_check, str_player, str_settings;

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

        String p1 = Lang.colorDom +""+ ChatColor.BOLD + "Items Toggled: " + Lang.colorSec +""+ ChatColor.BOLD + str_item;
        String p2 = Lang.colorDom +""+ ChatColor.BOLD + "Mobs Toggled: " + Lang.colorSec +""+ ChatColor.BOLD + str_mob;
        String p3 = Lang.colorDom + "Item File: " + Lang.colorSec + Data.item_file;
        String p4 = Lang.colorDom + "Mob File: " + Lang.colorSec + Data.mob_file;
        String p5 = Lang.colorDom + "Subtraction: " + Lang.colorSec + str_sub;
        String p6 = Lang.colorDom + "Auto Collection: " + Lang.colorSec + str_auto;
        String p7 = Lang.colorDom + "Global List: " + Lang.colorSec + str_global;
        String p8 = Lang.colorDom + "Personal Lists: " + Lang.colorSec + str_personal;
        String p9 = Lang.colorDom + "Other Lists: " + Lang.colorSec + str_others;
        String p10 = Lang.colorDom + "List Priority: " + Lang.colorSec + str_priority;
        String p11 = Lang.colorDom + "Progress Command: " + Lang.colorSec + str_progress;
        String p12 = Lang.colorDom + "Check Command: " + Lang.colorSec + str_check;
        String p13 = Lang.colorDom + "Player Command: " + Lang.colorSec + str_player;
        String p14 = Lang.colorDom + "Settings Command: " + Lang.colorSec + str_settings;

        String general = p7 + "\n" + p8 + "\n" + p9 + "\n" + p10 + "\n" + p11 + "\n" + p12 + "\n" + p13 + "\n" + p14;

        String b = ChatColor.WHITE + "------------------------------------------------\n";
        String itemHead = b + Lang.colorHelpTitle + Lang.itemSettings + ": \n" + b;
        String mobHead = b + Lang.colorHelpTitle + Lang.mobSettings + ": \n" + b;

        item_settings = itemHead + p1 + "\n" + p2 + "\n" + p3 + "\n" + p5 + "\n" + p6 + "\n" + general + "\n" + b;
        mob_settings = mobHead + p1 + "\n" + p2 + "\n" + p4 + "\n" + general + "\n" + b;
    }

    //Handles setting commands
    public void handle_settings(Player p, boolean mob) {
        mainVariables Main = main.getPlugin().variables;
        lang Lang = Main.lang;

        if (!Main.data.general_settings) {
            p.sendMessage(Lang.colorBad + Lang.commandDisabled);
            return;
        }

        if (!mob) {
            p.sendMessage(item_settings);
        } else {
            p.sendMessage(mob_settings);
        }
    }
}
