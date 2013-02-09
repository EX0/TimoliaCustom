package de.dariusmewes.TimoliaCustom.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class sgames extends TCommand {

	public sgames() {
		setName("sgames");
		setPermission("timolia.sgames");
		setMaxArgs(0);
		setUsage("/sgames");
	}

	public void perform(CommandSender sender, String[] args) {
		World sgames = Bukkit.getWorld("sgames");

		if (sgames == null) {
			sender.sendMessage(prefix + "Die Welt ist nicht geladen!");
			return;
		}

		String msg = "";
		int count = 0;
		for (Player ingame : sgames.getPlayers()) {
			if (ingame.getGameMode() == GameMode.SURVIVAL) {
				msg += ingame.getName() + " ";
				count++;
			}
		}

		if (count == 0) {
			sender.sendMessage(prefix + "Es sind keine Tribute in der Survival-Games Arena.");
			return;
		}

		sender.sendMessage(prefix + "Tribute(" + count + "): " + msg);
	}
}