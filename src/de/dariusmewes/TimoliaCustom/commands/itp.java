package de.dariusmewes.TimoliaCustom.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.dariusmewes.TimoliaCustom.Message;

public class itp extends TCommand {

	public itp() {
		setName("itp");
		setPermission("timolia.itp");
		setMinArgs(3);
		setMaxArgs(4);
		setUsage("/itp [Player] <x> <y> <z>");
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
		} catch (NumberFormatException e) {
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