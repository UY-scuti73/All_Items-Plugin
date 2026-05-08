package xyz.quazaros.structures.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.quazaros.main;

import java.util.ArrayList;

import static xyz.quazaros.structures.items.itemSprite.getSprite;

public class item {
    public Material item_type;
    public String item_name;
    public String item_display_name;
    public String item_sprite;
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
            item_sprite = getSprite(item_type);
            item_founder = "";
            item_time = "";
            item_lore = new ArrayList<>();

            isFound = false;

            item_data = new itemData(item_name);

            item_stack = new ItemStack(item_type, 1);
            item_meta = item_stack.getItemMeta();
            item_lore.add(main.getPlugin().variables.lang.colorSec + main.getPlugin().variables.lang.menuItemNotFound);
            item_meta.setLore(item_lore);
            main.getPlugin().variables.version.addFlags(item_meta);
            main.getPlugin().variables.version.setGlint(item_meta, false);
            item_stack.setItemMeta(item_meta);

            setDisplayName();
        }
    }

    //Copies an item from another item
    public item(item item) {
        item_type = item.item_type;
        item_name = "WRONG VERSION";
        if (!item_type.isAir()) {
            item_name = item.item_name;
            item_display_name = camel_case(item_name);
            item_sprite = getSprite(item_type);
            item_founder = "";
            item_time = "";
            item_lore = new ArrayList<>();

            isFound = false;

            item_data = new itemData(item_name);

            item_stack = new ItemStack(item.item_stack);
            item_meta = item_stack.getItemMeta();
            item_lore.add(main.getPlugin().variables.lang.colorSec + main.getPlugin().variables.lang.menuItemNotFound);
            item_meta.setLore(item_lore);
            main.getPlugin().variables.version.addFlags(item_meta);
            main.getPlugin().variables.version.setGlint(item_meta, false);
            item_stack.setItemMeta(item_meta);

            setDisplayName();
        }
    }

    //Handles what happens when an item is submitted
    public void submit(String p, String time) {
        if (isFound) {return;}
        item_founder = p;
        item_time = time;
        item_meta.setDisplayName(main.getPlugin().variables.lang.colorGood + item_display_name);
        item_lore.set(0, main.getPlugin().variables.lang.colorSec + main.getPlugin().variables.lang.menuItemFound);
        item_lore.add(main.getPlugin().variables.lang.colorSec + main.getPlugin().variables.lang.byPlayer + " " + item_founder);
        item_lore.add(main.getPlugin().variables.lang.colorSec + main.getPlugin().variables.lang.atTime + " " + item_time);
        item_meta.setLore(item_lore);
        main.getPlugin().variables.version.setGlint(item_meta, true);
        item_stack.setItemMeta(item_meta);
        item_data.submit(item_name, p, time);
        isFound = true;
        update();
    }

    public void submit(String time) {
        if (isFound) {return;}
        item_time = time;
        item_meta.setDisplayName(main.getPlugin().variables.lang.colorGood + item_display_name);
        item_lore.set(0, main.getPlugin().variables.lang.colorSec + main.getPlugin().variables.lang.menuItemFound);
        item_lore.add(main.getPlugin().variables.lang.colorSec + main.getPlugin().variables.lang.atTime + " " + item_time);
        item_meta.setLore(item_lore);
        main.getPlugin().variables.version.setGlint(item_meta, true);
        item_stack.setItemMeta(item_meta);
        item_data.submit(item_name, "", time);
        isFound = true;
        update();
    }

    //Handles what happens when an item is unsubmitted
    public void unsubmit() {
        if (!isFound) {return;}
        ArrayList<String> empty = new ArrayList<>();
        empty.add(main.getPlugin().variables.lang.colorSec + main.getPlugin().variables.lang.menuItemNotFound);
        item_founder = "";
        item_time = "";
        item_meta.setDisplayName(main.getPlugin().variables.lang.colorBad + item_display_name);
        item_lore = empty;
        item_meta.setLore(item_lore);
        main.getPlugin().variables.version.setGlint(item_meta, false);
        item_stack.setItemMeta(item_meta);
        item_data.unsubmit();
        isFound = false;
        update();
    }

    private void setDisplayName() {

        //if (item_name == null) {return;}

        //String nbt = "{display:{Name:'" + item_sprite + "'}}";
        //System.out.println(nbt);

        //nbt = "{display:{Name:'{\"type\":\"object\",\"item\":\"lingering_potion\"}'}}";
        //String nbt = "{display:{Name:\"{\\\"text\\\":\\\"My Super Sword\\\"}\"}}";

        /*String name = item_type.toString().toLowerCase();
        String nbt = "{Item:{id:\"" + name + "\",components:{\"minecraft:custom_name\":{text:\"TEST\"}}}}";
        String command = "/summon item 0 300 0 " + nbt;
        System.out.println(command);


        String nbt = "{Item:{id:\"" + "diamond" + "\",components:{\"minecraft:custom_name\":{text:\"TEST\"}}}}";
        String command = "summon cow 0 300 0";
        System.out.println(command);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say Hello world!");

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);*/

        //item_meta.setDisplayName(main.getPlugin().variables.lang.colorBad + item_display_name + item_sprite);
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
        item_meta.setDisplayName(main.getPlugin().variables.lang.colorBad + item_display_name);
        main.getPlugin().variables.version.setGlint(item_meta, false);
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

    //Updates the player score
    private void update() {
        main.getPlugin().variables.player_list.initialize_score();
    }

    //1.21.6 breaks if the vault spawner doesn't have blockdata
    private void ifSpawner() {
        if (!main.getPlugin().variables.version.checkItem(item_type, "trial_spawner")) {return;}
        if (main.getPlugin().variables.version.get_version() <= 21.5) {return;}

        item_meta = Bukkit.getItemFactory().getItemMeta(item_type);
    }
}
