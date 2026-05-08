package xyz.quazaros.items;

import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

import static xyz.quazaros.file.retrieveStringList;

public class itemList {
    public ArrayList<item> item_list;

    public itemList() {
        item_list = new ArrayList<>();

        ArrayList<String> item_string_list = retrieveStringList();

        for (String item_string : item_string_list) {
            item tempItem = getItemFromString(item_string);
            if (tempItem != null) {item_list.add(tempItem);}
        }
    }

    private item getItemFromString(String item_string) {
        for (Material material : Material.values()) {
            if (item_string.equalsIgnoreCase(material.toString())) {
                return new item(material);
            }
        }

        item tempItem;

        for (Enchantment enchantment : Enchantment.values()) {
            if (item_string.equalsIgnoreCase(enchantment.getKey().getKey())) {
                tempItem = new item(Material.ENCHANTED_BOOK);
                tempItem.setEnchant(enchantment);
                return tempItem;
            }
        }

        for (PotionType potion_type : PotionType.values()) {
            if (item_string.equalsIgnoreCase(potion_type.toString())) {
                tempItem = new item(Material.POTION);
                tempItem.setPotion(potion_type);
                return tempItem;
            }
        }

        for (MusicInstrument instrument : MusicInstrument.values()) {
            if ((item_string+"_goat_horn").equalsIgnoreCase(instrument.getKey().getKey())) {
                tempItem = new item(Material.GOAT_HORN);
                tempItem.setInstrument(instrument);
                return tempItem;
            }
        }

        return null;
    }

    public item submitItem(ItemStack itemStack) {
        int index = getIndex(itemStack);
        if (index <= -1) {return null;}

        item tempItem = item_list.get(index);
        if (tempItem == null || tempItem.isFound) {return null;}

        tempItem.submit();
        return tempItem;
    }

    public int getIndex(ItemStack itemStack) {
        for (int i = 0; i < item_list.size(); i++) {
            item tempItem = item_list.get(i);

            if (itemStack.getType() == Material.ENCHANTED_BOOK && tempItem.item_type == Material.ENCHANTED_BOOK) {
                EnchantmentStorageMeta curItemMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                EnchantmentStorageMeta tempItemMeta = (EnchantmentStorageMeta) tempItem.item.getItemMeta();
                if (curItemMeta.getStoredEnchants().equals(tempItemMeta.getStoredEnchants())) {
                    return i;
                }
            }

            else if (itemStack.getType() == Material.POTION && tempItem.item_type == Material.POTION) {
                PotionMeta curItemMeta = (PotionMeta) itemStack.getItemMeta();
                PotionMeta tempItemMeta = (PotionMeta) tempItem.item.getItemMeta();
                if (curItemMeta.getBasePotionType().getEffectType().equals(tempItemMeta.getBasePotionType().getEffectType())) {
                    return i;
                }
            }

            else if (itemStack.getType() == Material.GOAT_HORN && tempItem.item_type == Material.GOAT_HORN) {
                MusicInstrumentMeta curItemMeta = (MusicInstrumentMeta) itemStack.getItemMeta();
                MusicInstrumentMeta tempItemMeta = (MusicInstrumentMeta) tempItem.item.getItemMeta();
                if (curItemMeta.getInstrument().equals(tempItemMeta.getInstrument())) {
                    return i;
                }
            }

            else {
                if (itemStack.getType() == tempItem.item_type) {
                    return i;
                }
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
