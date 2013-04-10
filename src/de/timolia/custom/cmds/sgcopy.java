/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public final class sgcopy extends TCommand {

	protected void prepare() {
		permission();
		minArgs(1);
		maxArgs(1);
	}

	public void perform(final CommandSender sender, String[] args) {
		if (Bukkit.getWorld("sgames") != null) {
			sender.sendMessage(prefix + "Die Welt ist noch geladen!");
			return;
		}

		final File file = new File("sg_" + args[0]);
		if (!file.exists() || !file.isDirectory()) {
			sender.sendMessage(prefix + "Der Ordner sg_" + args[0] + " existiert nicht!");
			return;
		}

		new Thread(new Runnable() {
			public void run() {
				try {
					sender.sendMessage(prefix + "Welt wird kopiert!");
					copyFolder(file, new File("sgames"));
					sender.sendMessage(prefix + "Welt kopiert!");
				} catch (final IOException e) {
					sender.sendMessage(prefix + "Fehler beim Kopieren!");
				}
			}
		}).start();
	}

	private static void copyFolder(final File src, final File dest) throws IOException {
		if (src.isDirectory()) {
			if (dest.exists())
				deleteFolder(dest);

			dest.mkdir();

			for (final String file : src.list()) {
				final File srcFile = new File(src, file);
				final File destFile = new File(dest, file);
				copyFolder(srcFile, destFile);
			}

		} else {
			final FileInputStream in = new FileInputStream(src);
			final FileOutputStream out = new FileOutputStream(dest);
			final FileChannel ic = in.getChannel();
			final FileChannel oc = out.getChannel();
			ic.transferTo(0L, ic.size(), oc);
			in.close();
			out.close();
			ic.close();
			oc.close();
		}
	}

	private static void deleteFolder(File folder) {
		final File[] files = folder.listFiles();
		if (files != null)
			for (final File f : files)
				if (f.isDirectory())
					deleteFolder(f);
				else
					f.delete();

		folder.delete();
	}

}