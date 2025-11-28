package xyz.quazaros.util.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.world.WorldSaveEvent;

import static xyz.quazaros.util.events.staticEvents.autoSave.handle_autoSave;
import static xyz.quazaros.util.events.staticEvents.inventoryClick.handle_inventoryClick;
import static xyz.quazaros.util.events.staticEvents.itemPickup.handle_itemPickup;
import static xyz.quazaros.util.events.staticEvents.mobDeath.handle_mobDies;
import static xyz.quazaros.util.events.staticEvents.playerJoin.handle_playerJoin;

public final class events implements Listener {

    @EventHandler
    public void guiClickEvent(InventoryClickEvent e) {
        handle_inventoryClick(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerPickupItemEvent(PlayerPickupItemEvent e) {
        handle_itemPickup(e);
    }

    @EventHandler
    public void EntityDeathEvent(EntityDeathEvent e) {
        handle_mobDies(e);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        handle_playerJoin(e);
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent e) {
        handle_autoSave();
    }
}
