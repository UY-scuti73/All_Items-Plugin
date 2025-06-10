package xyz.quazaros.data.items;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionType;

import xyz.quazaros.main;
import xyz.quazaros.data.meta.*;

public class itemList {
    public ArrayList<item> items;
    public ArrayList<Integer> indexes;
    public List<String> item_names;

    public boolean mob;
    public boolean personal;
    public boolean completed;

    private int enchantedBook_index;
    private int potion_index;
    private int splashPotion_index;
    private int lingeringPotion_index;
    private int tippedArrow_index;
    private int goatHorn_index;

    metaList meta_list;

    //Initialize the item_list
    public itemList() {
        initialize();
    }

    public itemList(boolean mob_p, ArrayList<String> str_list) {
        mob = mob_p;
        personal = false;
        completed = false;
        initialize();
        if (!mob) {
            total();
        } else {
            total_mobs();
        }
        setIndexes(str_list);
    }

    public itemList(itemList item_list, boolean personal_p) {
        mob = item_list.mob;
        personal = personal_p;
        completed = false;

        items = new ArrayList<>();

        for (item i : item_list.items) {
            items.add(new item(i));
        }

        indexes = new ArrayList<>(item_list.indexes);
        item_names = new ArrayList<>(item_list.item_names);
        meta_list = main.getPlugin().meta_list;

        enchantedBook_index = item_list.enchantedBook_index;
        potion_index = item_list.potion_index;
        splashPotion_index = item_list.splashPotion_index;
        lingeringPotion_index = item_list.lingeringPotion_index;
        tippedArrow_index = item_list.tippedArrow_index;
        goatHorn_index = item_list.goatHorn_index;
    }

    private void initialize() {
        items = new ArrayList<item>();
        indexes = new ArrayList<>();
        item_names = new ArrayList<>();
        meta_list = main.getPlugin().meta_list;
    }

    //Upon first starting the plugin this will initialize all the items
    public void total() {
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

    //This will set all the indexes
    public void setIndexes(ArrayList<String> str_list) {
        indexes = string_to_index(str_list);
    }

    //Turns the list of strings into a list of items
    private ArrayList<Integer> string_to_index(ArrayList<String> str_list) {
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

    //This will get the index of the item to submit
    private ArrayList<Integer> get_index(ItemStack current_item) {
        ArrayList<Integer> Index_List = new ArrayList<>();
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
                    if (!personal) {items.get(i).submit(p, date());}
                    else {items.get(i).submit(date());}
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

    //Check if the index is a part of the main list
    public boolean is_in_indexes(int index) {
        for (int i : indexes) {
            if (i == index) {
                return true;
            }
        }
        return false;
    }

    //Checks if an item is a part of the list
    public boolean item_exists(String item_name) {
        for (item i : items) {
            if (i.item_name.equalsIgnoreCase(item_name)) {
                return true;
            }
        }
        return false;
    }

    //Gets the total progress
    public int get_progress() {
        int cnt = 0;
        for (int i : indexes) {
            if (items.get(i).isFound) {
                cnt++;
            }
        }
        return cnt;
    }

    //Gets the progress percent string
    public String progPer() {
        String s1 = Integer.toString(get_progress());
        String s2 = Integer.toString(indexes.size());
        return s1 + "/" + s2;
    }

    //Checks if a list is complete
    public boolean complete() {
        if (completed) {return false;}

        int prog = get_progress();
        int total = indexes.size();
        int temp = prog/total;
        if (temp == 1) {
            completed = true;
            return true;
        } else {
            return false;
        }
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

    //Checks if an item is a potion/splash potion/lingering potion/tipped arrow
    public boolean check_potion(Material item_type) {
        return (item_type.equals(Material.POTION)) || (item_type.equals(Material.SPLASH_POTION)) || (item_type.equals(Material.LINGERING_POTION)) || (item_type.equals(Material.TIPPED_ARROW));
    }
}

