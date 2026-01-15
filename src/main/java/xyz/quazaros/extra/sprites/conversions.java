package xyz.quazaros.extra.sprites;

import static xyz.quazaros.util.main.mainVariables.getVariables;

public class conversions {
    public static String toNBT(String tempColor, String prefix, String sprite, String suffix) {
        String temp;

        if (!getVariables().data.general_sprites) {
            if (suffix == null) {
                temp = "[{\"color\":\"" + tempColor + "\",\"text\":\"" + prefix + "\"}]";
            } else {
                temp =
                    "[{\"color\":\"" + tempColor + "\",\"text\":\"" + prefix + "\"}" +
                    ",{\"color\":\"" + tempColor + "\",\"text\":\"" + " " + suffix + "\"}]";
            }
        } else {
            if (suffix == null) {
                temp =
                    "[{\"color\":\"" + tempColor + "\",\"text\":\"" + prefix + "\"}" +
                    ",{\"color\":\"white\",\"text\":\" (\"}," + sprite + ",{\"color\":\"white\",\"text\":\") \"}]";
            } else {
                temp =
                    "[{\"color\":\"" + tempColor + "\",\"text\":\"" + prefix + "\"}" +
                    ",{\"color\":\"white\",\"text\":\" (\"}," + sprite + ",{\"color\":\"white\",\"text\":\") \"}" +
                    ",{\"color\":\"" + tempColor + "\",\"text\":\"" + suffix + "\"}]";
            }
        }

        return temp;
    }
}
