package de.dariusmewes.TimoliaCustom.commands;


import org.bukkit.command.CommandSender;

import de.dariusmewes.TimoliaCustom.TimoliaCustom;

public class tbreload extends TCommand {

	private TimoliaCustom plugin;

	public tbreload(TimoliaCustom plugin) {
		this.plugin = plugin;
		setName("tbreload");
		setPermission("timolia.admin");
		setMaxArgs(0);
		setUsage("/tbreload");
	}

	public void perform(CommandSender sender, String[] args) {
		plugin.reloadConfig();
		sender.sendMessage(prefix + "Konfiguration neu geladen!");
	}
}