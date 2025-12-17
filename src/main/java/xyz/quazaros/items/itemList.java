package xyz.quazaros.items;

import org.bukkit.Material;
import xyz.quazaros.main;

import java.util.ArrayList;

import static xyz.quazaros.file.retrieveStringList;

public class itemList {
    public ArrayList<item> item_list;

    public itemList() {
        item_list = new ArrayList<>();

        ArrayList<String> item_string_list = retrieveStringList();
        item_string_list = main.getPlugin().Version.modifyStringList(item_string_list);

        for (String item_string : item_string_list) {
            Material type = getSpawnEgg(item_string);
            if (type == null) {continue;}
            item_list.add(new item(item_string, type));
        }
    }

    private Material getSpawnEgg(String mob) {
        mob = mob + "_spawn_egg";
        for (Material m : Material.values()) {
            if (m.toString().equalsIgnoreCase(mob)) {
                return m;
            }
        }
        return null;
    }

    public item submitItem(String mob) {
        int index = getIndex(mob);
        if (index <= -1) {return null;}

        item tempItem = item_list.get(index);
        if (tempItem == null || tempItem.isFound) {return null;}

        tempItem.submit();
        return tempItem;
    }

    public int getIndex(String mob) {
        for (int i = 0; i < item_list.size(); i++) {
            if (item_list.get(i).item_name.equalsIgnoreCase(mob)) {
                return i;
            }
        }
        return -1;
    }

    public int size() {
        return item_list.size();
    }

    public item get(int index) {
        return item_list.get(index);
    }

    public int getProg() {
        int i = 0;
        for (item item : item_list) {
            if (item.isFound) {
                i++;
            }
        }
        return i;
    }

    public String getProgString() {
        return getProg() + "/" + size();
    }
}
