package xyz.quazaros.data.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.quazaros.data.config.lang;
import xyz.quazaros.data.items.item;
import xyz.quazaros.data.items.itemList;
import xyz.quazaros.main;

import java.util.ArrayList;
import java.util.Arrays;

public class inventory {
    public ArrayList<Inventory> inventory_list = new ArrayList<>();
    public ArrayList<Inventory> sorted_list = new ArrayList<>();

    public int size;

    ItemStack forward;
    ItemStack back;
    ItemStack leaderboard;
    ItemStack players;
    ItemStack sort;

    boolean is_mob;
    boolean is_public;

    //Sets the inventory to reflect the item list
    public void set_inventory(itemList itemList, String text) {
        is_mob = itemList.mob;
        is_public = !itemList.personal;
        ArrayList<item> item_list = itemList.get_sub_items();
        ArrayList<item> sorted_items;
        sorted_items = sort(item_list);
        size = (item_list.size()/45)+1;
        menuButtons(itemList.progPer());

        inventory_list.clear();
        sorted_list.clear();

        //Sets every item in the item list to a spot in the menu, and sets the menu buttons
        for (int i = 0; i<size ; i++){
            inventory_list.add(Bukkit.createInventory(null, 9 * 6, ChatColor.DARK_GREEN + text + " " + (i+1)));
            sorted_list.add(Bukkit.createInventory(null, 9 * 6, ChatColor.DARK_GREEN + text + " " + (i + 1)));

            for (int j = 0; j<45 ; j++){
                if (j+45*i<item_list.size()) {
                    inventory_list.get(i).setItem(j, item_list.get(j + 45 * (i)).item_stack);
                    sorted_list.get(i).setItem(j, sorted_items.get(j + 45 * (i)).item_stack);
                }
            }
            inventory_list.get(i).setItem(49, leaderboard);
            sorted_list.get(i).setItem(49, leaderboard);
            inventory_list.get(i).setItem(51, players);
            sorted_list.get(i).setItem(51, players);
            inventory_list.get(i).setItem(47, sort);
            sorted_list.get(i).setItem(47, sort);
            if (size > 1) {
                inventory_list.get(i).setItem(53, forward);
                sorted_list.get(i).setItem(53, forward);
                inventory_list.get(i).setItem(45, back);
                sorted_list.get(i).setItem(45, back);
            }
        }
    }

    //Sets the menu buttons in the alist menu
    public void menuButtons(String progPercent) {
        lang Lang = main.getPlugin().lang;

        forward = new ItemStack(Material.ARROW);
        back = new ItemStack(Material.ARROW);
        leaderboard = new ItemStack(Material.DIAMOND);
        players = new ItemStack(Material.OAK_HANGING_SIGN);
        sort = new ItemStack(Material.HOPPER);

        ItemMeta forwardM = forward.getItemMeta();
        forwardM.setDisplayName(ChatColor.GOLD + Lang.nextPage);
        forward.setItemMeta(forwardM);

        ItemMeta backM = back.getItemMeta();
        backM.setDisplayName(ChatColor.GOLD + Lang.lastPage);
        back.setItemMeta(backM);

        ItemMeta sortM = sort.getItemMeta();
        sortM.setDisplayName(ChatColor.AQUA + Lang.filterItems);
        sort.setItemMeta(sortM);

        ItemMeta leaderboardM = leaderboard.getItemMeta();
        leaderboardM.setDisplayName(ChatColor.AQUA + Lang.progress);
        leaderboardM.setLore(Arrays.asList(ChatColor.LIGHT_PURPLE + progPercent));
        leaderboardM.setEnchantmentGlintOverride(true);
        leaderboard.setItemMeta(leaderboardM);

        ItemMeta playersM = players.getItemMeta();
        playersM.setDisplayName(ChatColor.AQUA + Lang.leaderboard);
        if (!is_mob) {
            playersM.setLore(main.getPlugin().player_list.leaderboard(false));
        } else {
            playersM.setLore(main.getPlugin().player_list.leaderboard(true));
        }
        players.setItemMeta(playersM);
    }

    //Sorts the items for the filter
    private ArrayList<item> sort(ArrayList<item> items) {
        ArrayList<item> temp = new ArrayList<>();
        for (item i : items) {
            if (!i.isFound) {
                temp.add(i);
            }
        }
        for (item i : items) {
            if (i.isFound) {
                temp.add(i);
            }
        }
        return temp;
    }
}
