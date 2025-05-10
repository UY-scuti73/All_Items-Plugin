package xyz.quazaros;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class inventory {
    ArrayList<Inventory> inventory_list = new ArrayList<>();
    ArrayList<Inventory> sorted_list = new ArrayList<>();
    int size;
    int progress;
    int total;

    ItemStack forward;
    ItemStack back;
    ItemStack leaderboard;
    ItemStack players;
    ItemStack sort;

    boolean is_mob;
    boolean is_public;

    public inventory(boolean mob) {
        forward = new ItemStack(Material.ARROW);
        back = new ItemStack(Material.ARROW);
        leaderboard = new ItemStack(Material.DIAMOND);
        players = new ItemStack(Material.OAK_HANGING_SIGN);
        sort = new ItemStack(Material.HOPPER);
    }

    //Sets the inventory to reflect the item list
    public void set_inventory(ArrayList<item> item_list, int prog, String text, boolean mob, boolean pub) {
        is_mob = mob;
        is_public = pub;
        ArrayList<item> sorted_items;
        sorted_items = sort(item_list);
        size = (item_list.size()/45)+1;
        total = item_list.size();
        progress = prog;
        menuButtons(total, prog);

        //Sets every item in the item list to a spot in the menu, and sets the menu buttons
        for (int i = 0; i<size ; i++){
            inventory_list.add(Bukkit.createInventory(null, 9*6, ChatColor.DARK_GREEN + text + " " + (i+1)));
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
    public void menuButtons(int total, int completed) {
        ItemMeta forwardM = forward.getItemMeta();
        forwardM.setDisplayName(ChatColor.GOLD + "Next Page");
        forward.setItemMeta(forwardM);

        ItemMeta backM = back.getItemMeta();
        backM.setDisplayName(ChatColor.GOLD + "Last Page");
        back.setItemMeta(backM);

        ItemMeta sortM = sort.getItemMeta();
        sortM.setDisplayName(ChatColor.AQUA + "Filter Items");
        sort.setItemMeta(sortM);

        ItemMeta leaderboardM = leaderboard.getItemMeta();
        leaderboardM.setDisplayName(ChatColor.AQUA + "Progress");
        leaderboardM.setLore(Arrays.asList(ChatColor.LIGHT_PURPLE + String.valueOf(completed) + "/" + String.valueOf(total)));
        leaderboardM.setEnchantmentGlintOverride(true);
        leaderboard.setItemMeta(leaderboardM);

        ItemMeta playersM = players.getItemMeta();
        playersM.setDisplayName(ChatColor.AQUA + "Leaderboard");
        if (!is_mob) {
            playersM.setLore(main.getPlugin().player_list.leaderboard(false));
        } else {
            playersM.setLore(main.getPlugin().player_list.leaderboard(true));
        }
        players.setItemMeta(playersM);
    }

    //Sorts the items for the filter
    public ArrayList<item> sort(ArrayList<item> items) {
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
