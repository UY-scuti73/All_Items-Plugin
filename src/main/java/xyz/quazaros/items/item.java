package xyz.quazaros.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class item {
    public Material item_type;
    public String item_name;
    public String item_display_name;
    public boolean isFound;

    public ItemStack item;

    public item(Material material) {
        item_type = material;
        item_name = material.toString().toLowerCase();
        item_display_name = camel_case(item_name);
        isFound = false;

        item = new ItemStack(item_type);
        ItemMeta item_meta = item.getItemMeta();
        item_meta.setDisplayName(ChatColor.RED + item_display_name);
        item_meta.setEnchantmentGlintOverride(false);
        item.setItemMeta(item_meta);
    }

    public void setEnchant(Enchantment enchantment) {
        item_name = enchantment.getKey().getKey();
        item_display_name = camel_case(item_name);

        EnchantmentStorageMeta item_meta = (EnchantmentStorageMeta) item.getItemMeta();
        item_meta.setDisplayName(ChatColor.RED + item_display_name);
        item_meta.addStoredEnchant(enchantment, enchantment.getMaxLevel(), true);
        item.setItemMeta(item_meta);
    }

    public void setPotion(PotionType potion) {
        item_name = potion.toString().toLowerCase();
        item_display_name = camel_case(item_name);

        PotionMeta item_meta = (PotionMeta) item.getItemMeta();
        item_meta.setDisplayName(ChatColor.RED + item_display_name);
        item_meta.setBasePotionType(potion);
        item.setItemMeta(item_meta);
    }

    public void setInstrument(MusicInstrument instrument) {
        item_name = instrument.getKey().getKey();
        item_display_name = camel_case(item_name);

        MusicInstrumentMeta item_meta = (MusicInstrumentMeta) item.getItemMeta();
        item_meta.setDisplayName(ChatColor.RED + item_display_name);
        item_meta.setInstrument(instrument);
        item.setItemMeta(item_meta);
    }

    public void submit() {
        ItemMeta item_meta = item.getItemMeta();
        item_meta.setDisplayName(ChatColor.GREEN + item_display_name);
        item_meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(item_meta);

        isFound = true;
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
