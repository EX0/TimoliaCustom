/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.timolia.custom.Message;

public final class itp extends TCommand {

    protected void prepare() {
        permission();
        minArgs(3);
        maxArgs(4);
    }

    public void perform(CommandSender sender, String[] args) {
        // OLD ITP CODE

        Player p;
        if (args.length == 3) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Message.INGAME);
                return;
            }

            p = (Player) sender;
        } else {
            p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                sender.sendMessage(prefix + args[0] + " ist nicht online!");
                return;
            }
        }

        double x, y, z;
        try {
            x = Double.valueOf(args.length == 3 ? args[0].replaceAll(",", ".") : args[1].replaceAll(",", "."));
            y = Double.valueOf(args.length == 3 ? args[1].replaceAll(",", ".") : args[2].replaceAll(",", "."));
            z = Double.valueOf(args.length == 3 ? args[2].replaceAll(",", ".") : args[3].replaceAll(",", "."));
        } catch (final NumberFormatException e) {
            sender.sendMessage(prefix + "Fehler: Keine Zahl");
            return;
        }

        x = x >= 0 ? x + 0.5 : x - 0.5;
        z = z >= 0 ? z + 0.5 : z - 0.5;

        Location loc = new Location(p.getWorld(), x, y, z);

        if (loc.getBlock().getType() != Material.AIR && loc.getBlock().getType() != Material.WATER)
            loc.setY(p.getWorld().getHighestBlockYAt(loc));

        p.teleport(loc);
        sender.sendMessage(prefix + p.getName() + " wurde teleportiert!");
    }

}