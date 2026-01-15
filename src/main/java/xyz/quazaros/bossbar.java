package xyz.quazaros;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import xyz.quazaros.items.itemList;

public class bossbar {

    public BossBar spBossBar;

    public final String color1;
    public final String color2;
    public final String color3;

    public bossbar() {
        NamespacedKey bossbarkey = new NamespacedKey(main.getPlugin(), "73");

        spBossBar = Bukkit.createBossBar(bossbarkey, "All Item Bossbar", BarColor.RED, BarStyle.SOLID);
        spBossBar.setProgress(0.5);

        color1 = "light_purple";
        color2 = "aqua";
        color3 = "white";
    }

    public void updateBossBar(boolean isBoot) {
        itemList ItemList = main.getPlugin().ItemList;
        if (ItemList.currentItem == null) {
            System.out.println("TEST");
            setComplete();
            return;
        }

        String newTitle;

        if (main.getPlugin().version.isGreater(21.9)) {
            String currentSprite = main.getPlugin().itemSprite.getSprite(ItemList.currentItem.type);
            newTitle =
                "[{\"color\":\"" + color1 + "\",\"text\":\"Current Item: \"}," +
                "{\"color\":\"" + color2 + "\",\"text\":\"" + ItemList.currentItem.display_name + "\"}," +
                "{\"color\":\"" + color3 + "\",\"text\":\" (\"}," +
                currentSprite +
                ",{\"color\":\"" + color3 + "\",\"text\":\")\"}," +
                "{\"color\":\"" + color3 + "\",\"text\":\" | \"}," +
                "{\"color\":\"" + color1 + "\",\"text\":\"Progress: \"}," +
                "{\"color\":\"" + color1 + "\",\"text\":\"" + ItemList.getProgString() + "\"}]";
        } else {
            newTitle =
                "[{\"color\":\"" + color1 + "\",\"text\":\"Current Item: \"}," +
                "{\"color\":\"" + color2 + "\",\"text\":\"" + ItemList.currentItem.display_name + "\"}," +
                "{\"color\":\"" + color3 + "\",\"text\":\" | \"}," +
                "{\"color\":\"" + color1 + "\",\"text\":\"Progress: \"}," +
                "{\"color\":\"" + color1 + "\",\"text\":\"" + ItemList.getProgString() + "\"}]";
        }

        String command = "bossbar set all_items_random:73 name " + newTitle;

        if (isBoot) {
            Bukkit.getScheduler().runTaskLater(main.getPlugin(), () -> {
                sendCommand(command);
            }, 10L);
        } else {
            sendCommand(command);
        }

        spBossBar.setProgress(ItemList.getProgFloat());
    }

    private void sendCommand(String command) {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false);
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, true);
        }
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
