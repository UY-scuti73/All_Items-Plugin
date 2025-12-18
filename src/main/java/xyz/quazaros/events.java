package xyz.quazaros;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import xyz.quazaros.items.item;

public class events implements Listener {
    @EventHandler
    public void PlayerPickupItemEvent(PlayerPickupItemEvent e) {
        item tempItem = main.getPlugin().ItemList.submitItem(e.getItem().getItemStack());
        if (tempItem == null) {return;}

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatColor.GREEN + tempItem.display_name + " Has Been Submitted By " + e.getPlayer().getName());
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        main.getPlugin().BossBar.updatePlayers();
    }
}
