package xyz.quazaros.util.files.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class lang {
    public String commandDisabled;
    public String noPermission;
    public String itemDisabled;
    public String mobDisabled;
    public String enterPlayer;
    public String enterItem;
    public String enterMob;
    public String enterCommand;
    public String itemNotFound;
    public String mobNotFound;
    public String playerNotFound;
    public String noItemsFound;
    public String youHaveNoItems;
    public String subAlreadyFound;
    public String subNotFound;

    public String itemSubmitted;
    public String itemSubNotInList;
    public String mobKilled;
    public String subBy;
    public String alreadyFound;
    public String alreadyFoundBy;
    public String hasBeenFound;
    public String hasBeenFoundBy;
    public String hasNotBeenFound;
    public String progress;
    public String itemSettings;
    public String mobSettings;
    public String areset;
    public String mreset;
    public String resetCancel;
    public String submit;
    public String unsubmit;
    public String admin;

    public String itemListMenu;
    public String mobListMenu;
    public String itemPersonalSuffix;
    public String mobPersonalSuffix;
    public String lastPage;
    public String nextPage;
    public String filterItems;
    public String leaderboard;
    public String menuItemFound;
    public String menuItemNotFound;
    public String byPlayer;
    public String atTime;
    public String mainButton;
    public String personalButton;
    public String playerList;
    public String mobPlayerList;
    public String personalPlayerList;
    public String mobPersonalPlayerList;

    public String congrats;
    public String allItems;
    public String allMobs;
    public String completeItemSuffix;
    public String completeMobSuffix;

    public String placeholderTrue;
    public String placeholderFalse;
    public String placeholderNotFound;

    public final ChatColor colorGood;
    public final ChatColor colorBad;
    public final ChatColor colorDom;
    public final ChatColor colorSec;
    public final ChatColor colorHigh;
    public final ChatColor colorWar;

    public lang() {
        colorGood = ChatColor.GREEN;
        colorBad = ChatColor.RED;
        colorDom = ChatColor.LIGHT_PURPLE;
        colorSec = ChatColor.AQUA;
        colorHigh = ChatColor.GOLD;
        colorWar = ChatColor.DARK_RED;
    }

    public void initialize(YamlConfiguration config) {
        commandDisabled = config.getString("sendMessages.error.commandDisabled");
        noPermission = config.getString("sendMessages.error.noPermission");
        itemDisabled = config.getString("sendMessages.error.itemDisabled");
        mobDisabled = config.getString("sendMessages.error.mobDisabled");
        enterPlayer = config.getString("sendMessages.error.enterPlayer");
        enterItem = config.getString("sendMessages.error.enterItem");
        enterMob = config.getString("sendMessages.error.enterMob");
        enterCommand = config.getString("sendMessages.error.enterCommand");
        itemNotFound = config.getString("sendMessages.error.itemNotFound");
        mobNotFound = config.getString("sendMessages.error.mobNotFound");
        playerNotFound = config.getString("sendMessages.error.playerNotFound");
        noItemsFound = config.getString("sendMessages.error.noItemsFound");
        youHaveNoItems = config.getString("sendMessages.error.youHaveNoItems");
        alreadyFound = config.getString("sendMessages.error.alreadyFound");
        alreadyFoundBy = config.getString("sendMessages.error.alreadyFoundBy");
        subAlreadyFound = config.getString("sendMessages.error.subAlreadyFound");
        subNotFound = config.getString("sendMessages.error.subNotFound");

        itemSubmitted = config.getString("sendMessages.other.itemSubmitted");
        itemSubNotInList = config.getString("sendMessages.other.itemSubNotInList");
        mobKilled = config.getString("sendMessages.other.mobKilled");
        subBy = config.getString("sendMessages.other.subBy");
        hasBeenFound = config.getString("sendMessages.other.hasBeenFound");
        hasBeenFoundBy = config.getString("sendMessages.other.hasBeenFoundBy");
        hasNotBeenFound = config.getString("sendMessages.other.hasNotBeenFound");
        itemSettings = config.getString("sendMessages.other.itemSettings");
        mobSettings = config.getString("sendMessages.other.mobSettings");
        areset = config.getString("sendMessages.other.areset");
        mreset = config.getString("sendMessages.other.mreset");
        resetCancel = config.getString("sendMessages.other.resetCancel");
        submit = config.getString("sendMessages.other.submit");
        unsubmit = config.getString("sendMessages.other.unsubmit");
        admin = config.getString("sendMessages.other.admin");

        itemListMenu = config.getString("gui.itemListMenu");
        mobListMenu = config.getString("gui.mobListMenu");
        itemPersonalSuffix = config.getString("gui.itemPersonalSuffix");
        mobPersonalSuffix = config.getString("gui.mobPersonalSuffix");
        lastPage = config.getString("gui.lastPage");
        nextPage = config.getString("gui.nextPage");
        progress = config.getString("gui.progress");
        filterItems = config.getString("gui.filterItems");
        leaderboard = config.getString("gui.leaderboard");
        menuItemFound = config.getString("gui.menuItemFound");
        menuItemNotFound = config.getString("gui.menuItemNotFound");
        byPlayer = config.getString("gui.foundBy");
        atTime = config.getString("gui.foundAt");
        mainButton = config.getString("gui.mainButton");
        personalButton = config.getString("gui.personalButton");
        playerList = config.getString("gui.playerList");
        mobPlayerList = config.getString("gui.mobPlayerList");
        personalPlayerList = config.getString("gui.personalPlayerList");
        mobPersonalPlayerList = config.getString("gui.mobPersonalPlayerList");

        congrats = config.getString("completed.congrats");
        allItems = config.getString("completed.allItems");
        allMobs = config.getString("completed.allMobs");
        completeItemSuffix = config.getString("completed.personalItemSuffix");
        completeMobSuffix = config.getString("completed.personalMobSuffix");

        placeholderTrue = config.getString("placeholderAPI.true");
        placeholderFalse = config.getString("placeholderAPI.false");
        placeholderNotFound = config.getString("placeholderAPI.notFound");

        check();
    }

    private void check() {
        if (itemSubmitted.equals(itemSubNotInList)) {
            itemSubNotInList += "1";
        }
    }
}
