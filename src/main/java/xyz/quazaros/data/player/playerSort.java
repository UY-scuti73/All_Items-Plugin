package xyz.quazaros.data.player;

public class playerSort implements Comparable<playerSort>{
    String name;
    int score;

    public playerSort(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(playerSort other) {
        return Integer.compare(other.score, this.score);
    }
}
