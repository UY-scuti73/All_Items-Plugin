package xyz.quazaros.items;

import org.bukkit.inventory.ItemStack;
import xyz.quazaros.main;

import java.util.ArrayList;

import static xyz.quazaros.file.retrieveStringList;

public class itemList {
    public ArrayList<item> total;
    public ArrayList<item> found;
    public ArrayList<item> not_found;

    public item currentItem;

    public itemList() {
        total = new ArrayList<>();
        found = new ArrayList<>();
        not_found = new ArrayList<>();
    }

    public void initialize(ArrayList<String> str_list) {
        for (String itemString : str_list) {
            total.add(new item(itemString));
        }
        not_found.addAll(total);
    }

    public void submit(item curItem) {
        found.add(curItem);
        not_found.remove(curItem);
    }

    public item submitItem(ItemStack itemStack) {
        if (currentItem == null) {return null;}
        if (isCompleted()) {return null;}
        if (itemStack.getType() == currentItem.type) {
            item curItem = getIndex(itemStack);
            if (curItem == null) {return null;}

            submit(curItem);

            if (isCompleted()) {
                main.getPlugin().BossBar.setComplete();
                currentItem = null;
                return null;
            }

            setRandomItem();
            main.getPlugin().BossBar.updateBossBar(false);

            return curItem;
        }
        return null;
    }

    private item getIndex(ItemStack itemStack) {
        for (item i : not_found) {
            if (i.type == itemStack.getType()) {
                return i;
            }
        }
        return null;
    }

    public void setRandomItem() {
        currentItem = getRandomItem();
    }

    public item getRandomItem() {
        if (not_found.isEmpty()) {return null;}
        int index = (int) (Math.random() * not_found.size());
        return not_found.get(index);
    }

    public int size() {
        return total.size();
    }

    public String getProgString() {
        return found.size() + "/" + total.size();
    }

    public float getProgFloat() {
        return (float) found.size() / (float) total.size();
    }

    public boolean isCompleted() {
        return not_found.isEmpty();
    }
}
