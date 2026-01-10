package xyz.quazaros.util.main.mainMethods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.quazaros.main;
import xyz.quazaros.util.main.mainVariables;

public class announceCollection {
    //Announced Collection To The Server
    public static void announce_collection(String s, Player p) {
        mainVariables Main = main.getPlugin().variables;

        if (!Main.data.general_announceSend || s.contains(Main.lang.colorBad.toString()) || s.contains(Main.lang.itemSubNotInList)) {
            p.sendMessage(s);
        } else {
            s = s + " " + Main.lang.subBy + " " + p.getName();
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(s);
            }
        }
    }
}
