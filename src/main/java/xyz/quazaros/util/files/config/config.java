package xyz.quazaros.util.files.config;

import org.bukkit.configuration.file.YamlConfiguration;

public class config {
    public boolean general_global;
    public boolean general_personal;
    public boolean general_others;
    public boolean general_listPriority;
    public boolean general_announceSend;
    public boolean general_progress;
    public boolean general_check;
    public boolean general_player;
    public boolean general_settings;
    public boolean general_mainCompletion;
    public boolean general_personalCompletion;
    public boolean general_autoSave;
    public String general_mainItemCompletion_command;
    public String general_mainMobCompletion_command;
    public String general_personalItemCompletion_command;
    public String general_personalMobCompletion_command;
    public boolean general_sprites;
    public boolean item_toggle;
    public String item_file;
    public boolean item_subtraction;
    public boolean item_autoCollect;
    public boolean mob_toggle;
    public String mob_file;
    
    public void initialize(YamlConfiguration config) {
        general_global = (config.getString("general.global").equalsIgnoreCase("true"));
        general_personal = (config.getString("general.personal").equalsIgnoreCase("true"));
        general_others = (config.getString("general.others_lists").equalsIgnoreCase("true"));
        general_listPriority = config.getString("general.list_priority").equalsIgnoreCase("1");
        general_announceSend = config.getString("general.announce_send").equalsIgnoreCase("true");
        general_progress = config.getString("general.progress").equalsIgnoreCase("true");
        general_check = config.getString("general.check").equalsIgnoreCase("true");
        general_player = config.getString("general.player").equalsIgnoreCase("true");
        general_settings = config.getString("general.settings").equalsIgnoreCase("true");
        general_mainCompletion = config.getString("general.completion.main_completion").equalsIgnoreCase("true");
        general_personalCompletion = config.getString("general.completion.personal_completion").equalsIgnoreCase("true");
        general_mainItemCompletion_command = config.getString("general.completion.main_item_completion_command");
        general_mainMobCompletion_command = config.getString("general.completion.main_mob_completion_command");
        general_personalItemCompletion_command = config.getString("general.completion.personal_item_completion_command");
        general_personalMobCompletion_command = config.getString("general.completion.personal_mob_completion_command");
        general_autoSave = config.getString("general.auto_save").equalsIgnoreCase("true");
        general_sprites = config.getString("general.sprites").equalsIgnoreCase("true");
        item_toggle = (config.getString("all_items.toggle").equalsIgnoreCase("true"));
        item_file = config.getString("all_items.file");
        item_subtraction = (config.getString("all_items.subtraction").equalsIgnoreCase("true"));
        item_autoCollect = (config.getString("all_items.auto_collect").equalsIgnoreCase("true"));
        mob_toggle = (config.getString("all_mobs.toggle").equalsIgnoreCase("true"));
        mob_file = config.getString("all_mobs.file");
    }
}
