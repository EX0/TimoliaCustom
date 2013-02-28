/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.dariusmewes.TimoliaCustom.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class crash extends TCommand {

	public crash(String name) {
		super(name);
		setMinArgs(1);
		setMaxArgs(1);
	}

	public void perform(CommandSender sender, String[] args) {
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(prefix + args[0] + " ist nicht online!");
			return;
		}
	}

}