package xyz.quazaros.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import xyz.quazaros.inventory.inventory;
import xyz.quazaros.items.item;
import xyz.quazaros.main;
import xyz.quazaros.player.player;

public class events implements Listener {
    @EventHandler
    public void guiClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        player pl = main.getPlugin().PlayerList.getPlayer(p.getName());

        inventory curInv = main.getPlugin().Inventory;

        if (curInv.inventory_list.contains(e.getInventory())) {

            e.setCancelled(true);

            switch (e.getSlot()) {
                case 45:
                    if (!e.getInventory().equals(curInv.inventory_list.get(0))) {
                        pl.invItt -= 1;
                    }
                    else{
                        pl.invItt = curInv.size - 1;
                    }
                    p.openInventory(curInv.inventory_list.get(pl.invItt));
                    break;
                case 53:
                    if (!e.getInventory().equals(curInv.inventory_list.get(curInv.size-1))) {
                        pl.invItt += 1;
                    }
                    else{
                        pl.invItt = 0;
                    }
                    p.openInventory(curInv.inventory_list.get(pl.invItt));
                    break;
            }
        }
    }

    @EventHandler
    public void PlayerPickupItemEvent(PlayerPickupItemEvent e) {
        Player p = (Player) e.getPlayer();

        item tempItem = main.getPlugin().ItemList.submitItem(e.getItem().getItemStack());
        if (tempItem == null) {return;}

        p.sendMessage(ChatColor.GREEN + tempItem.item_display_name + " Has Been Submitted");
    }
}
