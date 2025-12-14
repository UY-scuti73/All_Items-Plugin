package xyz.quazaros.util.main.mainMethods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.quazaros.main;
import xyz.quazaros.structures.items.itemList;
import xyz.quazaros.structures.player.player;

public class completed {
    //Checks If A List Has Been Completed
    public static void checkCompleted(boolean is_mob, player pl) {
        main Main = main.getPlugin();

        boolean is_personal;
        String command;
        String message;
        itemList temp;

        if (pl == null) {
            is_personal = false;
            if (!is_mob) {
                command = Main.data.general_mainItemCompletion_command;
                message = Main.lang.colorGood + Main.lang.allItems;
                temp = Main.all_items;
            } else {
                command = Main.data.general_mainMobCompletion_command;
                message = Main.lang.colorGood + Main.lang.allMobs;
                temp = Main.all_mobs;
            }
        } else {
            is_personal = true;
            if (!is_mob) {
                command = Main.data.general_personalItemCompletion_command;
                message = Main.lang.colorGood + pl.name + " " + Main.lang.completeItemSuffix;
                temp = pl.item_list;
            } else {
                command = Main.data.general_personalMobCompletion_command;
                message = Main.lang.colorGood + pl.name + " " + Main.lang.completeMobSuffix;
                temp = pl.mob_list;
            }
        }

        if (temp.complete()) {
            System.out.println(command);
            if (is_personal) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), modifyCommand(command, pl.name));}
            else {Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);}

            if (!Main.data.general_personalCompletion && is_personal) {return;}
            if (!Main.data.general_mainCompletion && !is_personal) {return;}
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle(Main.lang.colorHigh + Main.lang.congrats, message);
            }
        }
    }

    //Modifies The Command
    private static String modifyCommand(String command, String p) {
        if (command.contains("<player>")) {
            command = command.replace("<player>", p);
        }
        return command;
    }
}
