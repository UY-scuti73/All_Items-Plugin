package xyz.quazaros.data.meta;

import org.bukkit.Bukkit;
import org.bukkit.MusicInstrument;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.PotionMeta;
import xyz.quazaros.data.items.item;
import xyz.quazaros.file;
import xyz.quazaros.main;

import java.util.ArrayList;

public class metaList {

    file File;

    public ArrayList<item> items;
    public ArrayList<item> mobs;

    public ArrayList<enchantments> enchantment_list;
    public ArrayList<ArrayList<enchantments>> main_enchantment_list;
    public ArrayList<potions> potion_list;
    public ArrayList<instruments> instrument_list;

    ArrayList<String> items_init;
    ArrayList<String> mobs_init;

    int version;

    public metaList(ArrayList<String> items_init_p, ArrayList<String> mobs_init_p) {
        File = main.getPlugin().file;

        items = new ArrayList<>();
        mobs = new ArrayList<>();
        enchantment_list = new ArrayList<>();
        main_enchantment_list = new ArrayList<>();
        potion_list = new ArrayList<>();
        instrument_list = new ArrayList<>();

        version = get_version();

        items_init = items_init_p;
        mobs_init = mobs_init_p;

        initialize_enchantedBooks();
        initialize_potions();
        initialize_horns();
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
        set_enchants("mending", 1);
        set_enchants("unbreaking", 3);
        set_enchants("sharpness", 5);
        set_enchants("smite", 5);
        set_enchants("bane_of_arthropods", 5);
        set_enchants("looting", 3);
        set_enchants("fire_aspect", 2);
        set_enchants("sweeping_edge", 3);
        set_enchants("knockback", 2);
        set_enchants("efficiency", 5);
        set_enchants("fortune", 3);
        set_enchants("silk_touch", 1);
        set_enchants("protection", 4);
        set_enchants("fire_protection", 4);
        set_enchants("blast_protection", 4);
        set_enchants("projectile_protection", 4);
        set_enchants("feather_falling", 4);
        set_enchants("thorns", 3);
        set_enchants("aqua_affinity", 1);
        set_enchants("respiration", 3);
        set_enchants("depth_strider", 3);
        set_enchants("frost_walker", 2);
        set_enchants("soul_speed", 3);
        set_enchants("swift_sneak", 3);
        set_enchants("power", 5);
        set_enchants("punch", 2);
        set_enchants("flame", 1);
        set_enchants("infinity", 1);
        set_enchants("piercing", 4);
        set_enchants("multishot", 1);
        set_enchants("quick_charge", 3);
        set_enchants("lure", 3);
        set_enchants("luck_of_the_sea", 3);
        set_enchants("impaling", 5);
        set_enchants("loyalty", 3);
        set_enchants("riptide", 3);
        set_enchants("channeling", 1);
        set_enchants("density", 5);
        set_enchants("breach", 4);
        set_enchants("wind_burst", 3);
        set_enchants("binding_curse", 1);
        set_enchants("vanishing_curse", 1);
    }

    private void initialize_potions() {
        potion_list.add(new potions("water"));
        potion_list.add(new potions("thick"));
        potion_list.add(new potions("mundane"));
        potion_list.add(new potions("awkward"));
        potion_list.add(new potions("strength"));
        potion_list.add(new potions("weakness"));
        potion_list.add(new potions("healing"));
        potion_list.add(new potions("harming"));
        potion_list.add(new potions("regeneration"));
        potion_list.add(new potions("poison"));
        potion_list.add(new potions("oozing"));
        potion_list.add(new potions("infested"));
        potion_list.add(new potions("swiftness"));
        potion_list.add(new potions("slowness"));
        potion_list.add(new potions("leaping"));
        potion_list.add(new potions("slow_falling"));
        potion_list.add(new potions("fire_resistance"));
        potion_list.add(new potions("invisibility"));
        potion_list.add(new potions("night_vision"));
        potion_list.add(new potions("water_breathing"));
        potion_list.add(new potions("turtle_master"));
        potion_list.add(new potions("weaving"));
        potion_list.add(new potions("wind_charged"));
        potion_list.add(new potions("luck"));

        null_remove_potions();
    }

    private void initialize_horns() {
        instrument_list.add(new instruments("ponder", MusicInstrument.PONDER_GOAT_HORN));
        instrument_list.add(new instruments("sing", MusicInstrument.SING_GOAT_HORN));
        instrument_list.add(new instruments("seek", MusicInstrument.SEEK_GOAT_HORN));
        instrument_list.add(new instruments("feel", MusicInstrument.FEEL_GOAT_HORN));
        instrument_list.add(new instruments("admire", MusicInstrument.ADMIRE_GOAT_HORN));
        instrument_list.add(new instruments("call", MusicInstrument.CALL_GOAT_HORN));
        instrument_list.add(new instruments("yearn", MusicInstrument.YEARN_GOAT_HORN));
        instrument_list.add(new instruments("dream", MusicInstrument.DREAM_GOAT_HORN));
    }

    private void set_enchants(String enchant, int lvl) {
        for (Enchantment e : Enchantment.values()) {
            String s = e.toString().substring(27, e.toString().length()-1);
            if (enchant.equalsIgnoreCase(s)) {
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
            temp.set_name("potion_of_"+i.name);
            potion_meta = (PotionMeta) temp.item_meta;
            potion_meta.setBasePotionType(i.effect.get(0));
            temp.item_stack.setItemMeta(potion_meta);
            temp.item_meta = potion_meta;
            items.add(temp);

            //Splash Potions
            temp = new item("splash_potion");
            temp.set_name("splash_potion_of_"+i.name);
            potion_meta = (PotionMeta) temp.item_meta;
            potion_meta.setBasePotionType(i.effect.get(0));
            temp.item_stack.setItemMeta(potion_meta);
            temp.item_meta = potion_meta;
            items.add(temp);

            //Splash Potions
            temp = new item("lingering_potion");
            temp.set_name("lingering_potion_of_"+i.name);
            potion_meta = (PotionMeta) temp.item_meta;
            potion_meta.setBasePotionType(i.effect.get(0));
            temp.item_stack.setItemMeta(potion_meta);
            temp.item_meta = potion_meta;
            items.add(temp);

            //Tipped Arrows
            temp = new item("tipped_arrow");
            temp.set_name("tipped_arrow_of_"+i.name);
            potion_meta = (PotionMeta) temp.item_meta;
            potion_meta.setBasePotionType(i.effect.get(0));
            temp.item_stack.setItemMeta(potion_meta);
            temp.item_meta = potion_meta;
            items.add(temp);
        }
        //Goat Horns
        MusicInstrumentMeta instrument_meta;
        for (instruments i : instrument_list) {
            temp = new item("goat_horn");
            temp.set_name(i.name);
            instrument_meta = (MusicInstrumentMeta) temp.item_meta;
            instrument_meta.setInstrument(i.instrument);
            temp.item_stack.setItemMeta(instrument_meta);
            temp.item_meta = instrument_meta;
            items.add(temp);
        }
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

    //Gets the version of minecraft
    public int get_version() {
        String version = Bukkit.getVersion();
        int start = 0;
        int stop = 0;
        for (int i=0; i < version.length(); i++) {
            if (version.charAt(i) == '.') {
                start = i;
                break;
            }
        }
        for (int i=start+1; i < version.length(); i++) {
            if (version.charAt(i) == ')' || version.charAt(i) == '.') {
                stop = i;
                break;
            }
        }
        version = version.substring(start+1, stop);
        return Integer.parseInt(version);
    }
}

