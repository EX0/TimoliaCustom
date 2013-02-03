package me.pizzafreak08.TimoliaCustom.commands;

import me.pizzafreak08.TimoliaCustom.Message;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class google extends TCommand {

	public google() {
		setName("google");
		setPermission("timolia.google");
		setMinArgs(1);
		setUsage("/google <msg>");
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