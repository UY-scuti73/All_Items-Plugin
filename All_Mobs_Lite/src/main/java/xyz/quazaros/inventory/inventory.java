package xyz.quazaros.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.quazaros.items.itemList;
import xyz.quazaros.main;

import java.util.ArrayList;

public class inventory {
    public int size;

    public ArrayList<Inventory> inventory_list = new ArrayList<>();

    ItemStack forward;
    ItemStack back;
    ItemStack progress;

    public void set_inventory() {
        itemList item_list = main.getPlugin().ItemList;

        size = ( (item_list.size()-1) / 45 ) + 1;

        menuButtons();

        for (int i = 0; i<size ; i++) {
            inventory_list.add(Bukkit.createInventory(null, 9 * 6, ChatColor.DARK_GREEN + "Item List " + (i + 1)));

            for (int j = 0; j<45 ; j++){
                if (j+45*i<item_list.size()) {
                    inventory_list.get(i).setItem(j, item_list.get(j + 45 * (i)).item);
                }
            }

            inventory_list.get(i).setItem(49, progress);
            if (size > 1) {
                inventory_list.get(i).setItem(53, forward);
                inventory_list.get(i).setItem(45, back);
            }
        }
    }

    private void menuButtons() {
        forward = new ItemStack(Material.ARROW);
        back = new ItemStack(Material.ARROW);
        progress = new ItemStack(Material.DIAMOND);

        ItemMeta forwardM = forward.getItemMeta();
        forwardM.setDisplayName(ChatColor.GOLD + "Forward");
        forward.setItemMeta(forwardM);

        ItemMeta backM = back.getItemMeta();
        backM.setDisplayName(ChatColor.GOLD + "Back");
        back.setItemMeta(backM);

        ItemMeta progM = progress.getItemMeta();
        progM.setDisplayName(ChatColor.AQUA + main.getPlugin().ItemList.getProgString());
        progress.setItemMeta(progM);
    }
}
