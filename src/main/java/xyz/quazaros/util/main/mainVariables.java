package xyz.quazaros.util.main;

import xyz.quazaros.extra.timer.timer;
import xyz.quazaros.extra.version.version;
import xyz.quazaros.structures.items.itemList;
import xyz.quazaros.structures.items.itemSprite;
import xyz.quazaros.structures.meta.metaList;
import xyz.quazaros.structures.player.playerList;
import xyz.quazaros.util.commands.commands;
import xyz.quazaros.util.commands.tabComplete;
import xyz.quazaros.util.files.config.config;
import xyz.quazaros.util.files.config.lang;
import xyz.quazaros.util.files.file;

import java.util.ArrayList;

public class mainVariables {
    public file file;
    public commands commands;
    public tabComplete tabComplete;
    public metaList meta_list;
    public config data;
    public lang lang;
    public version version;
    public timer timer;
    public playerList player_list;
    public itemList emptyItemList;
    public itemList emptyMobList;
    public itemSprite sprite;

    public itemList all_items;
    public itemList all_mobs;

    public ArrayList<String> commandNames;
}
