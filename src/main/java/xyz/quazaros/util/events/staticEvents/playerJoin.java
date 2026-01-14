package xyz.quazaros.util.events.staticEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.quazaros.main;
import xyz.quazaros.structures.player.player;
import xyz.quazaros.util.main.mainVariables;

import static xyz.quazaros.util.main.mainVariables.getVariables;

public class playerJoin {
    //Handles When A Player Joins
    public static void handle_playerJoin(PlayerJoinEvent e) {
        mainVariables Main = getVariables();
        Player p = e.getPlayer();

        int temp=0;
        for(int i=0; i<Main.player_list.players.size(); i++) {
            if (Main.player_list.players.get(i).name.equals(p.getName())) {
                temp++;
            }
        }
        if (temp==0) {
            Main.player_list.players.add(new player(p.getName()));
        }
    }

    //Sets the permission of a player
    private static void set_permissions(Player player) {
        // Deprecated //

        main Main = main.getPlugin();

        player.addAttachment(Main, "all_items.items", false);
        player.addAttachment(Main, "all_items.mobs", false);

        if (player.isOp()) {
            player.addAttachment(Main, "all_items.items", true);
            player.addAttachment(Main, "all_items.mobs", true);
            return;
        }

        if (Main.variables.data.item_toggle) {
            player.addAttachment(Main, "all_items.items", true);
        }
        if (Main.variables.data.mob_toggle) {
            player.addAttachment(Main, "all_items.mobs", true);
        }
    }

    //Sets the permissions of every player
    public static void set_all_permissions() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            set_permissions(p);
        }
    }
}
