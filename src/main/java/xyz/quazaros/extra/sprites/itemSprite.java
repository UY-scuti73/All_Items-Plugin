package xyz.quazaros.extra.sprites;

import org.bukkit.Material;

import static xyz.quazaros.util.main.mainVariables.getVariables;

public class itemSprite {
    spriteExceptions exceptions;

    public itemSprite() {
        exceptions = new spriteExceptions();
    }

    public String getSprite(Material material) {
        if (material.name().equalsIgnoreCase("decorated_pot")) {
            return "{\"color\":\"white\",\"type\":\"object\",\"sprite\":\"minecraft:entity/decorated_pot/prize_pottery_pattern\",\"atlas\":\"minecraft:decorated_pot\"}";
        }

        itemSpriteData tempData = getSpriteData(material);

        String ret;

        if (getVariables().version.isGreater(21.11)) {
            ret = "{\"color\":\"white\",\"type\":\"object\",\"sprite\":\"minecraft:" + tempData.type + "/" + tempData.path + "\",\"atlas\":\"minecraft:" + tempData.type + "s\"}";
        } else {
            if (tempData.type.equalsIgnoreCase("block")) {
                ret = "{\"color\":\"white\",\"type\":\"object\",\"sprite\":\"minecraft:" + tempData.type + "/" + tempData.path + "\",\"atlas\":\"minecraft:" + tempData.type + "s\"}";
            } else {
                ret = "{\"color\":\"white\",\"sprite\":\"minecraft:item/" + tempData.path + "\"}";
            }
        }

        return ret;
    }

    private itemSpriteData getSpriteData(Material type) {
        String name = type.name().toLowerCase();
        itemSpriteData tempData;

        if (type.isBlock()) {
            tempData = new itemSpriteData(name, "block");
        } else if (type.isItem()) {
            tempData = new itemSpriteData(name, "item");
        } else {
            tempData = new itemSpriteData(name, "block");
        }

        if (exceptions.sprite_exception_map.containsKey(name)) {
            tempData.path = exceptions.sprite_exception_map.get(name);
        }

        if (exceptions.path_change_map.contains(name)) {
            tempData.type = tempData.type.equals("block") ? "item" : "block";
        }

        return tempData;
    }
}
