package xyz.quazaros.util.commands.itemCommands;

import org.bukkit.entity.Player;
import xyz.quazaros.main;
import xyz.quazaros.util.files.config.lang;
import xyz.quazaros.util.main.mainVariables;

import static xyz.quazaros.util.main.mainVariables.getVariables;

public class player {
    //Handles player commands
    public static void handle_player(Player p, String[] args, boolean mob, boolean list_priority_public) {
        mainVariables Main = getVariables();
        lang Lang = Main.lang;

        if (!Main.data.general_player) {
            p.sendMessage(Lang.colorBad + Lang.commandDisabled);
            return;
        }

        if (args.length == 0) {
            if (!Main.data.general_global && !Main.data.general_others) {
                p.sendMessage(Lang.colorBad + Lang.commandDisabled);
                return;
            }

            xyz.quazaros.structures.player.player pl = Main.player_list.get_player_from_string(p.getName());
            pl.invItt = 0;

            pl.inv.set_players(mob, list_priority_public);
            p.openInventory(pl.inv.inventory_list.get(pl.invItt));
            return;
        }

        if (!Main.data.general_global) {
            p.sendMessage(Lang.colorBad + Lang.commandDisabled);
            return;
        }

        xyz.quazaros.structures.player.player tempPlayer = Main.player_list.get_player_from_string(args[0]);
        if (tempPlayer == null) {
            p.sendMessage(Lang.colorBad + Lang.playerNotFound);
        } else {
            if (!mob) {p.sendMessage(Lang.colorDom + tempPlayer.name + ": " + Lang.colorSec + tempPlayer.score);}
            else {p.sendMessage(Lang.colorDom + tempPlayer.name + ": " + Lang.colorSec + tempPlayer.mobScore);}
        }
    }
}
