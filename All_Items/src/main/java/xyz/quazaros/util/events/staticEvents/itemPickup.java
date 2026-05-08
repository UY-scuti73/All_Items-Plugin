package xyz.quazaros.util.events.staticEvents;

import org.bukkit.event.player.PlayerPickupItemEvent;
import xyz.quazaros.main;

import static xyz.quazaros.util.main.mainMethods.itemSubmission.item_submission;

public class itemPickup {
    //Handles When An Item Is Picked Up
    public static void handle_itemPickup(PlayerPickupItemEvent e) {
        if (!main.getPlugin().variables.data.item_toggle) {return;}

        if (main.getPlugin().variables.data.item_autoCollect) {
            item_submission(e.getItem().getItemStack(), e.getPlayer(), false);
        }
    }
}
