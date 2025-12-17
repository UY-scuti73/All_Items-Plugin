package xyz.quazaros;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class version {
    public double mc_version;

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

    public ArrayList<String> modifyStringList(ArrayList<String> string_list) {
        if (mc_version < 21) {
            string_list.remove("bogged");
            string_list.remove("breeze");
        }
        if (mc_version < 21.11) {
            string_list.remove("zombie_horse");
        }
        return string_list;
    }
}
