/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import de.timolia.custom.Message;
import de.timolia.custom.TimoliaCustom;

public final class protnpc extends TCommand {

	public static final List<String> active = new ArrayList<String>();
	public static List<UUID> prot = new ArrayList<UUID>();

	protected void prepare() {
		permission();
		ingame();
		maxArgs(1);
	}

	public void perform(final CommandSender sender, String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase("clear")) {
			prot.clear();
			sender.sendMessage(prefix + "Alle geschützten NPCs sind nun attackierbar");
			return;
		}

		final Player p = (Player) sender;
		if (!active.contains(p.getName()))
			active.add(p.getName());
		sender.sendMessage(prefix + "Bitte klicke auf einen Villager um ihn zu protecten!");
	}

	public static void addName(final String name, final Villager npc) {
		try {
			final Location loc = npc.getLocation();
			final String loca = loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
			final File file = new File(TimoliaCustom.dataFolder + File.separator + "schlaeger.txt");
			final BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
			final String date = new SimpleDateFormat("dd. MMM. yyyy, HH:mm").format(System.currentTimeMillis());
			output.append(date + "\t" + name + "\t" + loca + "\t" + npc.getProfession().name());
			output.newLine();
			output.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		try {
			final File file = new File(TimoliaCustom.dataFolder + File.separator + "uuids.custom");
			final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(prot);
			oos.close();
		} catch (final Exception e) {
			Message.console("Fehler beim Speichern der protecteten NPCs: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static void load() {
		try {
			final File file = new File(TimoliaCustom.dataFolder + File.separator + "uuids.custom");
			if (file.exists()) {
				final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				prot = (List<UUID>) ois.readObject();
				ois.close();
			}
		} catch (final Exception e) {
			Message.console("Fehler beim Laden der protecteten NPCs: " + e.getMessage());
		}
	}

}