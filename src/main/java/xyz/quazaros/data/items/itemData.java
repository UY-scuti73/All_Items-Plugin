package xyz.quazaros.data.items;

import com.google.gson.annotations.Expose;

public class itemData {
    @Expose
    public Boolean is_found;
    @Expose
    public String name;
    @Expose
    public String player;
    @Expose
    public String date;

    public itemData(String n) {
        name = n;
        is_found = false;
        player = "";
        date = "";
    }

    public void set_name(String n) {
        name = n;
    }

    public void submit(String n, String p, String d) {
        is_found = true;
        name = n;
        player = p;
        date = d;
    }

    public void unsubmit() {
        is_found = false;
        player = "";
        date = "";
    }
}
