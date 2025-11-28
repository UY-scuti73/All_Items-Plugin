package xyz.quazaros.util.events.staticEvents;

import org.bukkit.event.player.PlayerPickupItemEvent;
import xyz.quazaros.main;

import static xyz.quazaros.util.events.util.itemSubmission.item_submission;

public class itemPickup {
    //Handles When An Item Is Picked Up
    public static void handle_itemPickup(PlayerPickupItemEvent e) {
        if (!main.getPlugin().data.item_toggle) {return;}

        if (main.getPlugin().data.item_autoCollect) {
            item_submission(e.getItem().getItemStack(), e.getPlayer(), false);
        }
    }
}
