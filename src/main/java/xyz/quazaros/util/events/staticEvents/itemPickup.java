package xyz.quazaros.util.events.staticEvents;

import org.bukkit.event.player.PlayerPickupItemEvent;
import xyz.quazaros.main;

import static xyz.quazaros.util.main.mainMethods.itemSubmission.item_submission;
import static xyz.quazaros.util.main.mainVariables.getVariables;

public class itemPickup {
    //Handles When An Item Is Picked Up
    public static void handle_itemPickup(PlayerPickupItemEvent e) {
        if (!getVariables().data.item_toggle) {return;}

        if (getVariables().data.item_autoCollect) {
            item_submission(e.getItem().getItemStack(), e.getPlayer(), false);
        }
    }
}
