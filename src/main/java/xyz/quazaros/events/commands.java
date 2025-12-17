package xyz.quazaros.events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.quazaros.main;
import xyz.quazaros.player.player;

public class commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {return true;}

        player pl = main.getPlugin().PlayerList.getPlayer(p.getName());

        if (command.getName().equalsIgnoreCase("alist")) {
            pl.invItt = 0;
            main.getPlugin().Inventory.set_inventory();
            p.openInventory(main.getPlugin().Inventory.inventory_list.get(0));
        }

        return true;
    }
}
