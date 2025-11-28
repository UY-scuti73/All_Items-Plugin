package xyz.quazaros.util.meta.itemMeta;

import org.bukkit.enchantments.Enchantment;

public class enchantments {
    public String name;
    public Enchantment enchant;
    public int level;

    public enchantments(String name, Enchantment enchant, int level) {
        this.name = name;
        this.enchant = enchant;
        this.level = level;
    }
}
