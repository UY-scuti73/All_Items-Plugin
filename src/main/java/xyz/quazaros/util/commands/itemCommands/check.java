package xyz.quazaros.util.commands.itemCommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.quazaros.main;
import xyz.quazaros.structures.items.item;
import xyz.quazaros.structures.items.itemList;
import xyz.quazaros.util.files.config.lang;
import xyz.quazaros.util.main.mainVariables;

import static xyz.quazaros.extra.sprites.conversions.toNBT;
import static xyz.quazaros.util.main.mainVariables.getVariables;

public class check {
    //Handles check commands
    public static void handle_check(Player p, String[] args, boolean mob) {
        mainVariables Main = getVariables();
        lang Lang = Main.lang;

        if (!Main.data.general_check) {
            p.sendMessage(Lang.colorBad + Lang.commandDisabled);
            return;
        }

        String item = "";
        itemList tempList = null;
        boolean is_public = args.length == 1;

        if (args.length == 0) {
            if (!mob) {p.sendMessage(Lang.colorBad + Lang.enterItem);}
            else {p.sendMessage(Lang.colorBad + Lang.enterMob);}
            return;
        } else if (args.length >= 2) {
            if ( ( !mob && !Main.all_items.item_exists(args[0]) ) || ( mob && !Main.all_mobs.item_exists(args[0]) ) ) {
                if (!mob) {p.sendMessage(Lang.colorBad + Lang.itemNotFound);}
                else {p.sendMessage(Lang.colorBad + Lang.mobNotFound);}
                return;
            } else {
                if (!Main.player_list.player_exists(args[1])) {
                    p.sendMessage(Lang.colorBad + Lang.playerNotFound);
                    return;
                } else {
                    item = args[0];
                    tempList = !mob ? Main.player_list.get_player_from_string(args[1]).item_list : Main.player_list.get_player_from_string(args[1]).mob_list;
                }
            }
        } else if (args.length >= 1) {
            if ( ( !mob && !Main.all_items.item_exists(args[0]) ) || ( mob && !Main.all_mobs.item_exists(args[0]) ) ) {
                if (!mob) {p.sendMessage(Lang.colorBad + Lang.itemNotFound);}
                else {p.sendMessage(Lang.colorBad + Lang.mobNotFound);}
                return;
            } else {
                item = args[0];
                tempList = !mob ? Main.all_items : Main.all_mobs;
            }
        }

        if (tempList == null || item.isEmpty()) {return;}

        if (is_public) {
            if (!Main.data.general_global) {
                p.sendMessage(Lang.colorBad + Lang.commandDisabled);
                return;
            }
        } else {
            if (args[1].equalsIgnoreCase(p.getName()) && !Main.data.general_personal) {
                p.sendMessage(Lang.colorBad + Lang.commandDisabled);
                return;
            } else if (!args[1].equalsIgnoreCase(p.getName()) && !Main.data.general_others) {
                p.sendMessage(Lang.colorBad + Lang.commandDisabled);
                return;
            }
        }

        int temp = tempList.get_item_index(item);
        if (temp == -1) {
            if (!mob) {p.sendMessage(Lang.colorBad + Lang.itemNotFound);}
            else {p.sendMessage(Lang.colorBad + Lang.mobNotFound);}
            return;
        }

        item tempItem = tempList.items.get(temp);

        if (tempItem.isFound) {
            if (is_public) {sendMessage(p, tempItem, Lang.hasBeenFoundBy + " " + tempItem.item_founder, true);}
            else {sendMessage(p, tempItem, Lang.hasBeenFound, true);}
        } else {
            sendMessage(p, tempItem, Lang.hasNotBeenFound, false);
        }
    }

    private static void sendMessage(Player p, item tempItem, String suffix, boolean isFound) {
        String tempColor = isFound ? getVariables().lang.colorGoodStr : getVariables().lang.colorBadStr;
        String text = toNBT(tempColor, tempItem.item_display_name, tempItem.item_sprite, suffix);
        String command = "tellraw " + p.getName() + " " + text;
        System.out.println(command);
        Bukkit.dispatchCommand(p, command);
    }
}
