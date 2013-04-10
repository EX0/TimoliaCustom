/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.timolia.custom.Message;

public final class google extends TCommand {

	protected void prepare() {
		permission();
		minArgs(1);
	}

	public void perform(final CommandSender sender, String[] args) {
		String msg = "";
		for (int i = 0; i < args.length; i++)
			msg += args[i] + " ";

		msg = ChatColor.translateAlternateColorCodes('&', msg);
		final String google = ChatColor.BLUE + "G" + ChatColor.DARK_RED + "o" + ChatColor.YELLOW + "o" + ChatColor.BLUE + "g" + ChatColor.GREEN + "l" + ChatColor.DARK_RED + "e " + ChatColor.WHITE;
		Message.online(google + msg);
		Message.console("[GOOGLE] " + msg);
	}

}