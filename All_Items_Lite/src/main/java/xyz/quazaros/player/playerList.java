package xyz.quazaros.player;

import java.util.ArrayList;

public class playerList {
    public ArrayList<player> players;

    public playerList() {
        players = new ArrayList<>();
    }

    public player getPlayer(String name) {
        for (player p : players) {
            if (p.name.equalsIgnoreCase(name)) {
                return p;
            }
        }

        player temp = new player(name);
        players.add(temp);
        return temp;
    }
}
