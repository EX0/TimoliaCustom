package me.pizzafreak08.TimoliaCustom.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class sgcopy extends TCommand {

	public sgcopy() {
		setName("sgcopy");
		setPermission("timolia.sgcopy");
		setMinArgs(1);
		setMaxArgs(1);
		setUsage("/sgcopy <world>");
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
				} catch (IOException e) {
					sender.sendMessage(prefix + "Fehler beim Kopieren!");
				}
			}
		}).start();
	}

	private static void copyFolder(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			if (dest.exists())
				deleteFolder(dest);

			dest.mkdir();

			for (String file : src.list()) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				copyFolder(srcFile, destFile);
			}

		} else {
			FileInputStream in = new FileInputStream(src);
			FileOutputStream out = new FileOutputStream(dest);
			FileChannel ic = in.getChannel();
			FileChannel oc = out.getChannel();
			ic.transferTo(0L, ic.size(), oc);
			in.close();
			out.close();
			ic.close();
			oc.close();
		}
	}

	private static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null)
			for (File f : files)
				if (f.isDirectory())
					deleteFolder(f);
				else
					f.delete();

		folder.delete();
	}
}