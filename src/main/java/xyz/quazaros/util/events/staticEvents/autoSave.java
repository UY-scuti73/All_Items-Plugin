package xyz.quazaros.util.events.staticEvents;

import xyz.quazaros.main;

public class autoSave {
    //Auto Saves When The World Does
    public static void handle_autoSave() {
        main Main = main.getPlugin();
        if (Main.data.general_autoSave) {
            Main.save_files(false);
        }
    }
}
