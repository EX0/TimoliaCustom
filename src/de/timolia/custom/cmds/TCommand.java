/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.timolia.custom.TimoliaCustom;

public abstract class TCommand implements CommandExecutor {

	final protected static String prefix = TimoliaCustom.PREFIX;
	final protected static String PERMISSION_PREFIX = "tcustom";
	protected static TimoliaCustom instance;

	private String name;
	private String permission = "";
	private boolean ingame = false;
	private int minArgs = 0;
	private int maxArgs = -1;
	protected String usage = "";

	public static void add(String commandName, TCommand tCmd) {
		tCmd.name = commandName;
		instance.getCommand(commandName).setExecutor(tCmd);
	}

	public static void setPluginInstance(TimoliaCustom instance) {
		TCommand.instance = instance;
	}

	public TCommand() {
		prepare();
	}

	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, String[] args) {
		if (!permission.equalsIgnoreCase("") && !sender.hasPermission(permission)) {
			sender.sendMessage(prefix + "You don't have permission!");
			return true;
		}

		if (ingame && !(sender instanceof Player)) {
			sender.sendMessage(prefix + "This command can only be executed from ingame!");
			return false;
		}

		if (cmd.getUsage() != null)
			this.usage = new StringBuilder(prefix).append(cmd.getUsage()).toString();

		if (args.length >= minArgs && (args.length <= maxArgs || maxArgs == -1))
			perform(sender, args);

		else if (usage.equalsIgnoreCase(""))
			sender.sendMessage(prefix + "Wrong argument count!");

		else
			sender.sendMessage(usage);

		return true;
	}

	protected abstract void prepare();

	public abstract void perform(final CommandSender sender, String[] args);

	protected void permission() {
		this.permission(this.name);
	}

	protected void permission(String permission) {
		this.permission = new StringBuilder(PERMISSION_PREFIX).append(permission).toString();
	}

	protected void ingame() {
		this.ingame = true;
	}

	protected void minArgs(int minArgs) {
		this.minArgs = minArgs;
	}

	protected void maxArgs(int maxArgs) {
		this.maxArgs = maxArgs;
	}

}