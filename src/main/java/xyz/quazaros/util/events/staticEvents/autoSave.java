package xyz.quazaros.util.events.staticEvents;

import xyz.quazaros.main;
import xyz.quazaros.util.main.mainVariables;

import static xyz.quazaros.util.main.initialize.save_files;
import static xyz.quazaros.util.main.mainVariables.getVariables;

public class autoSave {
    //Auto Saves When The World Does
    public static void handle_autoSave() {
        mainVariables Main = getVariables();
        if (Main.data.general_autoSave) {
            save_files(false);
        }
    }
}
