package xyz.quazaros.structures.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static xyz.quazaros.extra.sprites.conversions.toNBT;
import static xyz.quazaros.util.main.mainVariables.getVariables;

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
        item_abs(name, false);
    }

    //Copies an item from another item
    public item(item item) {
        item_type = item.item_type;
        item_stack = item.item_stack.clone();
        item_sprite = item.item_sprite;
        item_abs(item.item_name, true);
    }

    private void item_abs(String name, boolean hasStack) {
        if (!hasStack) {item_type = get_type(name);}

        item_name = "WRONG VERSION";

        if (item_type.isAir()) {return;}

        item_name = name;
        item_display_name = camel_case(name);

        if (!hasStack) {item_sprite = getVariables().sprite.getSprite(item_type);}

        item_founder = "";
        item_time = "";
        item_lore = new ArrayList<>();
        item_lore.add(getVariables().lang.colorSec + getVariables().lang.menuItemNotFound);

        isFound = false;

        item_data = new itemData(item_name);

        if (hasStack) {
            item_meta = item_stack.getItemMeta();
        } else {
            item_stack = new ItemStack(item_type, 1);
        }

        setupItemMeta(false);
    }

    //Handles what happens when an item is submitted
    public void submit(String p, String time) {
        submit_abs(time, p);
    }

    public void submit(String time) {
        submit_abs(time, "");
    }

    private void submit_abs(String time, String p) {
        if (isFound) {return;}
        item_time = time;
        item_founder = p;

        item_lore = new ArrayList<>();
        item_lore.add(getVariables().lang.colorSec + getVariables().lang.menuItemFound);
        if (!item_founder.isEmpty()) {item_lore.add(getVariables().lang.colorSec + getVariables().lang.byPlayer + " " + item_founder);}
        item_lore.add(getVariables().lang.colorSec + getVariables().lang.atTime + " " + item_time);

        setupItemMeta(true);

        item_data.submit(item_name, p, time);
        isFound = true;
        update();
    }

    //Handles what happens when an item is unsubmitted
    public void unsubmit() {
        if (!isFound) {return;}

        item_lore = new ArrayList<>();
        item_lore.add(getVariables().lang.colorSec + getVariables().lang.menuItemNotFound);
        item_founder = "";
        item_time = "";

        setupItemMeta(false);

        item_data.unsubmit();
        isFound = false;
        update();
    }

    private void setupItemMeta(boolean found) {
        String tempColor = found ? getVariables().lang.colorGoodStr : getVariables().lang.colorBadStr;
        ChatColor tempChatColor = found ? getVariables().lang.colorGood : getVariables().lang.colorBad;

        if (getVariables().data.general_sprites) {
            String nbt =
                    item_type.name().toLowerCase() + "[custom_name=" +
                            toNBT(tempColor, item_display_name, item_sprite, null) + "]";

            //Sets up the sprite
            item_stack = Bukkit.getUnsafe().modifyItemStack(item_stack, nbt);
            item_meta = item_stack.getItemMeta();
        } else {
            item_meta = item_stack.getItemMeta();
            item_meta.setDisplayName(tempChatColor + item_display_name);
        }

        item_meta.setLore(item_lore);
        getVariables().version.addFlags(item_meta);
        getVariables().version.setGlint(item_meta, found);
        item_stack.setItemMeta(item_meta);
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
        item_meta.setDisplayName(getVariables().lang.colorBad + item_display_name);
        getVariables().version.setGlint(item_meta, false);
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
        getVariables().player_list.initialize_score();
    }

    //1.21.6 breaks if the vault spawner doesn't have blockdata
    private void ifSpawner() {
        if (!getVariables().version.checkItem(item_type, "trial_spawner")) {return;}
        if (getVariables().version.get_version() <= 21.5) {return;}

        item_meta = Bukkit.getItemFactory().getItemMeta(item_type);
    }
}
