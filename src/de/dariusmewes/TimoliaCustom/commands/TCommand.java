/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.dariusmewes.TimoliaCustom.commands;

import org.bukkit.command.CommandSender;

import de.dariusmewes.TimoliaCustom.TimoliaCustom;

public abstract class TCommand {

	private String name;
	private String permission = "";
	private String desc = "";
	private boolean onlyIngame = false;
	private int minArgs = 0;
	private int maxArgs = -1;
	private String usage = "";
	protected static String prefix = TimoliaCustom.PREFIX;
	protected static TimoliaCustom instance;
	private static final String PERMISSION_PREFIX = "tcustom.";

	public TCommand(String name) {
		this.name = name;
		this.permission = PERMISSION_PREFIX + name;
	}

	public abstract void perform(CommandSender sender, String[] args);

	protected void setPermission(String permission) {
		this.permission = permission;
	}

	protected void setDesc(String description) {
		this.desc = description;
	}

	protected void setIngame() {
		this.onlyIngame = true;
	}

	protected void setMinArgs(int minArgs) {
		this.minArgs = minArgs;
	}

	protected void setMaxArgs(int string) {
		this.maxArgs = string;
	}

	protected void setUsage(String text) {
		this.usage = text;
	}

	static void setPluginInstance(TimoliaCustom instance) {
		TCommand.instance = instance;
	}

	String getName() {
		return this.name;
	}

	String getPermission() {
		return this.permission;
	}

	String getDescription() {
		return this.desc;
	}

	boolean onlyIngame() {
		return onlyIngame;
	}

	int getMinArgs() {
		return this.minArgs;
	}

	int getMaxArgs() {
		return this.maxArgs;
	}

	String getUsage() {
		return "Benutzung: " + this.usage;
	}

	String getCleanUsage() {
		return this.usage;
	}

}