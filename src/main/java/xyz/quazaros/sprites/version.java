package xyz.quazaros.sprites;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class version {

    public double mc_version;

    ArrayList<ItemFlag> itemFlags;

    public version() {
        mc_version = get_version();
    }

    //Gets the version of minecraft
    public double get_version() {
        String versionInfo = Bukkit.getVersion();

        Pattern pattern = Pattern.compile("\\(MC: ([\\d.]+)\\)");
        Matcher matcher = pattern.matcher(versionInfo);

        double ver = 1.0;
        if (matcher.find()) {
            String mcVersion = matcher.group(1);
            String[] parts = mcVersion.split("\\.");

            if (parts.length >= 2) {
                String major = parts[1]; // "21"
                String minor = parts.length >= 3 ? parts[2] : "0";
                ver = Double.parseDouble(major + "." + minor);
            }
        }

        return ver;
    }

    public boolean readyForSprites() {
        return isGreater(21.9);
    }

    public boolean isGreater(double a) {
        String verStr = Double.toString(mc_version);
        String aStr = Double.toString(a);

        if (verStr.length() > aStr.length()) {
            return true;
        } else if (verStr.length() < aStr.length()) {
            return false;
        } else {
            return mc_version >= a;
        }
    }
}
