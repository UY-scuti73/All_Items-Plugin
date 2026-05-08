package xyz.quazaros.util.main.mainMethods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import xyz.quazaros.main;
import xyz.quazaros.structures.player.player;
import xyz.quazaros.util.main.mainVariables;

import static xyz.quazaros.util.main.mainMethods.announceCollection.announce_collection;
import static xyz.quazaros.util.main.mainMethods.completed.checkCompleted;

public class itemSubmission {
    public static boolean item_submission(ItemStack it, Player p, boolean is_asend) {
        mainVariables Main = main.getPlugin().variables;

        boolean sub = false;
        player pl = Main.player_list.get_player_from_string(p.getName());

        String message_main = Main.all_items.check_items(it, p.getDisplayName(), !is_asend);
        String message_personal = pl.item_list.check_items(it, p.getDisplayName(), !is_asend);
        String message = Main.data.general_listPriority ? message_personal : message_main;

        if (message.contains(Main.lang.colorGood.toString())) {
            if ( (message.endsWith(Main.lang.itemSubmitted) && !message.contains(Main.lang.itemSubNotInList) ) || is_asend) {
                sub = true;
            }
        }

        checkCompleted(false, pl);
        checkCompleted(false, null);

        if (sub || is_asend) {
            announce_collection(message, p);
        }

        if (sub && Main.data.item_subtraction) {
            Bukkit.getScheduler().runTaskLater(main.getPlugin(), () -> {
                ItemStack found = findMatchingItem(p.getInventory(), it);
                if (found != null) {
                    if (found.getAmount() <= 1) {
                        p.getInventory().remove(found);
                    } else {
                        found.setAmount(found.getAmount() - 1);
                    }
                }
            }, 1L);
        }

        return sub;
    }

    private static ItemStack findMatchingItem(PlayerInventory inv, ItemStack toMatch) {
        for (ItemStack item : inv.getContents()) {
            if (item != null && item.isSimilar(toMatch)) {
                return item;
            }
        }
        return null;
    }
}
