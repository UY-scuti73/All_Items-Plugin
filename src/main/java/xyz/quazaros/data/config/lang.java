package xyz.quazaros.data.config;

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
    public String mobKilledBy;
    public String alreadyFound;
    public String alreadyFoundBy;
    public String hasBeenFound;
    public String hasBeenFoundBy;
    public String hasNotBeenFound;
    public String progress;
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

    public String congrats;
    public String allItems;
    public String allMobs;
    public String completeItemSuffix;
    public String completeMobSuffix;

    public ChatColor good;
    public ChatColor bad;
    public ChatColor dom;
    public ChatColor sec;

    public lang() {
        good = ChatColor.GREEN;
        bad = ChatColor.RED;
        dom = ChatColor.LIGHT_PURPLE;
        sec = ChatColor.AQUA;
    }

    public void initialize(YamlConfiguration config) {
        commandDisabled = config.getString("sendMessages.error.commandDisabled");
        noPermission = config.getString("sendMessages.error.noPermission");
        itemDisabled = config.getString("sendMessages.error.itemDisabled");
        mobDisabled = config.getString("sendMessages.error.mobDisabled");
        enterPlayer = config.getString("sendMessages.error.enterPlayer");
        enterItem = config.getString("sendMessages.error.enterItem");
        enterMob = config.getString("sendMessages.error.enterMob");
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
        mobKilledBy = config.getString("sendMessages.other.mobKilledBy");
        hasBeenFound = config.getString("sendMessages.other.hasBeenFound");
        hasBeenFoundBy = config.getString("sendMessages.other.hasBeenFoundBy");
        hasNotBeenFound = config.getString("sendMessages.other.hasNotBeenFound");
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

        congrats = config.getString("completed.congrats");
        allItems = config.getString("completed.allItems");
        allMobs = config.getString("completed.allMobs");
        completeItemSuffix = config.getString("completed.personalItemSuffix");
        completeMobSuffix = config.getString("completed.personalMobSuffix");
    }
}
