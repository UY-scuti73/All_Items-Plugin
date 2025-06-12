package xyz.quazaros.data.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.quazaros.data.config.lang;
import xyz.quazaros.data.items.item;
import xyz.quazaros.data.items.itemList;
import xyz.quazaros.data.player.playerList;
import xyz.quazaros.data.player.playerSort;
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
    ItemStack flip;

    private final Material item_arrow;
    private final Material item_leaderboard;
    private final Material item_progress;
    private final Material item_sort;
    private final Material item_flip;

    public boolean is_mob;
    public boolean is_public;
    public boolean is_playerList;
    public boolean is_sorted;
    public boolean add_flip;

    public inventory() {
        item_arrow = Material.ARROW;
        item_leaderboard = Material.OAK_HANGING_SIGN;
        item_progress = Material.DIAMOND;
        item_sort = Material.HOPPER;
        item_flip = Material.OAK_DOOR;
    }

    //Sets the inventory to reflect the item list
    public void set_inventory(itemList itemList, String text) {
        is_mob = itemList.mob;
        is_public = !itemList.personal;
        is_playerList = false;
        add_flip = true;
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

    public void set_players(boolean mob, boolean public_p) {
        playerList player_list = main.getPlugin().player_list;
        ArrayList<playerSort> plSort = player_list.sort_players(mob, !public_p);

        is_mob = mob;
        is_public = public_p;
        is_playerList = true;
        is_sorted = false;

        String text;
        lang Lang = main.getPlugin().lang;
        if (!is_mob) {
            text = is_public ? Lang.playerList : Lang.personalPlayerList;
        } else {
            text = is_public ? Lang.mobPlayerList : Lang.mobPersonalPlayerList;
        }

        size = (player_list.players.size()/45)+1;
        menuButtons();

        inventory_list.clear();
        sorted_list.clear();

        add_flip = ((!is_public && main.getPlugin().data.general_global) || (is_public && main.getPlugin().data.general_others));

        for (int i = 0; i<size ; i++){
            inventory_list.add(Bukkit.createInventory(null, 9 * 6, ChatColor.DARK_GREEN + text + " " + (i+1)));
            sorted_list.add(Bukkit.createInventory(null, 9 * 6, ChatColor.DARK_GREEN + text + " " + (i + 1)));

            for (int j = 0; j<45 ; j++){
                if (j+45*i<plSort.size()) {
                    inventory_list.get(i).setItem(j, getPlayerHead(plSort.get(j + 45 * (i))));
                }
            }
            if (add_flip) {inventory_list.get(i).setItem(49, flip);}
            if (size > 1) {
                inventory_list.get(i).setItem(53, forward);
                inventory_list.get(i).setItem(45, back);
            }
        }
    }

    //Sets the menu buttons in the alist menu
    public void menuButtons(String progPercent) {
        lang Lang = main.getPlugin().lang;

        forward = new ItemStack(item_arrow);
        back = new ItemStack(item_arrow);
        leaderboard = new ItemStack(item_progress);
        players = new ItemStack(item_leaderboard);
        sort = new ItemStack(item_sort);

        ItemMeta forwardM = forward.getItemMeta();
        forwardM.setDisplayName(Lang.colorHigh + Lang.nextPage);
        forward.setItemMeta(forwardM);

        ItemMeta backM = back.getItemMeta();
        backM.setDisplayName(Lang.colorHigh + Lang.lastPage);
        back.setItemMeta(backM);

        ItemMeta sortM = sort.getItemMeta();
        sortM.setDisplayName(Lang.colorSec + Lang.filterItems);
        sort.setItemMeta(sortM);

        ItemMeta leaderboardM = leaderboard.getItemMeta();
        leaderboardM.setDisplayName(Lang.colorSec + Lang.progress);
        leaderboardM.setLore(Arrays.asList(Lang.colorDom + progPercent));
        leaderboardM.setEnchantmentGlintOverride(true);
        leaderboard.setItemMeta(leaderboardM);

        ItemMeta playersM = players.getItemMeta();
        playersM.setDisplayName(Lang.colorSec + Lang.leaderboard);
        if (!is_mob) {
            if (is_public) {
                playersM.setLore(main.getPlugin().player_list.leaderboard(false, false));
            } else {
                playersM.setLore(main.getPlugin().player_list.leaderboard(false, true));
            }
        } else {
            if (is_public) {
                playersM.setLore(main.getPlugin().player_list.leaderboard(true, false));
            } else {
                playersM.setLore(main.getPlugin().player_list.leaderboard(true, true));
            }
        }
        players.setItemMeta(playersM);
    }

    private void menuButtons() {
        lang Lang = main.getPlugin().lang;

        forward = new ItemStack(item_arrow);
        back = new ItemStack(item_arrow);
        flip = new ItemStack(item_flip);

        ItemMeta forwardM = forward.getItemMeta();
        forwardM.setDisplayName(Lang.colorHigh + Lang.nextPage);
        forward.setItemMeta(forwardM);

        ItemMeta backM = back.getItemMeta();
        backM.setDisplayName(Lang.colorHigh + Lang.lastPage);
        back.setItemMeta(backM);

        ItemMeta flipM = flip.getItemMeta();
        flipM.setDisplayName(Lang.colorSec + (!is_public ? Lang.mainButton : Lang.personalButton));
        flip.setItemMeta(flipM);
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

    private ItemStack getPlayerHead(playerSort pl) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        //getPlayerSafe logic
        OfflinePlayer matched = null;
        for (OfflinePlayer offline : Bukkit.getOfflinePlayers()) {
            if (offline.getName() != null && offline.getName().equalsIgnoreCase(pl.name)) {
                matched = offline;
                break;
            }
        }

        // Only set owning player if they've played before
        if (matched != null && matched.hasPlayedBefore()) {
            meta.setOwningPlayer(matched);
        }

        meta.setDisplayName(main.getPlugin().lang.colorDom + pl.name);
        meta.setLore(Arrays.asList(main.getPlugin().lang.colorSec + "Score: " + pl.score));
        head.setItemMeta(meta);

        return head;
    }
}
