package xyz.quazaros.util.commands.itemCommands;

import org.bukkit.entity.Player;
import xyz.quazaros.main;
import xyz.quazaros.util.files.config.lang;
import xyz.quazaros.util.main.mainVariables;

public class reset {
    //Handles reset commands
    public static void handle_reset(Player p, boolean mob) {
        mainVariables Main = main.getPlugin().variables;
        lang Lang = Main.lang;

        if(p.isOp()) {
            if (!mob) {
                if (Main.file.reset) {
                    p.sendMessage(Lang.colorGood + Lang.resetCancel);
                } else {
                    p.sendMessage(Lang.colorWar + Lang.areset);
                }
                Main.file.reset = !Main.file.reset;
            } else {
                if (Main.file.mob_reset) {
                    p.sendMessage(Lang.colorGood + Lang.resetCancel);
                } else {
                    p.sendMessage(Lang.colorWar + Lang.mreset);
                }
                Main.file.mob_reset = !Main.file.mob_reset;
            }
        } else {p.sendMessage(Lang.colorBad + Lang.noPermission);}
    }
}
