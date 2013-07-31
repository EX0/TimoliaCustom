/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.timolia.custom.TimoliaCustom;

public class tprg extends TCommand {

	protected void prepare() {
		permission();
		ingame();
		minArgs(1);
		maxArgs(1);
	}

	public void perform(CommandSender sender, String[] args) {
		WorldGuardPlugin wgpl = TimoliaCustom.getWorldGuard();
		if (wgpl == null) {
			sender.sendMessage(prefix + "Das WorldGuard Plugin konnte nicht gefunden werden!");
			return;
		}

		Player p = (Player) sender;

		ProtectedRegion rg = WGBukkit.getRegionManager(p.getWorld()).getRegion(args[0]);
		if (rg == null) {
			sender.sendMessage(prefix + "Es konnte keine Region mit dem Namen '" + args[0] + "' gefunden!");
			return;
		}
		int x = rg.getMinimumPoint().getBlockX();
		int z = rg.getMinimumPoint().getBlockZ();
		int y = p.getWorld().getHighestBlockYAt(x, z);

		p.teleport(new Location(p.getWorld(), x, y, z));
	}

}