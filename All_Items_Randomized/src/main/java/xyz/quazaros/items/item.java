package xyz.quazaros.items;

import org.bukkit.Material;

public class item {
    public String name;
    public String display_name;
    public Material type;

    public item(String name) {
        this.name = name;
        this.display_name = camel_case(name);
        this.type = getType(name);
    }

    private Material getType(String name) {
        for (Material m : Material.values()) {
            if (m.name().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    private String camel_case(String str) {
        if (str == null) {return "";}
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == '_') {
                str = str.substring(0, i) + " " + str.substring(i + 1);
            }
            if (str.charAt(i - 1) == ' ') {
                str = str.substring(0, i) + str.substring(i, i + 1).toUpperCase() + str.substring(i + 1);
            }
        }
        if (str.isEmpty()) {return "";}
        str = str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
        return str;
    }
}
