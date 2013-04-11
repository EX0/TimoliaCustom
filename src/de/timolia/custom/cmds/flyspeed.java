/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class flyspeed extends TCommand {

	@Override
	protected void prepare() {
		permission();
		ingame();
		minArgs(1);
		maxArgs(1);
	}

	@Override
	public void perform(final CommandSender sender, String[] args) {
		Player p = (Player) sender;
		Float speed;
		try {
			speed = Float.valueOf(args[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage(prefix + args[0] + " ist keine gültige Zahl!");
			return;
		}

		p.setFlySpeed(speed);
	}

}