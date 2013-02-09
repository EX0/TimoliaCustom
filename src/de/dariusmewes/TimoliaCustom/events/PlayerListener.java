package de.dariusmewes.TimoliaCustom.events;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import de.dariusmewes.TimoliaCustom.Message;
import de.dariusmewes.TimoliaCustom.TimoliaCustom;
import de.dariusmewes.TimoliaCustom.commands.tode;
import de.dariusmewes.TimoliaCustom.commands.west;

public class PlayerListener implements Listener {

	private TimoliaCustom plugin;

	public PlayerListener(TimoliaCustom plugin) {
		this.plugin = plugin;
	}

	// instant damage stacks verbieten
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer().getItemInHand().getType() == Material.POTION && event.getPlayer().getItemInHand().getAmount() > 1) {
			short data = event.getPlayer().getItemInHand().getDurability();
			if (data == 32732 || data == 32764)
				event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		// PVP Arena Team entfernen
		TimoliaCustom.gold.remove(event.getEntity().getName());
		TimoliaCustom.eisen.remove(event.getEntity().getName());

		// DEATH MSG
		String vanillaMsg = event.getDeathMessage();
		event.setDeathMessage("");
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (tode.inactive.contains(p.getName()))
				continue;

			if (event.getEntity().getWorld().getName().equalsIgnoreCase("sgames") && plugin.getConfig().getBoolean("sgamesdeathmsg"))
				p.sendMessage(ChatColor.DARK_RED + vanillaMsg);
			else
				p.sendMessage(ChatColor.DARK_GRAY + vanillaMsg);
		}
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