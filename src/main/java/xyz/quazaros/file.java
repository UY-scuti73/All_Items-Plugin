package xyz.quazaros;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

public class file {
    public List<String> file_list;
    public List<String> mob_file_list;

    private final File file_data;
    private final File file_mobs;
    private final File file_player;
    private final File file_config;
    private final File file_lists;
    private final File mob_file_lists;
    private final File file_normal;
    private final File file_alphabetical;
    private final File file_all;
    private final File file_normal_mobs;

    private final String path_pre;

    ArrayList<String> item_string_list;
    ArrayList<String> mob_string_list;
    itemList total_items;
    itemList total_mobs;
    playerList player_list;

    String file_name;
    String mob_file_name;
    boolean reset;
    boolean mob_reset;
    boolean sub_item;
    boolean auto_collect;
    boolean toggle_items;
    boolean toggle_mobs;

    ArrayList<String> normal;
    ArrayList<String> alphabetical;
    ArrayList<String> all;
    ArrayList<String> normal_mobs;

    ArrayList<String> all_items_init;
    ArrayList<String> all_mobs_init;

    public file() {
        path_pre = xyz.quazaros.main.getPlugin().getDataFolder().getAbsolutePath();
        file_data = new File(path_pre + "/items.json");
        file_mobs = new File(path_pre + "/mobs.json");
        file_player = new File(path_pre + "/players.txt");
        file_config = new File(path_pre + "/config.txt");

        file_lists = new File(path_pre + "/Lists");
        mob_file_lists = new File(path_pre + "/MobLists");
        file_normal = new File(path_pre + "/Lists/normal.txt");
        file_alphabetical = new File(path_pre + "/Lists/alphabetical.txt");
        file_all = new File(path_pre + "/Lists/all.txt");
        file_normal_mobs = new File(path_pre + "/MobLists/normal.txt");

        normal = new ArrayList<>();
        alphabetical = new ArrayList<>();
        all = new ArrayList<>();
        normal_mobs = new ArrayList<>();

        all_items_init = new ArrayList<>();
        all_mobs_init = new ArrayList<>();

        file_name = "normal";
        mob_file_name = "normal";
        reset = false;
        sub_item = false;
        auto_collect = false;
        toggle_items = true;
        toggle_mobs = true;

        item_string_list = new ArrayList<>();

        //For Meta List
        all_items_init.addAll(get_from_file("total_items.txt"));
        all_mobs_init.addAll(get_from_file("total_mobs.txt"));
    }

    private ArrayList<String> get_from_file(String path) {
        ArrayList<String> temp = new ArrayList<>();
        try (InputStream inputStream = main.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    temp.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    //Gets data upon starting the server
    public void get_data() throws IOException {
        total_items = new itemList();
        total_mobs = new itemList();
        total_items.total();
        total_mobs.total_mobs();
        player_list = new playerList();

        normal.addAll(get_from_file("items.txt"));

        alphabetical.addAll(normal);
        Collections.sort(alphabetical);

        for (item i : total_items.items) {
            all.add(i.item_name);
        }

        for (item i : total_mobs.items) {
            normal_mobs.add(i.item_name);
        }

        if (file_config.exists()) {
            Scanner myScanner = new Scanner(file_config);
            String temp;
            temp = myScanner.nextLine();
            file_name = myScanner.nextLine();
            temp = myScanner.nextLine();
            temp = myScanner.nextLine();
            mob_file_name = myScanner.nextLine();
            temp = myScanner.nextLine();
            temp = myScanner.nextLine();
            temp = myScanner.nextLine();
            if (temp.equalsIgnoreCase("true")) {sub_item = true;} else {sub_item = false;}
            temp = myScanner.nextLine();
            temp = myScanner.nextLine();
            temp = myScanner.nextLine();
            if (temp.equalsIgnoreCase("true")) {auto_collect = true;} else {auto_collect = false;}
            temp = myScanner.nextLine();
            temp = myScanner.nextLine();
            temp = myScanner.nextLine();
            if (temp.equalsIgnoreCase("enabled")) {toggle_items = true;} else {toggle_items = false;}
            temp = myScanner.nextLine();
            temp = myScanner.nextLine();
            temp = myScanner.nextLine();
            if (temp.equalsIgnoreCase("enabled")) {toggle_mobs = true;} else {toggle_mobs = false;}

            item_string_list = get_item_from_list(file_name, false);
            mob_string_list = get_item_from_list(mob_file_name, true);
        } else {
            item_string_list = get_item_from_list("normal", false);
            mob_string_list = get_item_from_list("normal", true);
        }

        total_items.indexes = total_items.string_to_index(item_string_list);
        total_mobs.indexes = total_mobs.string_to_index(mob_string_list);

        Gson gson = new GsonBuilder().setLenient().create();
        Reader myReader;
        if (file_data.exists()) {
            myReader = new FileReader(file_data);
            itemData[] temp = gson.fromJson(myReader, itemData[].class);
            for (int i=0; i<total_items.items.size(); i++) {
                if (temp[i] != null && total_items.items.get(i).item_name != null && temp[i].name.equals(total_items.items.get(i).item_name)) {
                    total_items.items.get(i).item_data = temp[i];
                    if (total_items.items.get(i).item_data.is_found) {
                        total_items.items.get(i).submit(temp[i].player, temp[i].date);
                    }
                }
                else {
                    for (int j=0; j<total_items.items.size(); j++) {
                        if (temp[i] != null && total_items.items.get(i).item_name != null && temp[i].name.equals(total_items.items.get(j).item_name)) {
                            total_items.items.get(j).item_data = temp[i];
                            if (total_items.items.get(j).item_data.is_found) {
                                total_items.items.get(j).submit(temp[i].player, temp[i].date);
                            }
                        }
                    }
                }
            }
            myReader.close();
        }

        if (file_mobs.exists()) {
            myReader = new FileReader(file_mobs);
            itemData[] temp = gson.fromJson(myReader, itemData[].class);
            for (int i=0; i<total_mobs.items.size(); i++) {
                if (temp[i].name.equals(total_mobs.items.get(i).item_name)) {
                    total_mobs.items.get(i).item_data = temp[i];
                    if (total_mobs.items.get(i).item_data.is_found) {
                        total_mobs.items.get(i).submit(temp[i].player, temp[i].date);
                    }
                }
                else {
                    for (int j=0; j<total_mobs.items.size(); j++) {
                        if (temp[i].name.equals(total_mobs.items.get(j).item_name)) {
                            total_mobs.items.get(j).item_data = temp[i];
                            if (total_mobs.items.get(j).item_data.is_found) {
                                total_mobs.items.get(j).submit(temp[i].player, temp[i].date);
                            }
                        }
                    }
                }
            }
        }

        if (file_player.exists()) {
            Scanner myScanner = new Scanner(file_player);
            while (myScanner.hasNextLine()) {
                player_list.players.add(new player(myScanner.nextLine()));
            }
        }

        player_list.initialize_score(total_items, false);
        player_list.initialize_score(total_mobs, true);

        //Sets up the default "list" files
        file_normal.getParentFile().mkdirs();

        if (!file_normal.exists()) {
            file_normal.createNewFile();
            Writer myWriter = new FileWriter(file_normal, false);
            for (int i = 0; i < normal.size(); i++) {
                myWriter.write(normal.get(i)+"\n");
            }
            myWriter.close();
        }
        if (!file_alphabetical.exists()) {
            file_alphabetical.createNewFile();
            Writer myWriter = new FileWriter(file_alphabetical, false);
            for (String i : alphabetical) {
                myWriter.write(i+"\n");
            }
            myWriter.close();
        }
        if (!file_all.exists()) {
            file_all.createNewFile();
            Writer myWriter = new FileWriter(file_all, false);
            for (String i : all) {
                myWriter.write(i+"\n");
            }
            myWriter.close();
        }

        file_normal_mobs.getParentFile().mkdirs();

        if (!file_normal_mobs.exists()) {
            file_normal_mobs.createNewFile();
            Writer myWriter = new FileWriter(file_normal_mobs, false);
            for (String i : normal_mobs) {
                myWriter.write(i+"\n");
            }
            myWriter.close();
        }

        file_list = remove_txt(Arrays.asList(file_lists.list()));
        mob_file_list = remove_txt(Arrays.asList(mob_file_lists.list()));
    }

    //Sends the data to files upon closing the server and periodically saving
    public void send_data(itemList items, itemList mobs, playerList players) throws IOException {
        ArrayList<itemData> item_data_list = new ArrayList<>();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Writer myWriter;
        if (!reset) {
            file_data.createNewFile();
            myWriter = new FileWriter(file_data, false);
            for (item i : items.items) {
                item_data_list.add(i.item_data);
            }
            gson.toJson(item_data_list, item_data_list.getClass(), myWriter);
            myWriter.flush();
            myWriter.close();
        } else {
            for (int i=0; i<players.players.size(); i++) {
                players.players.get(i).score = 0;
            }
            if (file_data.exists()) {
                file_data.delete();
            }
        }

        item_data_list.clear();
        if (!mob_reset) {
            file_mobs.createNewFile();
            myWriter = new FileWriter(file_mobs, false);
            for (item i : mobs.items) {
                item_data_list.add(i.item_data);
            }
            gson.toJson(item_data_list, item_data_list.getClass(), myWriter);
            myWriter.flush();
            myWriter.close();
        } else {
            for (int i=0; i<players.players.size(); i++) {
                players.players.get(i).mobScore = 0;
            }
            if (file_mobs.exists()) {
                file_mobs.delete();
            }
        }

        file_player.createNewFile();
        myWriter = new FileWriter(file_player, false);
        for (player p : players.players) {
            myWriter.write(p.name+"\n");
        }
        myWriter.close();

        file_config.createNewFile();
        myWriter = new FileWriter(file_config, false);
        myWriter.write("Item File Name:\n");
        myWriter.write(file_name + "\n\n");
        myWriter.write("Mob File Name:\n");
        myWriter.write(mob_file_name + "\n\n");
        myWriter.write("Subtraction:\n");
        myWriter.write((sub_item ? "True" : "False") + "\n\n");
        myWriter.write("Auto Collect:\n");
        myWriter.write((auto_collect ? "True" : "False") + "\n\n");
        myWriter.write("Items Toggle:\n");
        myWriter.write((toggle_items ? "Enabled" : "Disabled") + "\n\n");
        myWriter.write("Mobs Toggle:\n");
        myWriter.write((toggle_mobs ? "Enabled" : "Disabled") + "\n\n");
        myWriter.close();
    }

    //Gets the list of items (strings) from a file
    private ArrayList<String> get_item_from_list(String str, boolean is_mob) throws IOException {
        File file_itemlist;
        if (!is_mob) {
            file_itemlist = new File(path_pre + "/Lists/" + str + ".txt");
        } else {
            file_itemlist = new File(path_pre + "/MobLists/" + str + ".txt");
        }
        ArrayList<String> temp = new ArrayList<>();
        if (file_itemlist.exists()) {
            Scanner myReader = new Scanner(file_itemlist);
            while (myReader.hasNextLine()) {
                temp.add(myReader.nextLine());
            }
        } else {
            if (!is_mob) {
                temp = normal;
            } else {
                temp = normal_mobs;
            }
        }
        return temp;
    }

    //Removes the .txt extension from each string in a list
    private List<String> remove_txt(List<String> list) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            temp.add(list.get(i).substring(0, list.get(i).length() - 4));
        }
        return temp;
    }
}