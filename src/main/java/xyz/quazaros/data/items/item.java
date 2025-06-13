package xyz.quazaros.data.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.quazaros.main;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class item {
    public Material item_type;
    public String item_name;
    public String item_display_name;
    public String item_founder;
    public String item_time;
    public ArrayList<String> item_lore;
    public boolean isFound;

    public itemData item_data;

    public ItemStack item_stack;
    public ItemMeta item_meta;

    //Initializes a not found item
    public item(String name) {
        item_type = get_type(name);
        item_name = "WRONG VERSION";
        if (!item_type.isAir()) {
            item_name = name;
            item_display_name = camel_case(name);
            item_founder = "";
            item_time = "";
            item_lore = new ArrayList<>();

            isFound = false;

            item_data = new itemData(item_name);

            item_stack = new ItemStack(item_type, 1);
            item_meta = item_stack.getItemMeta();
            item_meta.setDisplayName(main.getPlugin().lang.colorBad + item_display_name);
            item_lore.add(main.getPlugin().lang.colorSec + main.getPlugin().lang.menuItemNotFound);
            main.getPlugin().version.setGlint(item_meta, false);
            item_meta.setLore(item_lore);
            item_stack.setItemMeta(item_meta);
        }
    }

    //Copies an item from another item
    public item(item item) {
        item_type = item.item_type;
        item_name = "WRONG VERSION";
        if (!item_type.isAir()) {
            item_name = item.item_name;
            item_display_name = camel_case(item_name);
            item_founder = "";
            item_time = "";
            item_lore = new ArrayList<>();

            isFound = false;

            item_data = new itemData(item_name);

            item_stack = new ItemStack(item.item_stack);
            item_meta = item_stack.getItemMeta();
            item_meta.setDisplayName(main.getPlugin().lang.colorBad + item_display_name);
            item_lore.add(main.getPlugin().lang.colorSec + main.getPlugin().lang.menuItemNotFound);
            main.getPlugin().version.setGlint(item_meta, false);
            item_meta.setLore(item_lore);
            item_stack.setItemMeta(item_meta);
        }
    }

    //Handles what happens when an item is submitted
    public void submit(String p, String time) {
        item_founder = p;
        item_time = time;
        item_meta.setDisplayName(main.getPlugin().lang.colorGood + item_display_name);
        item_lore.set(0, main.getPlugin().lang.colorSec + main.getPlugin().lang.menuItemFound);
        item_lore.add(main.getPlugin().lang.colorSec + main.getPlugin().lang.byPlayer + " " + item_founder);
        item_lore.add(main.getPlugin().lang.colorSec + main.getPlugin().lang.atTime + " " + item_time);
        item_meta.setLore(item_lore);
        main.getPlugin().version.setGlint(item_meta, true);
        item_stack.setItemMeta(item_meta);
        item_data.submit(item_name, p, time);
        isFound = true;
    }

    public void submit(String time) {
        item_time = time;
        item_meta.setDisplayName(main.getPlugin().lang.colorGood + item_display_name);
        item_lore.set(0, main.getPlugin().lang.colorSec + main.getPlugin().lang.menuItemFound);
        item_lore.add(main.getPlugin().lang.colorSec + main.getPlugin().lang.atTime + " " + item_time);
        item_meta.setLore(item_lore);
        main.getPlugin().version.setGlint(item_meta, true);
        item_stack.setItemMeta(item_meta);
        item_data.submit(item_name, "", time);
        isFound = true;
    }

    //Handles what happens when an item is unsubmitted
    public void unsubmit() {
        ArrayList<String> empty = new ArrayList<>();
        empty.add(main.getPlugin().lang.colorSec + main.getPlugin().lang.menuItemNotFound);
        item_founder = "";
        item_time = "";
        item_meta.setDisplayName(main.getPlugin().lang.colorGood + item_display_name);
        item_lore = empty;
        item_meta.setLore(item_lore);
        main.getPlugin().version.setGlint(item_meta, false);
        item_stack.setItemMeta(item_meta);
        item_data.unsubmit();
        isFound = false;
    }

    //Gets the type of an item
    private Material get_type(String name) {
        for (Material m : Material.values()) {
            if (m.toString().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return Material.AIR;
    }

    //Changes the name of an item
    public void set_name(String s) {
        item_name = s;
        item_display_name = camel_case(s);

        item_stack = new ItemStack(item_type, 1);
        item_meta = item_stack.getItemMeta();
        item_meta.setDisplayName(main.getPlugin().lang.colorBad + item_display_name);
        main.getPlugin().version.setGlint(item_meta, false);
        item_stack.setItemMeta(item_meta);

        item_data.set_name(s);
    }

    //Turns the item name into a nicer looking display name
    private String camel_case(String str) {
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == '_') {
                str = str.substring(0, i) + " " + str.substring(i + 1);
            }
            if (str.charAt(i - 1) == ' ') {
                str = str.substring(0, i) + str.substring(i, i + 1).toUpperCase() + str.substring(i + 1);
            }
        }
        str = str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
        return str;
    }
}
