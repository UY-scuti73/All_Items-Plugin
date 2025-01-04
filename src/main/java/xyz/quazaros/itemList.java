package xyz.quazaros;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionType;

public class itemList {
    ArrayList<item> items;
    ArrayList<Integer> indexes;
    List<String> item_names;

    private int enchantedBook_index;
    private int potion_index;
    private int splashPotion_index;
    private int lingeringPotion_index;
    private int tippedArrow_index;
    private int goatHorn_index;

    metaList meta_list;

    //Initialize the meta_list
    public itemList() {
        items = new ArrayList<item>();
        indexes = new ArrayList<>();
        meta_list = new metaList();
        item_names = new ArrayList<>();
    }

    //This will get the index of the item to submit
    private ArrayList<Integer> get_index(ItemStack current_item) {
        ArrayList<Integer> Index_List = new ArrayList<>();
        String temp = "";
        if (current_item.getType().equals(Material.ENCHANTED_BOOK)) {
            Index_List.add(enchantedBook_index);

        } else if (current_item.getType().equals(Material.POTION)) {
            if (!items.get(potion_index).isFound) {
                Index_List.add(potion_index);
            }
        } else if (current_item.getType().equals(Material.SPLASH_POTION)) {
            if (!items.get(splashPotion_index).isFound) {
                Index_List.add(splashPotion_index);
            }
        } else if (current_item.getType().equals(Material.LINGERING_POTION)) {
            if (!items.get(lingeringPotion_index).isFound) {
                Index_List.add(lingeringPotion_index);
            }
        } else if (current_item.getType().equals(Material.TIPPED_ARROW)) {
            if (!items.get(tippedArrow_index).isFound) {
                Index_List.add(tippedArrow_index);
            }
        } else if (current_item.getType().equals(Material.GOAT_HORN)) {
            if (!items.get(goatHorn_index).isFound) {
                Index_List.add(goatHorn_index);
            }
        }

        if (current_item.getType().equals(Material.AIR)) {
            Index_List.add(-1);
        //Checks if the current item is an enchanted book
        } else if (current_item.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta currentItemMeta = (EnchantmentStorageMeta) current_item.getItemMeta();
            ItemStack bookItem = new ItemStack(Material.ENCHANTED_BOOK);
            for (int i = 0; i < items.size(); i++) {
                if (currentItemMeta.getStoredEnchants().size() == 1) {
                    for (enchantments e : meta_list.enchantment_list) {
                        EnchantmentStorageMeta tempMeta = (EnchantmentStorageMeta) bookItem.getItemMeta();
                        tempMeta.addStoredEnchant(e.enchant, e.level, true);
                        //If the current enchantment has the same enchant as the current item, and items[i]'s name matches the enchant.
                        if (tempMeta.getStoredEnchants().equals(currentItemMeta.getStoredEnchants()) && e.name.equals(items.get(i).item_name)) {
                            for (enchantments general_e : meta_list.enchantment_list) {
                                if (general_e.name.equals(e.name.substring(0, e.name.length()-2))) {
                                    Index_List.add(get_item_index(general_e.name));
                                }
                            }
                            Index_List.add(i);
                        }
                    }
                }
            }
        //Checks if the current item is a potion
        } else if (check_potion(current_item.getType())) {
            PotionMeta currentItemMeta = (PotionMeta) current_item.getItemMeta();
            if (current_item.getType().equals(Material.POTION)) {
                Index_List.add(potion_index);
            } else if (current_item.getType().equals(Material.SPLASH_POTION)) {
                Index_List.add(splashPotion_index);
            } else if (current_item.getType().equals(Material.LINGERING_POTION)) {
                Index_List.add(lingeringPotion_index);
            } else if (current_item.getType().equals(Material.TIPPED_ARROW)) {
                Index_List.add(tippedArrow_index);
            }
            for (int i = 0; i < items.size(); i++) {
                for (potions e : meta_list.potion_list) {
                    for (PotionType effect : e.effect) {
                        //If the current item has the same potion effect as the current effect, and the name of the potion effect is in the name of items[i], and the item type is the right potion
                        if (currentItemMeta.getBasePotionType().equals(effect) && items.get(i).item_name.contains(e.name) && items.get(i).item_type.equals(current_item.getType())) {
                            Index_List.add(i);
                        }
                    }
                }
            }
        //Checks if the item is a goat horn
        } else if (current_item.getType().equals(Material.GOAT_HORN)) {
            Index_List.add(goatHorn_index);
            MusicInstrumentMeta currentItemMeta = (MusicInstrumentMeta) current_item.getItemMeta();
            for (int i = 0; i < items.size(); i++) {
                for (instruments e : meta_list.instrument_list) {
                    //Checks if the instrument is the same, and the name is the same as items[i]
                    if (currentItemMeta.getInstrument().equals(e.instrument) && items.get(i).item_name.equals(e.name)) {
                        Index_List.add(i);
                    }
                }
            }
        //Checks for any other item
        } else {
            for (int i=0; i<items.size(); i++) {
                if (items.get(i).item_type.equals(current_item.getType())) {
                    Index_List.add(i);
                }
            }
        }
        return Index_List;
    }

    //This will check and submit the right item
    public String check_items(ItemStack current_item, String p, boolean non_specific) {
        ArrayList<Integer> Index_List = get_index(current_item);

        if (Index_List.size() == 0) {
            return ChatColor.RED + "No Items Found";
        }
        if (Index_List.get(0) == -1) {
            return ChatColor.RED + "Please Enter an Item";
        }

        String temp = "";

        boolean item_found = false;
        for (int i : Index_List) {
            if (!items.get(i).isFound) {
                if (!non_specific || is_in_indexes(i)) {
                    item_found = true;
                }
            }
        }

        if (!item_found) {
            return generate_string(Index_List.get(Index_List.size()-1));
        }

        //This will provide the right output, based on the priority Specific-General, In_List-Not_In_List, Found-Not_Found
        for (int i : Index_List) {
            if (is_in_indexes(i)) {
                if (!items.get(i).isFound) {
                    temp = generate_string(i);
                }
            }
        }
        if (temp.equals("")) {
            for (int i: Index_List) {
                if (!items.get(i).isFound) {
                    temp = generate_string(i);
                }
            }
        }

        if (item_found) {
            for (int i : Index_List) {
                if (!items.get(i).isFound) {
                    items.get(i).submit(p, date());
                }
            }
        }
        return temp;
    }

    //Handles what happens when an item is about to be submitted
    public String generate_string(int temp) {
        if (!items.get(temp).isFound) {
            if (is_in_indexes(temp)) {
                return ChatColor.GREEN + items.get(temp).item_display_name + " Submitted!";
            } else {
                return ChatColor.RED + "Item submitted, but it is not in the list";
            }
        } else {
            return ChatColor.RED + "Item Already Obtained By " + items.get(temp).item_founder;
        }
    }

    //Gets the items index from it's name
    public int get_item_index(String item_name) {
        for (int i=0; i<items.size(); i++) {
            if (items.get(i).item_name.equalsIgnoreCase(item_name)) {
                return i;
            }
        }
        return -1;
    }

    //Gets the item from the items name
    private item get_item(String item_name) {
        for (item i : items) {
            if (item_name.equals(i.item_name)) {
                return i;
            }
        }
        return null;
    }

    //Check if the index is apart of the main list
    public boolean is_in_indexes(int index) {
        for (int i : indexes) {
            if (i == index) {
                return true;
            }
        }
        return false;
    }

    //Turns the list of strings into a list of items
    public ArrayList<Integer> string_to_index(ArrayList<String> str_list) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (String s : str_list) {
            for (int i=0; i<items.size(); i++) {
                if (items.get(i).item_name.equalsIgnoreCase(s.replace("\n", ""))){
                    temp.add(i);
                    break;
                }
            }
        }
        return temp;
    }

    //Turns the current date and time into a readable string
    public String date() {
        LocalDateTime d = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d h:mm a");
        return d.format(formatter);
    }

    //Updates the list of item names
    private void update_item_names() {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            temp.add(items.get(i).item_name);
        }
        item_names = temp;
    }

    //Gets the indexes for the items in the sub list and makes a new list using the data from the total list
    public ArrayList<item> get_sub_items() {
        ArrayList<item> temp = new ArrayList<>();
        for (int i = 0; i < indexes.size(); i++) {
            temp.add(items.get(indexes.get(i)));
        }
        return temp;
    }

    //Initializes special indexes like enchanted books and potions in the list of items
    private void specialIndex_initialize() {
        enchantedBook_index = -1;
        potion_index = -1;
        splashPotion_index = -1;
        lingeringPotion_index = -1;
        tippedArrow_index = -1;
        goatHorn_index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).item_name.equals("enchanted_book")) {
                enchantedBook_index = i;
            } else if (items.get(i).item_name.equals("potion")) {
                potion_index = i;
            } else if (items.get(i).item_name.equals("splash_potion")) {
                splashPotion_index = i;
            } else if (items.get(i).item_name.equals("lingering_potion")) {
                lingeringPotion_index = i;
            } else if (items.get(i).item_name.equals("tipped_arrow")) {
                tippedArrow_index = i;
            } else if (items.get(i).item_name.equals("goat_horn")) {
                goatHorn_index = i;
            }
        }
    }

    //Checks if an item is a potion/splash potion/lingering potion/tipped arrow
    public boolean check_potion(Material item_type) {
        if ((item_type.equals(Material.POTION)) || (item_type.equals(Material.SPLASH_POTION)) || (item_type.equals(Material.LINGERING_POTION)) || (item_type.equals(Material.TIPPED_ARROW))) {
            return true;
        }
        else {
            return false;
        }
    }

    //Upon first starting the plugin this will initialize all the items
    public void total() {
        //metaList meta_list = new metaList();
        for (item i : meta_list.items) {
            items.add(i);
        }

        //Initialize special indexes
        specialIndex_initialize();

        //Update the name list
        update_item_names();
    }

    //Upon first starting the plugin this will initialize all the mobs
    public void total_mobs() {
        for (item i : meta_list.mobs) {
            items.add(i);
        }

        //Update the name list
        update_item_names();
    }
}

class item {
    Material item_type;
    String item_name;
    String item_display_name;
    String item_founder;
    String item_time;
    ArrayList<String> item_lore;
    boolean isFound;

    itemData item_data;

    ItemStack item_stack;
    ItemMeta item_meta;

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
            item_meta.setDisplayName(ChatColor.RED + item_display_name);
            item_lore.add(ChatColor.AQUA + "Not Found");
            item_meta.setEnchantmentGlintOverride(false);
            item_meta.setLore(item_lore);
            item_stack.setItemMeta(item_meta);
        }
    }

    //Handles what happens when an item is submitted
    public void submit(String p, String time) {
        item_founder = p;
        item_time = time;
        item_meta.setDisplayName(ChatColor.GREEN+item_display_name);
        item_lore.set(0, ChatColor.AQUA+"Found");
        item_lore.add(ChatColor.AQUA+"By " + item_founder);
        item_lore.add(ChatColor.AQUA+"At " + item_time);
        item_meta.setLore(item_lore);
        item_meta.setEnchantmentGlintOverride(true);
        item_stack.setItemMeta(item_meta);
        item_data.submit(item_name, p, time);
        isFound = true;
    }

    //Handles what happens when an item is unsubmitted
    public void unsubmit() {
        ArrayList<String> empty = new ArrayList<>();
        empty.add(ChatColor.AQUA + "Not Found");
        item_founder = "";
        item_time = "";
        item_meta.setDisplayName(ChatColor.RED+item_display_name);
        item_lore = empty;
        item_meta.setLore(item_lore);
        item_meta.setEnchantmentGlintOverride(false);
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
        item_meta.setDisplayName(ChatColor.RED + item_display_name);
        item_meta.setEnchantmentGlintOverride(false);
        item_stack.setItemMeta(item_meta);

        item_data.set_name(s);
    }

    //Turns the item name into a nicer looking display name
    private String camel_case(String str) {
        for (int i = 1; i<str.length(); i++){
            if (str.charAt(i) == '_') {
                str = str.substring(0,i) + " "  + str.substring(i+1);
            }
            if (str.charAt(i-1) == ' ') {
                str = str.substring(0,i) + str.substring(i,i+1).toUpperCase() + str.substring(i+1);
            }
        }
        str = str.substring(0,1).toUpperCase() + str.substring(1, str.length());
        return str;
    }
}

class itemData {
    @Expose Boolean is_found;
    @Expose String name;
    @Expose String player;
    @Expose String date;

    public itemData(String n) {
        name = n;
        is_found = false;
        player = "";
        date = "";
    }

    public void set_name(String n) {
        name = n;
    }

    public void submit(String n, String p, String d) {
        is_found = true;
        name = n;
        player = p;
        date = d;
    }

    public void unsubmit() {
        is_found = false;
        player = "";
        date = "";
    }
}

