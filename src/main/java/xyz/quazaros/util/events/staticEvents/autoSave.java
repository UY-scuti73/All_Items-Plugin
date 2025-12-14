package xyz.quazaros.util.events.staticEvents;

import xyz.quazaros.main;

import static xyz.quazaros.util.main.initialize.save_files;

public class autoSave {
    //Auto Saves When The World Does
    public static void handle_autoSave() {
        main Main = main.getPlugin();
        if (Main.data.general_autoSave) {
            save_files(false);
        }
    }
}
