package xyz.quazaros.util.commands.itemCommands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import xyz.quazaros.main;

import java.util.ArrayList;

import static xyz.quazaros.util.main.mainMethods.itemSubmission.item_submission;

public class send {
    public static void handle_send(Player p, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("inventory")) {
            inv_check(p.getInventory(), p, p.getInventory().getSize());
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("hotbar")) {
            inv_check(p.getInventory(), p, 9);
        } else {
            item_submission(p.getInventory().getItemInMainHand(), p, true);
        }
    }

    //Checks the inventory for the /asendhot and /asendinv commands
    private static void inv_check(PlayerInventory player_items, Player p, int size) {
        ArrayList<ItemStack> inventory_list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (player_items.getItem(i) == null) {
                inventory_list.add(null);
            } else {
                inventory_list.add(player_items.getItem(i));
            }
        }

        int temp = 0;
        for (int i = 0; i < size; i++) {
            if (inventory_list.get(i) != null) {
                boolean sub = item_submission(inventory_list.get(i), p, false);
                if (sub) {
                    temp++;
                }
            }
        }

        if (temp == 0) {
            p.sendMessage(main.getPlugin().lang.colorBad + main.getPlugin().lang.youHaveNoItems);
        }
    }
}
