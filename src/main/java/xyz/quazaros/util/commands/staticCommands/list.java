package xyz.quazaros.util.commands.staticCommands;

import org.bukkit.entity.Player;
import xyz.quazaros.main;
import xyz.quazaros.structures.items.itemList;
import xyz.quazaros.structures.player.player;
import xyz.quazaros.util.files.config.lang;

public class list {
    //Returns a string depending on the outcome of list_setup
    public static void handle_list(boolean mob, boolean self, Player p, player pl, String[] args) {
        boolean playerFound = list_setup(mob, self, p, pl, args);
        if (!playerFound) {
            p.sendMessage(main.getPlugin().lang.colorBad + main.getPlugin().lang.playerNotFound);
        }
    }

    //Handles what happens with lists
    private static boolean list_setup(boolean mob, boolean self, Player p, player pl, String[] args) {
        main Main = main.getPlugin();
        lang Lang = Main.lang;

        if (!Main.player_list.player_exists(p.getName())) {
            return false;
        }
        pl.invItt = 0;

        player targetPlayer = null;
        boolean personal = false;
        if (self) {
            personal = true;
            targetPlayer = pl;
        }
        else if (args.length >= 1) {
            personal = true;
            if (Main.player_list.player_exists(args[0])) {
                targetPlayer = Main.player_list.get_player_from_string(args[0]);
            } else {
                return false;
            }
        }

        String temp = "";
        itemList tempList;
        boolean is_public = true;
        if (personal && !mob) { //aself
            temp = targetPlayer.name + " " + Lang.itemPersonalSuffix;
            tempList = targetPlayer.item_list;
            is_public = false;
        } else if (!personal && !mob) { //alist
            temp = Lang.itemListMenu;
            tempList = Main.all_items;
        } else if (personal && mob) { //mself
            temp = targetPlayer.name + " " + Lang.mobPersonalSuffix;
            tempList = targetPlayer.mob_list;
            is_public = false;
        } else if (!personal && mob) { //mlist
            temp = Lang.mobListMenu;
            tempList = Main.all_mobs;
        } else {tempList = new itemList();}

        if (is_public) {
            if (!Main.data.general_global) {
                p.sendMessage(Lang.colorBad + Lang.commandDisabled);
                return true;
            }
        } else {
            if (targetPlayer.name.equalsIgnoreCase(p.getName()) && !Main.data.general_personal) {
                p.sendMessage(Lang.colorBad + Lang.commandDisabled);
                return true;
            } else if (!targetPlayer.name.equalsIgnoreCase(p.getName()) && !Main.data.general_others) {
                p.sendMessage(Lang.colorBad + Lang.commandDisabled);
                return true;
            }
        }

        pl.inv.is_sorted = false;
        pl.inv.set_inventory(tempList, temp);
        p.openInventory(pl.inv.inventory_list.get(0));
        return true;
    }
}
