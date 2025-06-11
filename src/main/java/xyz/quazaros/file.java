package xyz.quazaros;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.quazaros.data.items.*;
import xyz.quazaros.data.player.*;

import java.io.*;
import java.util.*;

public class file {
    public List<String> file_list;
    public List<String> mob_file_list;

    private final File file_data;
    private final File file_items;
    private final File file_mobs;
    private final File file_player;
    private final File file_lists;
    private final File file_mob_lists;
    private final File file_normal;
    private final File file_alphabetical;
    private final File file_all;
    private final File file_normal_mobs;
    private final File file_personal_items;
    private final File file_personal_mobs;
    private final File configFile;
    private final File langFile;

    private final String path_pre;

    ArrayList<String> item_string_list;
    ArrayList<String> mob_string_list;

    boolean reset;
    boolean mob_reset;

    ArrayList<String> normal;
    ArrayList<String> alphabetical;
    ArrayList<String> all;
    ArrayList<String> normal_mobs;

    ArrayList<String> items_init;
    ArrayList<String> mobs_init;
    ArrayList<String> all_items_init;
    ArrayList<String> all_mobs_init;

    public file() {
        //Sets up file names
        path_pre = xyz.quazaros.main.getPlugin().getDataFolder().getAbsolutePath();

        file_data = new File(path_pre + "/Data");

        file_items = new File(path_pre + "/Data/items.json");
        file_mobs = new File(path_pre + "/Data/mobs.json");
        file_player = new File(path_pre + "/Data/players.txt");

        file_lists = new File(path_pre + "/ItemLists");
        file_mob_lists = new File(path_pre + "/MobLists");
        file_normal = new File(path_pre + "/ItemLists/normal.txt");
        file_alphabetical = new File(path_pre + "/ItemLists/alphabetical.txt");
        file_all = new File(path_pre + "/ItemLists/all.txt");
        file_normal_mobs = new File(path_pre + "/MobLists/normal.txt");

        file_personal_items = new File(path_pre + "/Data/PersonalItems");
        file_personal_mobs = new File(path_pre + "/Data/PersonalMobs");

        configFile = new File(path_pre + "/config.yml");
        langFile = new File(path_pre + "/lang.yml");

        //Initializes item lists
        items_init = new ArrayList<>();
        mobs_init = new ArrayList<>();
        all_items_init = new ArrayList<>();
        all_mobs_init = new ArrayList<>();

        //Sets default settings
        reset = false;
        mob_reset = false;

        //Gets the list of items/mobs from the files in resources
        items_init.addAll(get_from_file("items.txt"));
        mobs_init.addAll(get_from_file("mobs.txt"));
        all_items_init.addAll(get_from_file("total_items.txt"));
        all_mobs_init.addAll(get_from_file("total_mobs.txt"));

        //Initializes string lists
        item_string_list = new ArrayList<>();

        normal = new ArrayList<>();
        alphabetical = new ArrayList<>();
        all = new ArrayList<>();
        normal_mobs = new ArrayList<>();

        normal.addAll(items_init);
        alphabetical.addAll(normal);
        Collections.sort(alphabetical);
        all.addAll(all_items_init);
        normal_mobs.addAll(mobs_init);
    }

    public void get_data() {
        try {setup_files();} catch (IOException e) {throw new RuntimeException(e);}

        try {get_config();} catch (IOException e) {throw new RuntimeException(e);}

        try {string_list_setup();} catch (IOException e) {throw new RuntimeException(e);}

        main.getPlugin().emptyItemList = new itemList(false, item_string_list);
        main.getPlugin().emptyMobList = new itemList(true, mob_string_list);

        try {get_players();} catch (IOException e) {throw new RuntimeException(e);}

        try {get_items();} catch (IOException e) {throw new RuntimeException(e);}
    }

    public void send_data(boolean remove) {
        try {send_items(remove);} catch (IOException e) {throw new RuntimeException(e);}
    }

    //Gets players from the playerList file
    private void get_players() throws IOException {
        playerList player_list = main.getPlugin().player_list;

        if (file_player.exists()) {
            Scanner myScanner = new Scanner(file_player);
            while (myScanner.hasNextLine()) {
                player_list.players.add(new player(myScanner.nextLine()));
            }
        }
    }

    //Creates directory and files
    private void setup_files() throws IOException {
        file_data.mkdirs();
        file_personal_items.mkdirs();
        file_personal_mobs.mkdirs();
    }

    //Gets the config and lang
    private void get_config() throws IOException {
        if (!configFile.exists()) {
            main.getPlugin().saveResource("config.yml", false);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        main.getPlugin().data.initialize(config);

        if (!langFile.exists()) {
            main.getPlugin().saveResource("lang.yml", false);
        }
        YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
        main.getPlugin().lang.initialize(langConfig);
    }

    //Sets up the string list files
    private void string_list_setup() throws IOException {
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

        item_string_list = get_item_from_list(main.getPlugin().data.item_file, false);
        mob_string_list = get_item_from_list(main.getPlugin().data.mob_file, true);
    }

    //Gets data upon starting the server
    private void get_items() throws IOException {
        main.getPlugin().all_items = new itemList(main.getPlugin().emptyItemList, false);
        main.getPlugin().all_mobs = new itemList(main.getPlugin().emptyMobList, false);
        itemList all_items = main.getPlugin().all_items;
        itemList all_mobs = main.getPlugin().all_mobs;

        Gson gson = new GsonBuilder().setLenient().create();
        Reader myReader;

        if (file_items.exists()) {
            myReader = new FileReader(file_items);
            itemData[] temp = gson.fromJson(myReader, itemData[].class);
            for (int i=0; i<all_items.items.size(); i++) {
                if (temp[i] != null && all_items.items.get(i).item_name != null && temp[i].name.equals(all_items.items.get(i).item_name)) {
                    all_items.items.get(i).item_data = temp[i];
                    if (all_items.items.get(i).item_data.is_found) {
                        all_items.items.get(i).submit(temp[i].player, temp[i].date);
                    }
                }
                else {
                    for (int j=0; j<all_items.items.size(); j++) {
                        if (temp[i] != null && all_items.items.get(i).item_name != null && temp[i].name.equals(all_items.items.get(j).item_name)) {
                            all_items.items.get(j).item_data = temp[i];
                            if (all_items.items.get(j).item_data.is_found) {
                                all_items.items.get(j).submit(temp[i].player, temp[i].date);
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
            for (int i=0; i<all_mobs.items.size(); i++) {
                if (temp[i].name.equals(all_mobs.items.get(i).item_name)) {
                    all_mobs.items.get(i).item_data = temp[i];
                    if (all_mobs.items.get(i).item_data.is_found) {
                        all_mobs.items.get(i).submit(temp[i].player, temp[i].date);
                    }
                }
                else {
                    for (int j=0; j<all_mobs.items.size(); j++) {
                        if (temp[i].name.equals(all_mobs.items.get(j).item_name)) {
                            all_mobs.items.get(j).item_data = temp[i];
                            if (all_mobs.items.get(j).item_data.is_found) {
                                all_mobs.items.get(j).submit(temp[i].player, temp[i].date);
                            }
                        }
                    }
                }
            }
            myReader.close();
        }

        File tempFile;
        for (player pl : main.getPlugin().player_list.players) {
            tempFile = new File(path_pre + "/Data/PersonalItems/" + pl.name + ".json");
            if (tempFile.exists()) {
                myReader = new FileReader(tempFile);
                itemData[] temp = gson.fromJson(myReader, itemData[].class);
                for (int i=0; i<pl.item_list.items.size(); i++) {
                    if (temp[i] != null && pl.item_list.items.get(i).item_name != null && temp[i].name.equals(pl.item_list.items.get(i).item_name)) {
                        pl.item_list.items.get(i).item_data = temp[i];
                        if (pl.item_list.items.get(i).item_data.is_found) {
                            if (temp[i].player.isEmpty()) {pl.item_list.items.get(i).submit(temp[i].date);}
                            else {pl.item_list.items.get(i).submit(temp[i].player, temp[i].date);}
                        }
                    }
                    else {
                        for (int j=0; j<pl.item_list.items.size(); j++) {
                            if (temp[i] != null && pl.item_list.items.get(i).item_name != null && temp[i].name.equals(pl.item_list.items.get(j).item_name)) {
                                pl.item_list.items.get(j).item_data = temp[i];
                                if (pl.item_list.items.get(j).item_data.is_found) {
                                    if (temp[i].player.isEmpty()) {pl.item_list.items.get(i).submit(temp[i].date);}
                                    else {pl.item_list.items.get(i).submit(temp[i].player, temp[i].date);}
                                }
                            }
                        }
                    }
                }
                myReader.close();
            }
            tempFile = new File(path_pre + "/Data/PersonalMobs/" + pl.name + ".json");
            if (tempFile.exists()) {
                myReader = new FileReader(tempFile);
                itemData[] temp = gson.fromJson(myReader, itemData[].class);
                for (int i=0; i<pl.mob_list.items.size(); i++) {
                    if (temp[i].name.equals(pl.mob_list.items.get(i).item_name)) {
                        pl.mob_list.items.get(i).item_data = temp[i];
                        if (pl.mob_list.items.get(i).item_data.is_found) {
                            if (temp[i].player.isEmpty()) {pl.mob_list.items.get(i).submit(temp[i].date);}
                            else {pl.mob_list.items.get(i).submit(temp[i].player, temp[i].date);}
                        }
                    }
                    else {
                        for (int j=0; j<pl.mob_list.items.size(); j++) {
                            if (temp[i].name.equals(pl.mob_list.items.get(j).item_name)) {
                                pl.mob_list.items.get(j).item_data = temp[i];
                                if (pl.mob_list.items.get(j).item_data.is_found) {
                                    if (temp[i].player.isEmpty()) {pl.mob_list.items.get(i).submit(temp[i].date);}
                                    else {pl.mob_list.items.get(i).submit(temp[i].player, temp[i].date);}
                                }
                            }
                        }
                    }
                }
                myReader.close();
            }
        }

        file_list = remove_txt(Arrays.asList(file_lists.list()));
        mob_file_list = remove_txt(Arrays.asList(file_mob_lists.list()));
    }

    //Sends the data to files upon closing the server and periodically saving
    private void send_items(boolean remove) throws IOException {
        boolean item_delete = remove && reset;
        boolean mob_delete = remove && mob_reset;

        itemList items = main.getPlugin().all_items;
        itemList mobs = main.getPlugin().all_mobs;
        playerList players = main.getPlugin().player_list;

        ArrayList<itemData> item_data_list = new ArrayList<>();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Writer myWriter;
        if (!item_delete) {
            file_items.createNewFile();
            myWriter = new FileWriter(file_items, false);
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
            if (file_items.exists()) {
                file_items.delete();
            }
            if (file_personal_items.exists()) {
                file_personal_items.delete();
            }
        }
        item_data_list.clear();
        if (!mob_delete) {
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

        //Gets Personal Lists
        for (player p :  players.players) {
            item_data_list.clear();
            File tempFile = new File(file_personal_items +"/"+ p.name + ".json");
            if (!item_delete) {
                tempFile.createNewFile();
                myWriter = new FileWriter(tempFile, false);
                for (item i : p.item_list.items) {
                    item_data_list.add(i.item_data);
                }
                gson.toJson(item_data_list, item_data_list.getClass(), myWriter);
                myWriter.flush();
                myWriter.close();
            } else {
                p.itemPerScore = 0;
                if (tempFile.exists()) {
                    tempFile.delete();
                }
            }

            item_data_list.clear();
            tempFile = new File(file_personal_mobs +"/"+ p.name + ".json");
            if (!mob_delete) {
                tempFile.createNewFile();
                myWriter = new FileWriter(tempFile, false);
                for (item i : p.mob_list.items) {
                    item_data_list.add(i.item_data);
                }
                gson.toJson(item_data_list, item_data_list.getClass(), myWriter);
                myWriter.flush();
                myWriter.close();
            } else {
                p.mobPerScore = 0;
                if (tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }

        file_player.createNewFile();
        myWriter = new FileWriter(file_player, false);
        for (player p : players.players) {
            myWriter.write(p.name+"\n");
        }
        myWriter.close();
    }

    //Gets the list of items (strings) from a file
    private ArrayList<String> get_item_from_list(String str, boolean is_mob) throws IOException {
        File file_itemlist;
        if (!is_mob) {
            file_itemlist = new File(path_pre + "/ItemLists/" + str + ".txt");
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

    //Gets the list of strings from a file
    private ArrayList<String> get_from_file(String path) {
        ArrayList<String> temp = new ArrayList<>();
        try (InputStream inputStream = main.class.getClassLoader().getResourceAsStream("Data/" + path)) {
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

    //Removes the .txt extension from each string in a list
    private List<String> remove_txt(List<String> list) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            temp.add(list.get(i).substring(0, list.get(i).length() - 4));
        }
        return temp;
    }
}