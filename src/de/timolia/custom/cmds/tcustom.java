/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import org.bukkit.command.CommandSender;

import de.timolia.custom.TimoliaCustom;

public class tcustom extends TCommand {

	public tcustom(String name) {
		super(name);
		setMinArgs(1);
		setMaxArgs(1);
		setUsage("/tcustom <reload>");
		setDesc("Plugin-Einstellungen");
	}

	public void perform(CommandSender sender, String[] args) {
		if (args[0].equalsIgnoreCase("reload")) {
			instance.reloadConfig();
			sender.sendMessage(prefix + "Konfiguration neu geladen!");
		} else if (args[0].equalsIgnoreCase("debug") && TimoliaCustom.coding)
			CommandHandler.list();
		else
			sender.sendMessage(getUsage());
	}

}