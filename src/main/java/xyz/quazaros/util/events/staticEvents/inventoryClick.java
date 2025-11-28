package xyz.quazaros.util.events.staticEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.quazaros.main;
import xyz.quazaros.structures.player.player;

public class inventoryClick {
    //Handles When The Inventory Is Clicked
    public static void handle_inventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        player pl = main.getPlugin().player_list.get_player_from_string(p.getName());

        if(pl.inv.inventory_list.contains(e.getInventory()) || pl.inv.sorted_list.contains(e.getInventory())) {

            //Prevents the player from moving any items in the menu
            e.setCancelled(true);

            switch (e.getSlot()) {
                //Goes to the last page if the back arrow is pressed, unless it is on the first page
                case 45: {
                    if (!e.getInventory().equals(pl.inv.inventory_list.get(0)) && !e.getInventory().equals(pl.inv.sorted_list.get(0))) {
                        pl.invItt -= 1;
                    }
                    else{
                        pl.invItt = pl.inv.size - 1;
                    }

                    if (!pl.inv.is_sorted) {
                        p.openInventory(pl.inv.inventory_list.get(pl.invItt));
                    } else {
                        p.openInventory(pl.inv.sorted_list.get(pl.invItt));
                    }

                    break;
                }
                //Goes to the next page if the forward arrow is pressed, unless it is on the last page
                case 53: {
                    if (!e.getInventory().equals(pl.inv.inventory_list.get(pl.inv.size-1)) && !e.getInventory().equals(pl.inv.sorted_list.get(pl.inv.size-1))) {
                        pl.invItt += 1;
                    }
                    else{
                        pl.invItt = 0;
                    }

                    if (!pl.inv.is_sorted) {
                        p.openInventory(pl.inv.inventory_list.get(pl.invItt));
                    } else {
                        p.openInventory(pl.inv.sorted_list.get(pl.invItt));
                    }

                    break;
                }
                //Handles what happens when the sort button is presses
                case 47: {
                    if (!pl.inv.is_playerList) {
                        pl.invItt = 0;
                        pl.inv.is_sorted = !pl.inv.is_sorted;
                        if (pl.inv.is_sorted) {
                            p.openInventory(pl.inv.sorted_list.get(0));
                        } else {
                            p.openInventory(pl.inv.inventory_list.get(0));
                        }
                    }

                    break;
                }
                //Handles the flip for the player list
                case 49: {
                    if (pl.inv.is_playerList && pl.inv.add_flip) {
                        pl.invItt = 0;
                        if (pl.inv.is_public) {
                            pl.inv.set_players(pl.inv.is_mob, false);
                        } else {
                            pl.inv.set_players(pl.inv.is_mob, true);
                        }
                        p.openInventory(pl.inv.inventory_list.get(pl.invItt));
                    }
                }
            }
        }
    }
}
