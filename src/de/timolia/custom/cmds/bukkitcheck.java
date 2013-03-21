/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import org.bukkit.command.CommandSender;

import de.timolia.custom.BukkitCheck;

public class bukkitcheck extends TCommand {

	public bukkitcheck(String name) {
		super(name);
		setPermission("tcustom.bukkitcheck");
		setMaxArgs(0);
		setUsage("/bukkitcheck");
		setDesc("Pruefe ob ein neuer Bukkit-Beta Build draussen ist.");
	}

	public void perform(CommandSender sender, String[] args) {
		BukkitCheck.action();
	}

}