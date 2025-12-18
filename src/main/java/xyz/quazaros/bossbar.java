package xyz.quazaros;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import xyz.quazaros.items.itemList;

public class bossbar {

    public BossBar spBossBar;

    public final ChatColor color1;
    public final ChatColor color2;
    public final ChatColor color3;

    public bossbar() {
        spBossBar = Bukkit.createBossBar("Test", BarColor.RED, BarStyle.SOLID);
        spBossBar.setProgress(0.5);

        color1 = ChatColor.LIGHT_PURPLE;
        color2 = ChatColor.AQUA;
        color3 = ChatColor.WHITE;
    }

    public void updateBossBar() {
        itemList ItemList = main.getPlugin().ItemList;
        if (ItemList.currentItem == null) {
            setComplete();
            return;
        }

        String newTitle = color1 + "Current Item: " + color2 + ItemList.currentItem.display_name + color3 + " | " + color1 + "Progress: " + color2 + ItemList.getProgString();
        spBossBar.setTitle(newTitle);

        spBossBar.setProgress(ItemList.getProgFloat());
    }

    public void setComplete() {
        spBossBar.setProgress(1);
        spBossBar.setTitle(color1 + "Completed");
        spBossBar.setColor(BarColor.GREEN);
    }

    public void updatePlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            spBossBar.addPlayer(p);
        }
    }

    public void removeBossBar() {
        spBossBar.removeAll();
    }
}
