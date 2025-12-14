package xyz.quazaros.util.commands.itemCommands;

import org.bukkit.entity.Player;
import xyz.quazaros.main;
import xyz.quazaros.structures.items.item;
import xyz.quazaros.structures.items.itemList;
import xyz.quazaros.structures.player.player;
import xyz.quazaros.util.files.config.lang;

import static xyz.quazaros.util.main.mainMethods.completed.checkCompleted;

public class submit {
    //Handles submit and unsubmit commands
    public static void handle_submit(Player p, String[] args, boolean mob, boolean unsub) {
        main Main = main.getPlugin();
        lang Lang = Main.lang;

        if (!p.isOp()) {
            p.sendMessage(Lang.colorBad + Lang.noPermission);
            return;
        }
        if (args.length == 0) {
            if (!mob) {p.sendMessage(Lang.colorBad + Lang.enterItem);}
            else {p.sendMessage(Lang.colorBad + Lang.enterMob);}
            return;
        }

        itemList tempList = null;
        player pl = null;
        String targetPlayer = "";
        String item = "";

        if (args.length >= 3 && args[0].equalsIgnoreCase("personal") && !unsub) {
            if (!Main.player_list.player_exists(args[1])) {
                p.sendMessage(Lang.colorBad + Lang.playerNotFound);
                return;
            }
            pl = Main.player_list.get_player_from_string(args[1]);
            if ( ( !mob && !pl.item_list.item_exists(args[2]) ) || ( mob && !pl.mob_list.item_exists(args[2]) ) ) {
                if (!mob) {p.sendMessage(Lang.colorBad + Lang.itemNotFound);}
                else {p.sendMessage(Lang.colorBad + Lang.mobNotFound);}
                return;
            }
            item = args[2];
            tempList = !mob ? pl.item_list : pl.mob_list;
        } else if (args.length >= 2) {
            if ( (!unsub && ( ( ( !mob && !Main.all_items.item_exists(args[0]) ) || ( mob && !Main.all_mobs.item_exists(args[0]) ) ) && !Main.player_list.player_exists(args[0]) ) ) || ( unsub && !Main.player_list.player_exists(args[0]) ) ) {
                if (!unsub) {
                    if (!mob) {p.sendMessage(Lang.colorBad + Lang.itemNotFound);}
                    else {p.sendMessage(Lang.colorBad + Lang.mobNotFound);}
                } else {
                    p.sendMessage(Lang.colorBad + Lang.playerNotFound);
                }
                return;
            } else {
                if (!unsub && (Main.all_items.item_exists(args[0]) || Main.all_mobs.item_exists(args[0]))) {
                    if (Main.player_list.player_exists(args[1])) {
                        if (!mob) {tempList = Main.all_items;}
                        else {tempList = Main.all_mobs;}
                        item = args[0];
                        targetPlayer = args[1];
                    } else {
                        p.sendMessage(Lang.colorBad + Lang.playerNotFound);
                        return;
                    }
                } else if (Main.player_list.player_exists(args[0])) {
                    pl = Main.player_list.get_player_from_string(args[0]);
                    tempList = !mob ? pl.item_list : pl.mob_list;
                    if (tempList.item_exists(args[1])) {
                        item = args[1];
                    } else {
                        if (!mob) {p.sendMessage(Lang.colorBad + Lang.itemNotFound);}
                        else {p.sendMessage(Lang.colorBad + Lang.mobNotFound);}
                        return;
                    }
                }
            }
        } else if (args.length >= 1) {
            if ( ( !mob && Main.all_items.item_exists(args[0]) ) || ( mob && Main.all_mobs.item_exists(args[0]) ) ) {
                item = args[0];
                tempList = !mob ? Main.all_items : Main.all_mobs;
            } else {
                if (!mob) {p.sendMessage(Lang.colorBad + Lang.itemNotFound);}
                else {p.sendMessage(Lang.colorBad + Lang.mobNotFound);}
                return;
            }
        }

        if (tempList == null) {return;}

        int temp = tempList.get_item_index(item);
        if (temp == -1) {
            if (!mob) {p.sendMessage(Lang.colorBad + Lang.itemNotFound);}
            else {p.sendMessage(Lang.colorBad + Lang.mobNotFound);}
            return;
        }

        item tempItem = tempList.items.get(temp);

        if (!unsub && tempItem.isFound) {
            p.sendMessage(Lang.colorBad + tempItem.item_display_name + " " + Lang.subAlreadyFound);
            return;
        } else if (unsub && !tempItem.isFound) {
            p.sendMessage(Lang.colorBad + tempItem.item_display_name + " " + Lang.subNotFound);
            return;
        }

        if (!unsub) {
            String targetText = targetPlayer.isEmpty() ? Lang.colorWar + Lang.admin : targetPlayer;
            tempItem.submit(targetText, tempList.date());
            p.sendMessage(Lang.colorGood + tempItem.item_display_name + " " + Lang.submit);
            checkCompleted(mob, null);
            checkCompleted(mob, pl);
        } else {
            tempItem.unsubmit();
            p.sendMessage(Lang.colorGood + tempItem.item_display_name + " " + Lang.unsubmit);
            tempList.completed = false;
        }
    }
}
