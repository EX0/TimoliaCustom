/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import de.timolia.custom.Message;
import de.timolia.custom.TimoliaCustom;
import de.timolia.custom.cmds.sapopvp;
import de.timolia.custom.cmds.west;

public class PlayerListener implements Listener {

	private TimoliaCustom plugin;

	public PlayerListener(TimoliaCustom plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		// PVP Arena Team entfernen
		sapopvp.gold.remove(event.getEntity().getName());
		sapopvp.eisen.remove(event.getEntity().getName());

		String vanillaMsg = event.getDeathMessage();
		if (event.getEntity().getWorld().getName().equalsIgnoreCase("sgames") && plugin.getConfig().getBoolean("sgamesdeathmsg"))
			event.setDeathMessage(ChatColor.DARK_RED + vanillaMsg);
		else
			event.setDeathMessage(ChatColor.DARK_GRAY + vanillaMsg);
	}

	// hubfly
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWorldChange(PlayerChangedWorldEvent event) {
		Player p = event.getPlayer();

		if (p.getWorld().getName().equalsIgnoreCase("HUB")) {
			p.setAllowFlight(true);
			p.sendMessage(ChatColor.RED + "Du hast nun ein JetPack");
		} else if ((event.getFrom().getName().equalsIgnoreCase("HUB")) && (event.getPlayer().getGameMode() != GameMode.CREATIVE)) {
			p.setAllowFlight(false);
			p.sendMessage(ChatColor.RED + "Du hast kein JetPack mehr :(");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		// hubfly
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase("HUB")) {
			event.getPlayer().setAllowFlight(true);
			event.getPlayer().sendMessage(ChatColor.RED + "Du hast nun ein JetPack");
		}

		// westwatch
		if (west.watched.containsKey(event.getPlayer().getName().toLowerCase())) {
			west.watched.put(event.getPlayer().getName().toLowerCase(), System.currentTimeMillis());
			Message.console("Datum fuer " + event.getPlayer().getName() + " wurde aktualisiert!");
		}
	}

}