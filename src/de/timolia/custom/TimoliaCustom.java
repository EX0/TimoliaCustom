/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.timolia.custom.cmds.CommandHandler;
import de.timolia.custom.cmds.protnpc;
import de.timolia.custom.cmds.sapopvp;
import de.timolia.custom.cmds.west;
import de.timolia.custom.events.EntityListener;
import de.timolia.custom.events.InventoryListener;
import de.timolia.custom.events.PlayerListener;
import de.timolia.custom.events.ProjectileListener;

public class TimoliaCustom extends JavaPlugin {

	public static final String PREFIX = ChatColor.GREEN + "[TCustom] " + ChatColor.WHITE;
	public static File dataFolder;
	public static boolean coding = false;

	public void onEnable() {
		CommandHandler.init(this);
		initEventHandlers();
		initConfig();
		dataFolder = getDataFolder();

		sapopvp.updateArena();
		protnpc.load();
		west.loadWatchedPlayers();
		west.repeatScanning();
		ProjectileListener.addPoisonousArrowRecipe();
		PlayerListener.addPocketWorkbenchRecipe();
	}

	public void onDisable() {
		west.saveWatchedPlayers();
		west.stopScanning();
		protnpc.save();
	}

	private void initEventHandlers() {
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
		Bukkit.getPluginManager().registerEvents(new ProjectileListener(), this);
	}

	private void initConfig() {
		FileConfiguration conf = getConfig();
		conf.addDefault("sapopvp.pos1", "");
		conf.addDefault("sapopvp.pos2", "");
		conf.addDefault("sapopvp.warpgold", "");
		conf.addDefault("sapopvp.warpeisen", "");
		conf.addDefault("westwatchwelt", "timolia");
		conf.addDefault("sgamesdeathmsg", true);
		conf.options().copyDefaults(true);
		saveConfig();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		CommandHandler.handleCommand(sender, cmd, args);
		return true;
	}

	public static WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");

		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null;
		}

		return (WorldGuardPlugin) plugin;
	}

	public static void logError(String err) {
		try {
			File file = new File(TimoliaCustom.dataFolder + File.separator + "errors.txt");
			BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
			String date = new SimpleDateFormat("dd. MMM. yyyy, HH:mm").format(System.currentTimeMillis());
			output.append(date + "\t" + err + "\n");
			output.close();
			Message.console("Error logged!");
		} catch (Exception e) {

		}
	}

}