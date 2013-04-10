/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.timolia.custom.TimoliaCustom;

public final class checkent extends TCommand {

	protected void prepare() {
		permission();
		ingame();
		minArgs(1);
		maxArgs(1);
	}

	public void perform(final CommandSender sender, String[] args) {
		final Player p = (Player) sender;

		final WorldGuardPlugin wg = TimoliaCustom.getWorldGuard();
		if (wg == null) {
			sender.sendMessage(prefix + "WorldGuard ist nicht geladen");
			return;
		}

		final World world = p.getWorld();

		final RegionManager man = wg.getRegionManager(world);
		if (man == null) {
			sender.sendMessage(prefix + "Regionen für die Welt sind abgeschaltet");
			return;
		}

		final Map<String, ProtectedRegion> regions = man.getRegions();
		if (!regions.containsKey(args[0])) {
			sender.sendMessage(prefix + "Die Region wurde nicht gefunden!");
			return;
		}

		final ProtectedRegion rg = regions.get(args[0]);
		int lcount = 0;
		int count = 0;
		for (Entity ent : p.getWorld().getEntities()) {
			final Location loc = ent.getLocation();
			if (rg.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))
				if (ent instanceof LivingEntity)
					lcount++;
				else
					count++;
		}

		sender.sendMessage(prefix + lcount + " Monster/Tiere und " + count + " andere Entities!");
	}

}