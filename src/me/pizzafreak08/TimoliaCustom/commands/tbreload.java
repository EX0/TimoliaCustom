package me.pizzafreak08.TimoliaCustom.commands;

import me.pizzafreak08.TimoliaCustom.TimoliaCustom;

import org.bukkit.command.CommandSender;

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