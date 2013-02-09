package de.dariusmewes.TimoliaCustom.commands;


import org.bukkit.command.CommandSender;

import de.dariusmewes.TimoliaCustom.TimoliaCustom;

public abstract class TCommand {

	protected String prefix = TimoliaCustom.PREFIX;
	private String name;
	private String permission = "";
	private boolean onlyIngame = false;
	private int minArgs = 0;
	private int maxArgs = -1;
	protected String usage;

	public abstract void perform(CommandSender sender, String[] args);

	public void setName(String name) {
		this.name = name;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public void setIngame() {
		this.onlyIngame = true;
	}

	public void setMinArgs(int minArgs) {
		this.minArgs = minArgs;
	}

	public void setMaxArgs(int string) {
		this.maxArgs = string;
	}

	public void setUsage(String text) {
		this.usage = prefix + "Benutzung: " + text;
	}

	public String getName() {
		return this.name;
	}

	public String getPermission() {
		return this.permission;
	}

	public boolean onlyIngame() {
		return onlyIngame;
	}

	public int getMinArgs() {
		return this.minArgs;
	}

	public int getMaxArgs() {
		return this.maxArgs;
	}

	public String getUsage() {
		return this.usage;
	}

}