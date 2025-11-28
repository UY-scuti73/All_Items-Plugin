package xyz.quazaros.util.events.staticEvents;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import xyz.quazaros.main;
import xyz.quazaros.structures.player.player;

import static xyz.quazaros.util.events.util.announceCollection.announce_collection;
import static xyz.quazaros.util.events.util.completed.checkCompleted;

public class mobDeath {
    //Handles When A Mob Dies
    public static void handle_mobDies(EntityDeathEvent e) {
        main Main = main.getPlugin();

        if (!Main.data.mob_toggle) {
            return;
        }

        LivingEntity mob = (LivingEntity) e.getEntity();
        if(mob.getKiller() instanceof Player) {
            Player p = (Player) mob.getKiller();
            player pl = Main.player_list.get_player_from_string(p.getName());
            String name = mob.getName();
            int temp = 0;

            for (int i=0; i<Main.all_mobs.items.size(); i++) {
                if (Main.all_mobs.items.get(i).item_display_name.equalsIgnoreCase(name)) {
                    temp = i;
                }
            }

            String message_main = "";
            String message_personal = "";

            if (!Main.all_mobs.items.get(temp).isFound) {
                if (Main.all_mobs.is_in_indexes(temp)) {
                    message_main = Main.lang.colorGood + name + " " + Main.lang.mobKilled;
                }
                Main.all_mobs.items.get(temp).submit(p.getName(), Main.all_mobs.date());
                checkCompleted(true, null);
            }
            if (!pl.mob_list.items.get(temp).isFound) {
                if (Main.all_mobs.is_in_indexes(temp)) {
                    message_personal = Main.lang.colorGood + name + " " + Main.lang.mobKilled;
                }
                pl.mob_list.items.get(temp).submit(pl.mob_list.date());
                checkCompleted(true, pl);
            }

            String message = Main.data.general_listPriority ? message_personal : message_main;
            if (!message.isEmpty()) {
                announce_collection(message, p);
            }
        }
    }
}
