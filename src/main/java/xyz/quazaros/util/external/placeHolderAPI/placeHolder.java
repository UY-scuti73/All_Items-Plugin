package xyz.quazaros.util.external.placeHolderAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import xyz.quazaros.structures.player.player;
import xyz.quazaros.structures.player.playerSort;
import xyz.quazaros.main;

import java.util.ArrayList;
import java.util.Arrays;

public class placeHolder extends PlaceholderExpansion {

    main Main;
    String NA;

    public placeHolder() {
        Main = main.getPlugin();
        NA = Main.lang.placeholderNotFound;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "AllItems";
    }

    @Override
    public @NotNull String getAuthor() {
        return "BowserTheBoss";
    }

    @Override
    public @NotNull String getVersion() {
        return "4.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        ArrayList<String> paramList = splitKeepingBraces(params);
        String command = paramList.get(0);

        String par1 = null;
        String par2 = null;
        if (paramList.size() > 1) {par1 = paramList.get(1);}
        if (paramList.size() > 2) {par2 = paramList.get(2);}

        String ret = null;

        // Progress
        if (command.equalsIgnoreCase("itemprog")){
            ret = getProg(true, null, player.getName());
        }
        if (command.equalsIgnoreCase("mobprog")){
            ret = getProg(false, null, player.getName());
        }
        if (command.equalsIgnoreCase("itemprogpersonal")){
            ret = getProg(true, par1, player.getName());
        }
        if (command.equalsIgnoreCase("mobprogpersonal")){
            ret = getProg(false, par1, player.getName());
        }
        if (command.equalsIgnoreCase("itemprogself")){
            ret = getProg(true, player.getName(), player.getName());
        }
        if (command.equalsIgnoreCase("mobprogself")){
            ret = getProg(false, player.getName(), player.getName());
        }

        // Score
        if (command.equalsIgnoreCase("itemscore")){
            ret = getScore(true, par1, player.getName());
        }
        if (command.equalsIgnoreCase("mobscore")){
            ret = getScore(false, par2, player.getName());
        }
        if (command.equalsIgnoreCase("itemscoreself")){
            ret = getScore(true, player.getName(), player.getName());
        }
        if (command.equalsIgnoreCase("mobscoreself")){
            ret = getScore(false, player.getName(), player.getName());
        }

        // Check
        if (command.equalsIgnoreCase("itemcheck")){
            ret = checkItem(true, null, par1, player.getName());
        }
        if (command.equalsIgnoreCase("mobcheck")){
            ret = checkItem(false, null, par1, player.getName());
        }
        if (command.equalsIgnoreCase("itemcheckpersonal")){
            ret = checkItem(true, par1, par2, player.getName());
        }
        if (command.equalsIgnoreCase("mobcheckpersonal")){
            ret = checkItem(false, par1, par2, player.getName());
        }
        if (command.equalsIgnoreCase("itemcheckself")){
            ret = checkItem(true, player.getName(), par1, player.getName());
        }
        if (command.equalsIgnoreCase("mobcheckself")){
            ret = checkItem(false, player.getName(), par1, player.getName());
        }

        // Completed
        if (command.equalsIgnoreCase("itemcompleted")){
            ret = checkComplete(true, null, player.getName());
        }
        if (command.equalsIgnoreCase("mobcompleted")){
            ret = checkComplete(false, null, player.getName());
        }
        if (command.equalsIgnoreCase("itemcompletedpersonal")){
            ret = checkComplete(true, par1, player.getName());
        }
        if (command.equalsIgnoreCase("mobcompletedpersonal")){
            ret = checkComplete(false, par1, player.getName());
        }
        if (command.equalsIgnoreCase("itemcompletedself")){
            ret = checkComplete(true, player.getName(), player.getName());
        }
        if (command.equalsIgnoreCase("mobcompletedself")){
            ret = checkComplete(false, player.getName(), player.getName());
        }

        // Leaderboard
        if (command.equalsIgnoreCase("itemleaderboardplayer")){
            ret = getLeaderboard(true, false, par1, true);
        }
        if (command.equalsIgnoreCase("mobleaderboardplayer")){
            ret = getLeaderboard(false, false, par1, true);
        }
        if (command.equalsIgnoreCase("itemleaderboardscore")){
            ret = getLeaderboard(true, false, par1, false);
        }
        if (command.equalsIgnoreCase("mobleaderboardscore")){
            ret = getLeaderboard(false, false, par1, false);
        }
        if (command.equalsIgnoreCase("itemleaderboardplayerpersonal")){
            ret = getLeaderboard(true, true, par1, true);
        }
        if (command.equalsIgnoreCase("mobleaderboardplayerpersonal")){
            ret = getLeaderboard(false, true, par1, true);
        }
        if (command.equalsIgnoreCase("itemleaderboardscorepersonal")){
            ret = getLeaderboard(true, true, par1, false);
        }
        if (command.equalsIgnoreCase("mobleaderboardscorepersonal")){
            ret = getLeaderboard(false, true, par1, false);
        }

        return ret;
    }

    public ArrayList<String> splitKeepingBraces(String input) {
        if (!input.contains("{") || !input.contains("}")) {
            return new ArrayList<>(Arrays.asList(input.split("_")));
        }

        boolean change = false;
        String tempString = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (input.charAt(i) == '{') {change = true;}
            if (input.charAt(i) == '}') {change = false;}

            if (change){
                if (c == '_' || c == ' ') {
                    c = '-';
                }
            }

            tempString = tempString + c;
        }

        ArrayList<String> result = new ArrayList<>(Arrays.asList(tempString.split("_")));

        for (int i = 0; i < result.size(); i++) {
            result.set(i, result.get(i).replaceAll("-", "_"));
            result.set(i, result.get(i).replaceAll("[{}]", ""));
        }

        return result;
    }

    //Functions to return the correct strings for the placeholder
    private String getProg(Boolean isItem, String pName, String playerName) {
        if ((isItem && !Main.data.item_toggle) || (!isItem && !Main.data.mob_toggle)) {return NA;}
        if (!Main.data.general_progress) {return NA;}

        if (pName == null) {
            if (!Main.data.general_global) {return NA;}

            return isItem ? Integer.toString(Main.all_items.get_progress()) : Integer.toString(Main.all_mobs.get_progress());
        } else {
            boolean samePlayer = pName.equalsIgnoreCase(playerName);
            if ((samePlayer && !Main.data.general_personal) || (!samePlayer && !Main.data.general_others)) {return NA;}

            player p = Main.player_list.get_player_from_string(pName);
            if (p == null) {return NA;}

            return isItem ? Integer.toString(p.item_list.get_progress()) : Integer.toString(p.mob_list.get_progress());
        }
    }

    private String getScore(Boolean isItem, String pName, String playerName) {
        if ((isItem && !Main.data.item_toggle) || (!isItem && !Main.data.mob_toggle)) {return NA;}
        if (!Main.data.general_player) {return NA;}
        if (!Main.data.general_global) {return NA;}

        player p = Main.player_list.get_player_from_string(pName);
        if (p == null) {
            return NA;
        }
        return isItem ? Integer.toString(p.score) : Integer.toString(p.mobScore);
    }

    private String checkItem(Boolean isItem, String pName, String itemName, String playerName) {
        if ((isItem && !Main.data.item_toggle) || (!isItem && !Main.data.mob_toggle)) {return NA;}
        if (!Main.data.general_check) {return NA;}
        if ((isItem && !Main.all_items.item_exists(itemName)) || (!isItem && !Main.all_mobs.item_exists(itemName))) {return NA;}

        boolean tempBool;

        if (pName == null) {
            if (!Main.data.general_global) {return NA;}

            tempBool = isItem ? Main.all_items.get_item(itemName).isFound : Main.all_mobs.get_item(itemName).isFound;
        } else {
            boolean samePlayer = pName.equalsIgnoreCase(playerName);
            if ((samePlayer && !Main.data.general_personal) || (!samePlayer && !Main.data.general_others)) {return NA;}

            player p = Main.player_list.get_player_from_string(pName);
            if (p == null) {return NA;}

            tempBool = isItem ? p.item_list.get_item(itemName).isFound : p.mob_list.get_item(itemName).isFound;
        }

        return tempBool ? Main.lang.placeholderTrue : Main.lang.placeholderFalse;
    }

    private String checkComplete(Boolean isItem, String pName, String playerName) {
        if ((isItem && !Main.data.item_toggle) || (!isItem && !Main.data.mob_toggle)) {return NA;}

        boolean tempBool;

        if (pName == null) {
            if (!Main.data.general_global) {return NA;}
            tempBool = isItem ? Main.all_items.completed : Main.all_mobs.completed;
        } else {
            boolean samePlayer = pName.equalsIgnoreCase(playerName);
            if ((samePlayer && !Main.data.general_personal) || (!samePlayer && !Main.data.general_others)) {return NA;}

            player p = Main.player_list.get_player_from_string(pName);
            if (p == null) {return NA;}

            tempBool = isItem ? p.item_list.completed : p.mob_list.completed;
        }

        return tempBool ? Main.lang.placeholderTrue : Main.lang.placeholderFalse;
    }

    private String getLeaderboard(Boolean isItem, boolean isPersonal, String place, boolean isName) {
        if ((isItem && !Main.data.item_toggle) || (!isItem && !Main.data.mob_toggle)) {return NA;}
        if ((isPersonal && !Main.data.general_others) || (!isPersonal && !Main.data.general_global)) {return NA;}

        int placeInt = Integer.parseInt(place) - 1;

        ArrayList<playerSort> plSort = Main.player_list.sort_players(!isItem, isPersonal);

        if (placeInt >= plSort.size() || placeInt < 0) {return NA;}

        return isName ? plSort.get(placeInt).name : Integer.toString(plSort.get(placeInt).score);
    }
}
