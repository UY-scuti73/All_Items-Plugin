package xyz.quazaros;

import xyz.quazaros.items.item;
import xyz.quazaros.items.itemList;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class file {
    private final File dataFile;

    public file() {
        String path_pre = main.getPlugin().getDataFolder().getAbsolutePath();
        main.getPlugin().getDataFolder().mkdir();

        dataFile = new File(path_pre + "/data.txt");
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
        if (!dataFile.exists()) {
            dataFile.createNewFile();
        } else {
            String input = Files.readString(dataFile.toPath());
            itemList ItemList = main.getPlugin().ItemList;
            int i = 0;
            for (char c : input.toCharArray()) {
                if (i > ItemList.size()) {break;}
                if (c == '1') {
                    ItemList.item_list.get(i).submit();
                }
                i++;
            }
        }
    }

    private void saveData() throws IOException {
        String output = "";
        for (item i : main.getPlugin().ItemList.item_list) {
            output += i.isFound ? "1" : "0";
        }

        Writer myWriter = new FileWriter(dataFile, false);
        myWriter.write(output);
        myWriter.close();
    }
}
