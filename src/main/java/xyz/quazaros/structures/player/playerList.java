package xyz.quazaros.structures.player;

import org.bukkit.ChatColor;
import xyz.quazaros.structures.items.itemList;
import xyz.quazaros.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static xyz.quazaros.util.main.mainVariables.getVariables;

public class playerList {
    public ArrayList<player> players;
    public List<String> player_names;

    public playerList() {
        players = new ArrayList<>();
        player_names = new ArrayList<>();
    }

    //Gets the place in the list of a player from a players name
    public int get_player_place(String s) {
        for (int i=0; i<players.size(); i++) {
            if(players.get(i).name.equalsIgnoreCase(s)) {
                return i;
            }
        }
        return -1;
    }

    //Provides the player from the players name
    public player get_player_from_string(String s) {
        for (int i=0; i<players.size(); i++) {
            if(players.get(i).name.equalsIgnoreCase(s)) {
                return players.get(i);
            }
        }
        return null;
    }

    //Checks if a player exists
    public boolean player_exists(String s) {
        if (get_player_place(s) != -1) {
            return true;
        }
        return false;
    }

    //Initializes the score of every player in the list
    public void initialize_score() {
        itemList all_items = getVariables().all_items;
        itemList all_mobs = getVariables().all_mobs;

        for (int i=0; i<players.size(); i++) {
            players.get(i).score = 0;
            players.get(i).itemPerScore = players.get(i).item_list.get_progress();
        }
        String found;
        for (int i = 0; i < all_items.indexes.size(); i++) {
            found = all_items.items.get(all_items.indexes.get(i)).item_founder;
            for (int j = 0; j < players.size(); j++) {
                if (found != null && found.equals(players.get(j).name)) {
                    players.get(j).score++;
                }
            }
        }
        for (int i=0; i<players.size(); i++) {
            players.get(i).mobScore = 0;
            players.get(i).mobPerScore = players.get(i).mob_list.get_progress();
        }
        for (int i = 0; i < all_mobs.indexes.size(); i++) {
            found = all_mobs.items.get(all_mobs.indexes.get(i)).item_founder;
            for (int j = 0; j < players.size(); j++) {
                if (found != null && found.equals(players.get(j).name)) {
                    players.get(j).mobScore++;
                }
            }
        }
    }

    //Updates the list of player names
    public void update_player_names() {
        List<String> temp = new ArrayList<>();
        for (int i=0; i<players.size(); i++) {
            temp.add(players.get(i).name);
        }
        player_names = temp;
    }

    //Sorts the player list by score
    public ArrayList<playerSort> sort_players(boolean mob, boolean personal) {
        ArrayList<playerSort> plSort = new ArrayList<>();

        //Regardless of the settings it will set up a player sort object with the right name and score
        for (player pl : players) {
            if (!mob && !personal) {
                plSort.add(new playerSort(pl.name, pl.score));
            } else if (!mob && personal) {
                plSort.add(new playerSort(pl.name, pl.itemPerScore));
            } else if (mob && !personal) {
                plSort.add(new playerSort(pl.name, pl.mobScore));
            } else if (mob && personal) {
                plSort.add(new playerSort(pl.name, pl.mobPerScore));
            } else {return null;}
        }

        //Sorts the objects
        Collections.sort(plSort);

        return plSort;
    }

    //Gets the top 3 players for the leaderboard
    public ArrayList<String> leaderboard(boolean mob, boolean personal) {
        ArrayList<String> temp = new ArrayList<>();

        ArrayList<playerSort> plSort = sort_players(mob, personal);

        //Adds everything to the string list
        String str;
        ChatColor color = getVariables().lang.colorDom;
        for (int i=0; i<plSort.size(); i++) {
            if (i >= 3) {break;}
            str = color + Integer.toString(i+1) + ": " + plSort.get(i).name + " - " + plSort.get(i).score;
            temp.add(str);
        }

        return temp;
    }
}

