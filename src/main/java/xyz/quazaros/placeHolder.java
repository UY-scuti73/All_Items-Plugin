package xyz.quazaros;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import xyz.quazaros.data.items.item;
import xyz.quazaros.data.player.player;

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

        if (paramList.size() == 1) {
            if (paramList.get(0).equalsIgnoreCase("itemprog")){
                return getProg(true, null, player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("mobprog")){
                return getProg(false, null, player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("itemprogself")){
                return getProg(true, player.getName(), player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("mobprogself")){
                return getProg(false, player.getName(), player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("itemscoreself")){
                return getScore(true, player.getName(), player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("mobscoreself")){
                return getScore(false, player.getName(), player.getName());
            }
        } else if (paramList.size() == 2) {
            if (paramList.get(0).equalsIgnoreCase("itemprogpersonal")){
                return getProg(true, paramList.get(1), player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("mobprogpersonal")){
                return getProg(false, paramList.get(1), player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("itemscore")){
                return getScore(true, paramList.get(1), player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("mobscore")){
                return getScore(false, paramList.get(1), player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("itemcheck")){
                return checkItem(true, null, paramList.get(1), player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("mobcheck")){
                return checkItem(false, null, paramList.get(1), player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("itemcheckself")){
                return checkItem(true, player.getName(), paramList.get(1), player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("mobcheckself")){
                return checkItem(false, player.getName(), paramList.get(1), player.getName());
            }
        } else if (paramList.size() == 3) {
            if (paramList.get(0).equalsIgnoreCase("itemcheckpersonal")){
                return checkItem(true, paramList.get(1), paramList.get(2), player.getName());
            }
            if (paramList.get(0).equalsIgnoreCase("mobcheckpersonal")){
                return checkItem(false, paramList.get(1), paramList.get(2), player.getName());
            }
        }

        return null;
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
}
