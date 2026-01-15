package xyz.quazaros.util.main.mainMethods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.quazaros.main;
import xyz.quazaros.util.main.mainVariables;

import static xyz.quazaros.util.main.mainVariables.getVariables;

public class announceCollection {
    //Announced Collection To The Server
    public static void announce_collection(String s, Player p) {
        mainVariables Main = getVariables();

        if (!Main.data.general_announceSend || s.contains(Main.lang.colorBad.toString()) || s.contains(Main.lang.itemSubNotInList)) {
            sendMessage(s, p);
        } else {
            s = s + " " + Main.lang.subBy + " " + p.getName();
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendMessage(s, player);
            }
        }
    }

    private static void sendMessage(String message, Player p) {
        String command = "tellraw " + p.getName() + " " + message;
        Bukkit.dispatchCommand(p, command);
    }
}
