/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.dariusmewes.TimoliaCustom.commands;

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

import de.dariusmewes.TimoliaCustom.TimoliaCustom;

public class checkent extends TCommand {

	public checkent(String name) {
		super(name);
		setIngame();
		setMinArgs(1);
		setMaxArgs(1);
		setUsage("/checkent [args]");
		setDesc("Pruefe die Entities auf einem bestimmten Grundstueck");
	}

	public void perform(CommandSender sender, String[] args) {
		Player p = (Player) sender;

		WorldGuardPlugin wg = TimoliaCustom.getWorldGuard();
		if (wg == null) {
			sender.sendMessage(prefix + "WorldGuard ist nicht geladen");
			return;
		}

		World world = p.getWorld();

		RegionManager man = wg.getRegionManager(world);
		if (man == null) {
			sender.sendMessage(prefix + "Regionen für die Welt sind abgeschaltet");
			return;
		}

		Map<String, ProtectedRegion> regions = man.getRegions();
		if (!regions.containsKey(args[0])) {
			sender.sendMessage(prefix + "Die Region wurde nicht gefunden!");
			return;
		}

		ProtectedRegion rg = regions.get(args[0]);
		int lcount = 0;
		int count = 0;
		for (Entity ent : p.getWorld().getEntities()) {
			Location loc = ent.getLocation();
			if (rg.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))
				if (ent instanceof LivingEntity)
					lcount++;
				else
					count++;
		}

		sender.sendMessage(prefix + lcount + " Monster/Tiere und " + count + " andere Entities!");
	}
	
}