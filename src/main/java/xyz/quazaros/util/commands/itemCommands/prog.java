package xyz.quazaros.util.commands.itemCommands;

import org.bukkit.entity.Player;
import xyz.quazaros.main;
import xyz.quazaros.structures.items.itemList;
import xyz.quazaros.util.files.config.lang;
import xyz.quazaros.util.main.mainVariables;

import static xyz.quazaros.util.main.mainVariables.getVariables;

public class prog {
    //Handles progress commands
    public static void handle_prog(Player p, String[] args, boolean mob) {
        mainVariables Main = getVariables();
        lang Lang = Main.lang;

        if (!Main.data.general_progress) {
            p.sendMessage(Lang.colorBad + Lang.commandDisabled);
            return;
        }

        boolean is_public = true;

        itemList tempList = null;
        if (args.length >= 1) {
            if (!Main.player_list.player_exists(args[0])) {
                p.sendMessage(Lang.colorBad + Lang.playerNotFound);
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
                p.sendMessage(Lang.colorBad + Lang.commandDisabled);
                return;
            }
        } else {
            if (args[0].equalsIgnoreCase(p.getName()) && !Main.data.general_personal) {
                p.sendMessage(Lang.colorBad + Lang.commandDisabled);
                return;
            } else if (!args[0].equalsIgnoreCase(p.getName()) && !Main.data.general_others) {
                p.sendMessage(Lang.colorBad + Lang.commandDisabled);
                return;
            }
        }

        p.sendMessage(Lang.colorDom + Lang.progress + ": " + Lang.colorSec + tempList.progPer());
    }
}
