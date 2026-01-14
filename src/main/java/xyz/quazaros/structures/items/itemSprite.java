package xyz.quazaros.structures.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import xyz.quazaros.main;

import java.util.ArrayList;

import static xyz.quazaros.util.main.mainVariables.getVariables;

public class itemSprite {
    ArrayList<String> side_sprites;

    Inventory inventory;

    public itemSprite() {
        side_sprites = new ArrayList<>();
        inventory = Bukkit.createInventory(null, 9, "Sprites");
    }

    public void initialize() {
        side_sprites = getVariables().file.get_from_file("Sprites/side.txt");
    }

    public static String getSprite(Material material) {
        String text = material.isItem() ? "item": "sprite";
        String name = material.name().toLowerCase();

        return "{\"color\":\"white\",\"type\":\"object\",\"sprite\":\"minecraft:" + text + "/" + name + "\",\"atlas\":\"minecraft:" + text + "s\"}";
    }
}
