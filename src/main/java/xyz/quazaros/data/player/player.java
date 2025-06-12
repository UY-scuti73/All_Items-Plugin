package xyz.quazaros.data.player;

import xyz.quazaros.data.items.itemList;
import xyz.quazaros.data.inventory.inventory;
import xyz.quazaros.main;

public class player {
    public String name;
    public int score;
    public int mobScore;
    public int itemPerScore;
    public int mobPerScore;
    public int invItt;
    public itemList item_list;
    public itemList mob_list;
    public inventory inv;

    public player(String p_name) {
        name = p_name;
        score = 0;
        mobScore = 0;
        itemPerScore = 0;
        mobPerScore = 0;

        invItt = 0;

        item_list = new itemList(main.getPlugin().emptyItemList, true);
        mob_list = new itemList(main.getPlugin().emptyMobList, true);
        inv = new inventory();
    }
}
