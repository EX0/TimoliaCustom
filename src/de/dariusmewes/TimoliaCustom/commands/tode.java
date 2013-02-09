package de.dariusmewes.TimoliaCustom.commands;

import java.util.Vector;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.dariusmewes.TimoliaCustom.Message;

public class tode extends TCommand {

	public static Vector<String> inactive = new Vector<String>();

	public tode() {
		setName("tode");
		setPermission("timolia.tode");
		setIngame();
		setMaxArgs(1);
		setUsage("/tode");
	}

	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {
			if (!sender.hasPermission("timolia.tode.clear")) {
				sender.sendMessage(Message.NOPERM);
				return;
			}

			inactive.clear();
			sender.sendMessage(prefix + "Todesmeldungen für alle Spieler aktiviert!");
			return;
		}

		Player p = (Player) sender;
		if (inactive.contains(p.getName())) {
			inactive.remove(p.getName());
			sender.sendMessage(prefix + "Todesmeldungen aktiviert");
		} else {
			inactive.add(p.getName());
			sender.sendMessage(prefix + "Todesmeldungen deaktiviert");
		}
	}
}