/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.timolia.custom.Message;
import de.timolia.custom.TimoliaCustom;

public final class west extends TCommand {

	public static Map<String, Long> watched = new HashMap<String, Long>();
	private static BukkitTask task;

	private final String wwPrefix = ChatColor.BLUE + "(Westwatch) " + ChatColor.WHITE;

	protected void prepare() {
		permission();
		minArgs(1);
		maxArgs(2);
	}

	public void perform(final CommandSender sender, String args[]) {
		if (args[0].equalsIgnoreCase("list")) {
			final Iterator<String> iter = west.watched.keySet().iterator();

			if (args.length == 2 && args[1].equalsIgnoreCase("-i"))
				sender.sendMessage(wwPrefix + "Liste aller Spieler:");
			else
				sender.sendMessage(wwPrefix + "Liste inaktiver Spieler:");

			final SimpleDateFormat sdf = new SimpleDateFormat("dd. MMM. yyyy, HH:mm");

			while (iter.hasNext()) {
				final String key = (String) iter.next();
				final long value = west.watched.get(key);

				boolean toolong = false;

				final Calendar c1 = Calendar.getInstance();
				c1.setTimeInMillis(value);
				c1.add(Calendar.MONTH, 1);
				final Calendar c2 = Calendar.getInstance();

				if (c2.after(c1))
					toolong = true;

				if (args.length == 2 && args[1].equalsIgnoreCase("-i")) {
					if (toolong)
						sender.sendMessage(key + ": " + ChatColor.RED + sdf.format(value));
					else
						sender.sendMessage(key + ": " + ChatColor.GREEN + sdf.format(value));

				} else if (toolong)
					sender.sendMessage(key + ": " + sdf.format(value));
			}
		}

		else if (args[0].equalsIgnoreCase("scan")) {
			final WorldGuardPlugin wg = TimoliaCustom.getWorldGuard();
			if (wg == null) {
				sender.sendMessage(wwPrefix + "WorldGuard ist nicht geladen");
				return;
			}

			final World world = Bukkit.getWorld(instance.getConfig().getString("westwatchwelt"));

			if (world == null) {
				sender.sendMessage(wwPrefix + "Invalide Welt");
				return;
			}

			final RegionManager man = wg.getRegionManager(world);
			if (man == null) {
				sender.sendMessage(wwPrefix + "Regionen für die Welt sind abgeschaltet");
				return;
			}

			final Map<String, ProtectedRegion> regions = man.getRegions();
			final Iterator<String> iter = regions.keySet().iterator();
			int count = 0;

			while (iter.hasNext()) {
				String name = iter.next();

				name = name.replaceFirst("_", "%%");
				final String[] data = name.split("%%");

				if (data.length == 2 && data[0].equalsIgnoreCase("west")) {
					if (west.watched.containsKey(data[1].toLowerCase()))
						continue;

					west.watched.put(data[1].toLowerCase(), System.currentTimeMillis());
					count++;
				}
			}

			sender.sendMessage(wwPrefix + count + " Spieler wurden der Liste hinzugefügt");
		}

		else if (args[0].equalsIgnoreCase("save"))
			if (saveWatchedPlayers())
				sender.sendMessage(wwPrefix + "Liste wurde gespeichert");
			else
				sender.sendMessage(wwPrefix + "Fehler beim Speichern der Liste");

		else if (args[0].equalsIgnoreCase("load"))
			if (loadWatchedPlayers())
				sender.sendMessage(wwPrefix + "Liste wurde geladen");
			else
				sender.sendMessage(wwPrefix + "Fehler beim Laden der Liste");

		else if (args[0].equalsIgnoreCase("purge")) {
			final WorldGuardPlugin wg = TimoliaCustom.getWorldGuard();
			if (wg == null) {
				sender.sendMessage(wwPrefix + "WorldGuard ist nicht geladen");
				return;
			}

			final World world = Bukkit.getWorld(instance.getConfig().getString("westwatchwelt"));

			if (world == null) {
				sender.sendMessage(wwPrefix + "Invalide Welt");
				return;
			}

			final RegionManager man = wg.getRegionManager(world);
			if (man == null) {
				sender.sendMessage(wwPrefix + "Regionen f�r die Welt sind abgeschaltet");
				return;
			}

			final Map<String, ProtectedRegion> regions = man.getRegions();
			final Iterator<String> iter = regions.keySet().iterator();
			final List<String> in = new ArrayList<String>();

			while (iter.hasNext()) {
				String name = iter.next();
				name = name.replaceFirst("_", "%%");
				final String[] data = name.split("%%");
				if (data.length == 2 && data[0].equalsIgnoreCase("west"))
					in.add(data[1]);
			}

			final Iterator<String> iterate = west.watched.keySet().iterator();
			final List<String> removes = new ArrayList<String>();
			while (iterate.hasNext()) {
				final String key = (String) iterate.next();
				if (!in.contains(key))
					removes.add(key);
			}

			int count = 0;
			for (final String remove : removes) {
				west.watched.remove(remove);
				count++;
			}

			sender.sendMessage(wwPrefix + count + " Spieler wurden von der Liste entfernt");
		}

		else if (args[0].equalsIgnoreCase("clear")) {
			west.watched.clear();
			sender.sendMessage(wwPrefix + "Die Liste wurde geleert!");

		} else {

			if (args.length != 2) {
				displayHelp(sender);
				return;
			}

			if (args[0].equalsIgnoreCase("add")) {
				String target = args[1].toLowerCase();
				if (west.watched.containsKey(target))
					sender.sendMessage(wwPrefix + target + " ist schon auf der Liste.");
				else {
					west.watched.put(target, System.currentTimeMillis());
					sender.sendMessage(wwPrefix + target + " wurde der Liste hinzugefügt.");
				}
			}

			else if (args[0].equalsIgnoreCase("remove")) {
				String target = args[1].toLowerCase();
				if (west.watched.containsKey(target)) {
					west.watched.remove(target);
					sender.sendMessage(wwPrefix + target.toLowerCase() + " wurde von der Liste entfernt.");
				} else
					sender.sendMessage(wwPrefix + target.toLowerCase() + " ist nicht auf der Liste.");
			}

			else if (args[0].equalsIgnoreCase("update")) {
				if (!west.watched.containsKey(args[1].toLowerCase())) {
					sender.sendMessage(wwPrefix + args[1] + " ist nicht auf der Liste!");
					return;
				}

				west.watched.put(args[1].toLowerCase(), System.currentTimeMillis());
				sender.sendMessage(wwPrefix + "Die Zeit von " + args[1].toLowerCase() + " wurde aktualisiert!");

			} else
				displayHelp(sender);
		}
	}

	private void displayHelp(final CommandSender sender) {
		sender.sendMessage("******" + ChatColor.BLUE + "(Westwatch)" + ChatColor.WHITE + "******");
		sender.sendMessage("Zu benutzen wie folgt: /west <Parameter>");
		sender.sendMessage("Parameter:");
		sender.sendMessage("add <Spieler> - Fügt der Liste den Spieler hinzu");
		sender.sendMessage("remove <Spieler> - Nimmt den Spieler von der Liste");
		sender.sendMessage("list [-i] - Zeigt alle inaktiven Spieler. [-i] zeigt alle Spieler");
		sender.sendMessage("scan - Scannt Worldguard");
		sender.sendMessage("purge - Löscht überflüssige Einträge");
		sender.sendMessage("save - Speichert die Liste");
		sender.sendMessage("load - Lädt die Liste neu");
		sender.sendMessage("clear - Leert die Liste");
		sender.sendMessage("update - Aktualisiert die Zeit eines Spielers auf der Liste");
	}

	public static boolean saveWatchedPlayers() {
		try {
			final File file = new File(TimoliaCustom.dataFolder + File.separator + "watched.custom");
			final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(west.watched);
			oos.flush();
			oos.close();
			return true;
		} catch (final Exception e) {
			Message.console("Fehler beim Speichern der Westliste: " + e.getMessage());
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean loadWatchedPlayers() {
		try {
			final File file = new File(TimoliaCustom.dataFolder + File.separator + "watched.custom");
			if (!file.exists())
				return false;

			final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			west.watched = (HashMap<String, Long>) ois.readObject();
			ois.close();
			return true;
		} catch (final Exception e) {
			Message.console("Fehler beim Laden der Westliste: " + e.getMessage());
			return false;
		}
	}

	public static void repeatScanning() {
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new Runnable() {
			public void run() {
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "west scan");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "west purge");
				Message.certain(ChatColor.BLUE + "(WestWatch) " + ChatColor.WHITE + "Die Westliste wurde aktualisiert", "tcustom.west.notify");
			}
		}, 1200L, 72000L);
	}

	public static void stopScanning() {
		if (task != null)
			task.cancel();
	}

}