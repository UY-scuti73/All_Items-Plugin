package xyz.quazaros.data.player;

import org.bukkit.ChatColor;
import xyz.quazaros.data.items.itemList;
import xyz.quazaros.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        itemList all_items = main.getPlugin().all_items;
        itemList all_mobs = main.getPlugin().all_mobs;

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

    //Gets the top 3 players for the leaderboard
    public ArrayList<String> leaderboard(boolean mob, boolean personal) {
        ArrayList<String> temp = new ArrayList<>();

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
            } else {return temp;}
        }

        //Sorts the objects
        Collections.sort(plSort);

        //Adds everything to the string list
        String str;
        ChatColor color = main.getPlugin().lang.colorDom;
        for (int i=0; i<plSort.size(); i++) {
            if (i >= 3) {break;}
            str = color + Integer.toString(i+1) + ": " + plSort.get(i).name + " - " + plSort.get(i).score;
            temp.add(str);
        }

        return temp;
    }

    /*
    //Gets the top 3 players for the leaderboard
    public ArrayList<String> leaderboard(boolean is_mob) {
        ArrayList<String> temp = new ArrayList<>();

        if (!is_mob) {
            if (players.size() == 0) {
                temp.add("No Players");
            } else if (players.size() == 1) {
                temp.add(main.getPlugin().lang.colorDom + "1: " + players.get(0).name + " - " + players.get(0).score);
            } else if (players.size() == 2) {
                if (players.get(0).score > players.get(1).score) {
                    temp.add(main.getPlugin().lang.colorDom + "1: " + players.get(0).name + " - " + players.get(0).score);
                    temp.add(main.getPlugin().lang.colorDom + "2: " + players.get(1).name + " - " + players.get(1).score);
                } else {
                    temp.add(main.getPlugin().lang.colorDom + "1: " + players.get(1).name + " - " + players.get(1).score);
                    temp.add(main.getPlugin().lang.colorDom + "2: " + players.get(0).name + " - " + players.get(0).score);
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
                        temp.add(main.getPlugin().lang.colorDom + Integer.toString(i+1) + ": " + temp_player.name + " - " + temp_player.score);
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
                    temp.add(main.getPlugin().lang.colorDom + Integer.toString(i+1) + ": " + temp_player.name + " - " + temp_player.score);
                    temp_player_list.remove(temp_player);
                }
            }
        } else {
            if (players.size() == 0) {
                temp.add("No Players");
            } else if (players.size() == 1) {
                temp.add(main.getPlugin().lang.colorDom + "1: " + players.get(0).name + " - " + players.get(0).mobScore);
            } else if (players.size() == 2) {
                if (players.get(0).mobScore > players.get(1).mobScore) {
                    temp.add(main.getPlugin().lang.colorDom + "1: " + players.get(0).name + " - " + players.get(0).mobScore);
                    temp.add(main.getPlugin().lang.colorDom + "2: " + players.get(1).name + " - " + players.get(1).mobScore);
                } else {
                    temp.add(main.getPlugin().lang.colorDom + "1: " + players.get(1).name + " - " + players.get(1).mobScore);
                    temp.add(main.getPlugin().lang.colorDom + "2: " + players.get(0).name + " - " + players.get(0).mobScore);
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
                        temp.add(main.getPlugin().lang.colorDom + Integer.toString(i+1) + ": " + temp_player.name + " - " + temp_player.mobScore);
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
                    temp.add(main.getPlugin().lang.colorDom + Integer.toString(i) + ": " + temp_player.name + " - " + temp_player.mobScore);
                    temp_player_list.remove(temp_player);
                }
            }
        }
        return temp;
    }*/
}

