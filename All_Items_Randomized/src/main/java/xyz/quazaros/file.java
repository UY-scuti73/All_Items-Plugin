package xyz.quazaros;

import xyz.quazaros.items.item;
import xyz.quazaros.items.itemList;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class file {
    private final File data_dir;

    private final File dataFile;
    private final File curFile;
    private final File itemsFile;

    public file() {
        String path_pre = main.getPlugin().getDataFolder().getAbsolutePath();

        data_dir = new File(path_pre + "/Data");
        dataFile = new File(path_pre + "/Data/data.txt");
        curFile = new File(path_pre + "/Data/currentItem.txt");
        itemsFile = new File(path_pre + "/items.txt");

        main.getPlugin().getDataFolder().mkdir();
        data_dir.mkdir();
    }

    public static ArrayList<String> retrieveStringList() {
        ArrayList<String> temp = new ArrayList<>();
        try (InputStream inputStream = main.class.getClassLoader().getResourceAsStream("Data/items.txt")) {
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    temp.add(line);
                }
            }
        } catch (IOException e) {e.printStackTrace();}
        return temp;
    }

    public void get() {
        try {getData();} catch (IOException e) {throw new RuntimeException(e);}
    }

    public void send() {
        try {saveData();} catch (IOException e) {throw new RuntimeException(e);}
    }

    private void getData() throws IOException {
        itemList ItemList = main.getPlugin().ItemList;
        ArrayList<String> temp;

        if (!itemsFile.exists()) {
            itemsFile.createNewFile();
            temp = retrieveStringList();

            Writer myWriter = new FileWriter(itemsFile, false);
            for (String s : temp) {
                myWriter.write(s + "\n");
            }
            myWriter.close();
        } else {
            temp = (ArrayList<String>) Files.readAllLines(itemsFile.toPath());
        }

        ItemList.initialize(temp);

        if (!dataFile.exists()) {
            dataFile.createNewFile();
        } else {
            String input = Files.readString(dataFile.toPath());
            int i = 0;
            for (char c : input.toCharArray()) {
                if (i > ItemList.size()) {break;}
                if (c == '1') {
                    ItemList.submit(ItemList.total.get(i));
                }
                i++;
            }
        }

        if (!curFile.exists()) {
            curFile.createNewFile();
            main.getPlugin().ItemList.setRandomItem();
        } else {
            String currentString = Files.readString(curFile.toPath());
            item currentItem = new item(currentString);
            if (currentItem.type != null) {
                main.getPlugin().ItemList.currentItem = currentItem;
            } else {
                main.getPlugin().ItemList.setRandomItem();
            }
            //Checks if list is completed
            if (main.getPlugin().ItemList.isCompleted()) {
                main.getPlugin().BossBar.updateBossBar();
                main.getPlugin().ItemList.currentItem = null;
            }
        }
    }

    private void saveData() throws IOException {
        itemList ItemList = main.getPlugin().ItemList;
        String output = "";
        for (item i : ItemList.total) {
            output += ItemList.found.contains(i) ? "1" : "0";
        }

        Writer myWriter = new FileWriter(dataFile, false);
        myWriter.write(output);
        myWriter.close();

        myWriter = new FileWriter(curFile, false);
        myWriter.write(ItemList.currentItem == null ? "null" : ItemList.currentItem.name);
        myWriter.close();
    }
}
