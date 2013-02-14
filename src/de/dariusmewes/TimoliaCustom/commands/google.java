/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.dariusmewes.TimoliaCustom.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.dariusmewes.TimoliaCustom.Message;

public class google extends TCommand {

	public google(String name) {
		super(name);
		setMinArgs(1);
		setUsage("/google <msg>");
		setDesc("");
	}

	public void perform(CommandSender sender, String[] args) {
		String msg = "";
		for (int i = 0; i < args.length; i++)
			msg += args[i] + " ";

		msg = ChatColor.translateAlternateColorCodes('&', msg);
		String google = ChatColor.BLUE + "G" + ChatColor.DARK_RED + "o" + ChatColor.YELLOW + "o" + ChatColor.BLUE + "g" + ChatColor.GREEN + "l" + ChatColor.DARK_RED + "e " + ChatColor.WHITE;
		Message.online(google + msg);
		Message.console("[GOOGLE] " + msg);
	}

}