package xyz.quazaros.structures.meta.itemMeta;

import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public class potions {
    public String name;
    public ArrayList<PotionType> effect;

    public potions(String name) {
        this.name = name;
        ArrayList<String> temp = new ArrayList<>();
        temp.add(name);
        temp.add("long_" + name);
        temp.add("strong_" + name);
        effect = to_potion(temp);
    }

    private ArrayList<PotionType> to_potion(List<String> strings) {
        ArrayList<PotionType> temp = new ArrayList<>();
        for (String s : strings) {
            for (PotionType p : PotionType.values()) {
                if (p.toString().equalsIgnoreCase(s)) {
                    temp.add(p);
                }
            }
        }
        return temp;
    }
}
