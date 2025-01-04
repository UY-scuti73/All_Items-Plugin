package xyz.quazaros;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class playerList {
    ArrayList<player> players;
    List<String> player_names;

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

    //Checks if a player_name is in the list
    public boolean in_list(String s) {
        for (player p : players) {
            if (s.equalsIgnoreCase(p.name)) {
                return true;
            }
        }
        return false;
    }

    //Initializes the score of every player in the list
    public void initialize_score(itemList item_list, boolean is_mob) {
        if (!is_mob) {
            for (int i=0; i<players.size(); i++) {
                players.get(i).score = 0;
            }
            for (int i = 0; i < item_list.indexes.size(); i++) {
                for (int j = 0; j < players.size(); j++) {
                    if (item_list.items.get(item_list.indexes.get(i)).item_founder.equals(players.get(j).name)) {
                        players.get(j).score++;
                    }
                }
            }
        } else {
            for (int i=0; i<players.size(); i++) {
                players.get(i).mobScore = 0;
            }
            for (int i = 0; i < item_list.indexes.size(); i++) {
                for (int j = 0; j < players.size(); j++) {
                    if (item_list.items.get(item_list.indexes.get(i)).item_founder.equals(players.get(j).name)) {
                        players.get(j).mobScore++;
                    }
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

    //Gets the top 3 players for the leaderboard
    public ArrayList<String> leaderboard(boolean is_mob) {
        ArrayList<String> temp = new ArrayList<>();

        if (!is_mob) {
            if (players.size() == 0) {
                temp.add("No Players");
            } else if (players.size() == 1) {
                temp.add(ChatColor.LIGHT_PURPLE + "1: " + players.get(0).name + " - " + players.get(0).score);
            } else if (players.size() == 2) {
                if (players.get(0).score > players.get(1).score) {
                    temp.add(ChatColor.LIGHT_PURPLE + "1: " + players.get(0).name + " - " + players.get(0).score);
                    temp.add(ChatColor.LIGHT_PURPLE + "2: " + players.get(1).name + " - " + players.get(1).score);
                } else {
                    temp.add(ChatColor.LIGHT_PURPLE + "1: " + players.get(1).name + " - " + players.get(1).score);
                    temp.add(ChatColor.LIGHT_PURPLE + "2: " + players.get(0).name + " - " + players.get(0).score);
                }
            } else {
                ArrayList<player> temp_player_list = new ArrayList<>();
                for (player p : players) {
                    temp_player_list.add(p);
                }
                player temp_player;
                for (int i = 0; i < 3; i++) {
                    if (temp_player_list.size() == 1) {
                        temp_player = temp_player_list.get(0);
                        temp.add(ChatColor.LIGHT_PURPLE + Integer.toString(i+1) + ": " + temp_player.name + " - " + temp_player.score);
                        break;
                    }
                    if (temp_player_list.get(0).score > temp_player_list.get(1).score) {
                        temp_player = temp_player_list.get(0);
                    } else {
                        temp_player = temp_player_list.get(1);
                    }
                    for (int j = 2; j < temp_player_list.size(); j++) {
                        if (temp_player_list.get(j).score > temp_player.score) {
                            temp_player = temp_player_list.get(j);
                        }
                    }
                    temp.add(ChatColor.LIGHT_PURPLE + Integer.toString(i+1) + ": " + temp_player.name + " - " + temp_player.score);
                    temp_player_list.remove(temp_player);
                }
            }
        } else {
            if (players.size() == 0) {
                temp.add("No Players");
            } else if (players.size() == 1) {
                temp.add(ChatColor.LIGHT_PURPLE + "1: " + players.get(0).name + " - " + players.get(0).mobScore);
            } else if (players.size() == 2) {
                if (players.get(0).mobScore > players.get(1).mobScore) {
                    temp.add(ChatColor.LIGHT_PURPLE + "1: " + players.get(0).name + " - " + players.get(0).mobScore);
                    temp.add(ChatColor.LIGHT_PURPLE + "2: " + players.get(1).name + " - " + players.get(1).mobScore);
                } else {
                    temp.add(ChatColor.LIGHT_PURPLE + "1: " + players.get(1).name + " - " + players.get(1).mobScore);
                    temp.add(ChatColor.LIGHT_PURPLE + "2: " + players.get(0).name + " - " + players.get(0).mobScore);
                }
            } else {
                ArrayList<player> temp_player_list = new ArrayList<>();
                for (player p : players) {
                    temp_player_list.add(p);
                }
                player temp_player;
                for (int i = 0; i < 3; i++) {
                    if (temp_player_list.size() == 1) {
                        temp_player = temp_player_list.get(0);
                        temp.add(ChatColor.LIGHT_PURPLE + Integer.toString(i+1) + ": " + temp_player.name + " - " + temp_player.mobScore);
                        break;
                    }
                    if (players.get(0).mobScore > players.get(1).mobScore) {
                        temp_player = temp_player_list.get(0);
                    } else {
                        temp_player = temp_player_list.get(1);
                    }
                    for (int j = 2; j < temp_player_list.size() - 2; j++) {
                        if (temp_player_list.get(j).mobScore > temp_player.mobScore) {
                            temp_player = temp_player_list.get(j);
                        }
                    }
                    temp.add(ChatColor.LIGHT_PURPLE + Integer.toString(i) + ": " + temp_player.name + " - " + temp_player.mobScore);
                    temp_player_list.remove(temp_player);
                }
            }
        }
        return temp;
    }
}

class player {
    String name;
    int score;
    int mobScore;
    int invItt;
    boolean sorted;
    boolean mobs;

    public player(String p_name) {
        name = p_name;
        score = 0;
        invItt = 0;
        sorted = false;
        mobs = false;
    }
}
