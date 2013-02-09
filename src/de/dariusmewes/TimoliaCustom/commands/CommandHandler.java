package de.dariusmewes.TimoliaCustom.commands;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.dariusmewes.TimoliaCustom.Message;
import de.dariusmewes.TimoliaCustom.TimoliaCustom;

public class CommandHandler {

	private static List<TCommand> commands = new ArrayList<TCommand>();

	public static void handleCommand(CommandSender sender, Command cmd, String args[]) {
		TCommand c = getCommand(cmd);
		if (!isvalidCommand(sender, c, args))
			return;

		c.perform(sender, args);
	}

	public static boolean isvalidCommand(CommandSender sender, TCommand cmd, String args[]) {
		if (cmd.onlyIngame() && !(sender instanceof Player)) {
			sender.sendMessage(Message.INGAME);
			return false;
		}

		if (!cmd.getPermission().equalsIgnoreCase("") && !sender.hasPermission("tcore." + cmd.getPermission())) {
			sender.sendMessage(Message.NOPERM);
			return false;
		}

		if (args.length >= cmd.getMinArgs() && (args.length <= cmd.getMaxArgs() || cmd.getMaxArgs() == -1))
			return true;

		else
			sender.sendMessage(cmd.getUsage() != null ? cmd.getUsage() : TimoliaCustom.PREFIX + "Falsche Benutzung");
		return false;
	}

	public static void addCommand(TCommand command) {
		commands.add(command);
	}

	public static TCommand getCommand(Command cmd) {
		for (TCommand c : commands) {
			if (cmd.getName().equalsIgnoreCase(c.getName()))
				return c;
		}
		return null;
	}
}
