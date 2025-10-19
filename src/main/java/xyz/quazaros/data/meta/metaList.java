package xyz.quazaros.data.meta;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import xyz.quazaros.data.items.item;
import xyz.quazaros.data.main.file;
import xyz.quazaros.main;

import java.util.ArrayList;

public class metaList {

    file File;

    public ArrayList<item> items;
    public ArrayList<item> mobs;

    public ArrayList<enchantments> enchantment_list;
    public ArrayList<ArrayList<enchantments>> main_enchantment_list;
    public ArrayList<potions> potion_list;
    //public ArrayList<instruments> instruments_list;

    ArrayList<String> items_init;
    ArrayList<String> mobs_init;

    public metaList(ArrayList<String> items_init_p, ArrayList<String> mobs_init_p) {
        File = main.getPlugin().file;

        items = new ArrayList<>();
        mobs = new ArrayList<>();
        enchantment_list = new ArrayList<>();
        main_enchantment_list = new ArrayList<>();
        potion_list = new ArrayList<>();
        //instruments_list = new ArrayList<>();

        items_init = items_init_p;
        mobs_init = mobs_init_p;

        initialize_enchantedBooks();
        initialize_potions();
        initialize_items();
        initialize_mobs();
    }

    private void initialize_items() {
        for (String s : items_init) {
            items.add(new item(s));
        }

        //Remove Null Items
        null_remove();

        //Initialize the books/potions/horns
        metaList_initialize();
    }

    private void initialize_mobs() {
        for (String s : mobs_init) {
            mobs.add(new item(s + "_spawn_egg"));
        }

        //Removes the null items
        null_remove_mobs();

        //Renames the items to remove the "spawn_egg"
        rename_mobs();
    }

    private void initialize_enchantedBooks() {
        for (Enchantment e : Enchantment.values()) {
            set_enchants(e.getKey().getKey(), e.getMaxLevel());
        }
    }

    private void initialize_potions() {
        for (PotionType p : PotionType.values()) {
            potion_list.add(new potions(p.name().toLowerCase()));
        }

        null_remove_potions();

        for (potions p : potion_list) {
            p.name = change_potion_names(p.name);
        }
    }

    private String change_potion_names(String name) {
        if (name.equalsIgnoreCase("jump")) {name = "leaping";}
        if (name.equalsIgnoreCase("instant_heal")) {name = "healing";}
        if (name.equalsIgnoreCase("instant_damage")) {name = "harming";}
        if (name.equalsIgnoreCase("regen")) {name = "regeneration";}
        if (name.equalsIgnoreCase("speed")) {name = "swiftness";}
        return name;
    }

    private void set_enchants(String enchant, int lvl) {
        for (Enchantment e : Enchantment.values()) {
            String key = e.getKey().getKey();
            if (enchant.equalsIgnoreCase(key)) {
                if (lvl == 1) {
                    enchantment_list.add(new enchantments(enchant, e, 1));
                } else {
                    for (int i = 1; i <= lvl; i++) {
                        enchantment_list.add(new enchantments(enchant + "_" + i, e, i));
                    }
                    enchantment_list.add(new enchantments(enchant, e, -1));
                }
            }
        }
    }

    //Sets the enchanted books, potions, and goat horns to have their correct metadata
    private void metaList_initialize() {
        //Enchanted books
        item temp;
        EnchantmentStorageMeta book_meta;
        for (enchantments i : enchantment_list) {
            temp = new item("enchanted_book");
            temp.set_name(i.name);
            book_meta = (EnchantmentStorageMeta) temp.item_meta;
            book_meta.addStoredEnchant(i.enchant, i.level, true);
            temp.item_stack.setItemMeta(book_meta);
            temp.item_meta = book_meta;
            items.add(temp);
        }
        //Potions
        PotionMeta potion_meta;
        for (potions i : potion_list) {
            //Potions
            temp = new item("potion");
            temp.set_name("potion_of_" + i.name);
            potion_meta = (PotionMeta) temp.item_meta;
            main.getPlugin().version.setPotionMeta(potion_meta, i.effect.get(0));
            temp.item_stack.setItemMeta(potion_meta);
            temp.item_meta = potion_meta;
            items.add(temp);

            //Splash Potions
            temp = new item("splash_potion");
            temp.set_name("splash_potion_of_" + i.name);
            potion_meta = (PotionMeta) temp.item_meta;
            main.getPlugin().version.setPotionMeta(potion_meta, i.effect.get(0));
            temp.item_stack.setItemMeta(potion_meta);
            temp.item_meta = potion_meta;
            items.add(temp);

            //Splash Potions
            temp = new item("lingering_potion");
            temp.set_name("lingering_potion_of_" + i.name);
            potion_meta = (PotionMeta) temp.item_meta;
            main.getPlugin().version.setPotionMeta(potion_meta, i.effect.get(0));
            temp.item_stack.setItemMeta(potion_meta);
            temp.item_meta = potion_meta;
            items.add(temp);

            //Tipped Arrows
            temp = new item("tipped_arrow");
            temp.set_name("tipped_arrow_of_" + i.name);
            potion_meta = (PotionMeta) temp.item_meta;
            main.getPlugin().version.setPotionMeta(potion_meta, i.effect.get(0));
            temp.item_stack.setItemMeta(potion_meta);
            temp.item_meta = potion_meta;
            items.add(temp);
        }
        //Goat Horns
        main.getPlugin().version.initializeHorns(items);
    }

    //Removes the null items in the list
    private void null_remove() {
        int i = 0;
        while (i < items.size()) {
            if (items.get(i).item_type.isAir()) {
                items.remove(i);
            } else {
                i++;
            }
        }
    }

    //Removes the null mobs in the list
    private void null_remove_mobs() {
        int i = 0;
        while (i < mobs.size()) {
            if (mobs.get(i).item_type.isAir()) {
                mobs.remove(i);
            } else {
                i++;
            }
        }
    }

    //Removes the null potions in the list
    private void null_remove_potions() {
        int i = 0;
        while (i < potion_list.size()) {
            if (potion_list.get(i).effect.size() == 0) {
                potion_list.remove(i);
            } else {
                i++;
            }
        }
    }

    //Renames the mobs to remove "spawn_egg"
    private void rename_mobs() {
        for (int i=0; i<mobs.size(); i++) {
            mobs.get(i).set_name(mobs.get(i).item_name.substring(0, mobs.get(i).item_name.length()-10));
        }
    }
}

