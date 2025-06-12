package xyz.quazaros;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import xyz.quazaros.data.items.itemList;
import xyz.quazaros.data.player.player;

public class events {
    main Main;

    public events() {
        Main = main.getPlugin();
    }

    public void inventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        player pl = Main.player_list.get_player_from_string(p.getName());
        if(pl.inv.inventory_list.contains(e.getInventory()) || pl.inv.sorted_list.contains(e.getInventory())) {
            //Prevents the player from moving any items in the menu
            e.setCancelled(true);

            switch (e.getSlot()) {
                //Goes to the last page if the back arrow is pressed, unless it is on the first page
                case 45: {
                    if (!e.getInventory().equals(pl.inv.inventory_list.get(0)) && !e.getInventory().equals(pl.inv.sorted_list.get(0))) {
                        pl.invItt -= 1;
                    }
                    else{
                        pl.invItt = pl.inv.size - 1;
                    }

                    if (!pl.inv.is_sorted) {
                        p.openInventory(pl.inv.inventory_list.get(pl.invItt));
                    } else {
                        p.openInventory(pl.inv.sorted_list.get(pl.invItt));
                    }

                    break;
                }
                //Goes to the next page if the forward arrow is pressed, unless it is on the last page
                case 53: {
                    if (!e.getInventory().equals(pl.inv.inventory_list.get(pl.inv.size-1)) && !e.getInventory().equals(pl.inv.sorted_list.get(pl.inv.size-1))) {
                        pl.invItt += 1;
                    }
                    else{
                        pl.invItt = 0;
                    }

                    if (!pl.inv.is_sorted) {
                        p.openInventory(pl.inv.inventory_list.get(pl.invItt));
                    } else {
                        p.openInventory(pl.inv.sorted_list.get(pl.invItt));
                    }

                    break;
                }
                //Handles what happens when the sort button is presses
                case 47: {
                    if (!pl.inv.is_playerList) {
                        pl.invItt = 0;
                        pl.inv.is_sorted = !pl.inv.is_sorted;
                        if (pl.inv.is_sorted) {
                            p.openInventory(pl.inv.sorted_list.get(0));
                        } else {
                            p.openInventory(pl.inv.inventory_list.get(0));
                        }
                    }

                    break;
                }
                //Handles the flip for the player list
                case 49: {
                    if (pl.inv.is_playerList && pl.inv.add_flip) {
                        pl.invItt = 0;
                        if (pl.inv.is_public) {
                            pl.inv.set_players(pl.inv.is_mob, false);
                        } else {
                            pl.inv.set_players(pl.inv.is_mob, true);
                        }
                        p.openInventory(pl.inv.inventory_list.get(pl.invItt));
                    }
                }
            }
        }
    }

    public void itemPickup(PlayerPickupItemEvent e) {
        if (!Main.data.item_toggle) {
            return;
        }

        player pl = Main.player_list.get_player_from_string(e.getPlayer().getName());

        if (Main.data.item_autoCollect) {
            Player p = e.getPlayer();
            ItemStack tempStack = e.getItem().getItemStack();
            String message_main = Main.all_items.check_items(tempStack, p.getDisplayName(), true);
            String message_personal = pl.item_list.check_items(tempStack, p.getDisplayName(), true);
            String message = Main.data.general_listPriority ? message_personal : message_main;
            if (message.endsWith(Main.lang.itemSubmitted)) {
                if (Main.data.item_subtraction) {
                    if (tempStack.getAmount() <= 1) {
                        e.getItem().setItemStack(new ItemStack(Material.AIR));
                    } else {
                        e.getItem().setItemStack(new ItemStack(tempStack.getType(), tempStack.getAmount() - 1));
                    }
                }
                p.sendMessage(message);
            }
            checkCompleted(false, null);
            checkCompleted(false, pl);
        }
    }

    public void mobDies(EntityDeathEvent e) {
        if (!Main.data.mob_toggle) {
            return;
        }

        LivingEntity mob = (LivingEntity) e.getEntity();
        if(mob.getKiller() instanceof Player) {
            Player p = (Player) mob.getKiller();
            player pl = Main.player_list.get_player_from_string(p.getName());
            String name = mob.getName();
            int temp = 0;

            for (int i=0; i<Main.all_mobs.items.size(); i++) {
                if (Main.all_mobs.items.get(i).item_display_name.equalsIgnoreCase(name)) {
                    temp = i;
                }
            }

            String message_main = "";
            String message_personal = "";

            if (!Main.all_mobs.items.get(temp).isFound && Main.all_mobs.is_in_indexes(temp)) {
                message_main = Main.lang.colorGood + name + " " + Main.lang.mobKilledBy + " " + p.getName();
                Main.all_mobs.items.get(temp).submit(p.getName(), Main.all_mobs.date());
                checkCompleted(true, null);
            }
            if (!pl.mob_list.items.get(temp).isFound && pl.mob_list.is_in_indexes(temp)) {
                message_personal = Main.lang.colorGood + name + " " + Main.lang.mobKilled;
                pl.mob_list.items.get(temp).submit(pl.mob_list.date());
                checkCompleted(true, pl);
            }

            String message = Main.data.general_listPriority ? message_personal : message_main;
            if (!message.isEmpty()) {
                p.sendMessage(message);
            }
        }
    }

    public void playerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        int temp=0;
        for(int i=0; i<Main.player_list.players.size(); i++) {
            if (Main.player_list.players.get(i).name.equals(p.getName())) {
                temp++;
            }
        }
        if (temp==0) {
            Main.player_list.players.add(new player(p.getName()));
        }

        set_permissions(p);
    }

    //AutoSaves when the world does
    public void autoSave() {
        if (Main.data.general_autoSave) {
            Main.save_files(false);
        }
    }

    public void checkCompleted(boolean is_mob, player pl) {
        String message;
        itemList temp;

        if (pl == null) {
            if (!Main.data.general_mainCompletion) {return;}

            if (!is_mob) {
                message = Main.lang.colorGood + Main.lang.allItems;
                temp = Main.all_items;
            } else {
                message = Main.lang.colorGood + Main.lang.allMobs;
                temp = Main.all_mobs;
            }
        } else {
            if (!Main.data.general_personalCompletion) {return;}

            if (!is_mob) {
                message = Main.lang.colorGood + pl.name + " " + Main.lang.completeItemSuffix;
                temp = pl.item_list;
            } else {
                message = Main.lang.colorGood + pl.name + " " + Main.lang.completeMobSuffix;
                temp = pl.mob_list;
            }
        }

        if (temp.complete()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle(Main.lang.colorHigh + Main.lang.congrats, message);
            }
        }
    }

    //Sets the permission of a player
    private void set_permissions(Player player) {
        player.addAttachment(Main, "all_items.items", false);
        player.addAttachment(Main, "all_items.mobs", false);

        if (player.isOp()) {
            player.addAttachment(Main, "all_items.items", true);
            player.addAttachment(Main, "all_items.mobs", true);
            return;
        }

        if (Main.data.item_toggle) {
            player.addAttachment(Main, "all_items.items", true);
        }
        if (Main.data.mob_toggle) {
            player.addAttachment(Main, "all_items.mobs", true);
        }
    }

    //Sets the permissions of every player
    public void set_all_permissions() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            set_permissions(p);
        }
    }
}
