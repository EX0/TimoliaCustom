/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.dariusmewes.TimoliaCustom.events;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import de.dariusmewes.TimoliaCustom.Message;
import de.dariusmewes.TimoliaCustom.TimoliaCustom;
import de.dariusmewes.TimoliaCustom.commands.sapopvp;
import de.dariusmewes.TimoliaCustom.commands.west;

public class PlayerListener implements Listener {

	private TimoliaCustom plugin;
	private static String linkKeys = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static String coreURL = "http://127.01/s/";

	public PlayerListener(TimoliaCustom plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		// link shortening
		String msg = event.getMessage();
		if (msg.contains("http") || msg.contains("www.") || msg.contains(".de") || msg.contains(".com")) {
			try {
				String[] parts = msg.split(" ");
				for (int i = 0; i < parts.length; i++)
					if (parts[i].contains("http") || parts[i].contains("www.") || parts[i].contains(".de") || parts[i].contains(".com")) {
						String tUrl = parts[i];
						tUrl = tUrl.replaceFirst("http://", "");
						tUrl = tUrl.replaceFirst("https://", "");
						tUrl = tUrl.replaceFirst("www.", "");
						if (tUrl.endsWith("/"))
							tUrl = tUrl.substring(0, tUrl.length() - 1);

						if (tUrl.length() > 20) {
							tUrl = parts[i];
							String hash = "";
							Random rand = new Random();
							for (int j = 0; j < 4; j++)
								hash += linkKeys.charAt(rand.nextInt(linkKeys.length()));

							URL url = new URL(coreURL + "add.php");
							URLConnection con = url.openConnection();
							con.setDoOutput(true);
							OutputStream out = con.getOutputStream();
							String data = "hash=" + hash + "&user=" + event.getPlayer().getName() + "&url=" + tUrl;
							out.write(data.getBytes());
							// debug
							Bukkit.broadcastMessage(data);
							out.flush();
							out.close();

							// InputStream in = con.getInputStream();
							// BufferedReader reader = new BufferedReader(new
							// InputStreamReader(in));
							// String line;
							// while ((line = reader.readLine()) != null) {
							// Bukkit.broadcastMessage(line);
							// }
							// in.close();
							// reader.close();

							parts[i] = "timolia.de/s/?i=" + hash;
						}

					}

				String out = "";
				for (int k = 0; k < parts.length; k++)
					out += parts[k] + " ";

				event.setMessage(out);
			} catch (Exception e) {
				Message.console("Fehler beim LinkkŸrzen: " + e.getMessage());
			}
		}
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