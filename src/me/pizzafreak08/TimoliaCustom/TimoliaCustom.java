package me.pizzafreak08.TimoliaCustom;

import java.io.File;
import java.util.Vector;

import me.pizzafreak08.TimoliaCustom.commands.CommandHandler;
import me.pizzafreak08.TimoliaCustom.commands.checkent;
import me.pizzafreak08.TimoliaCustom.commands.google;
import me.pizzafreak08.TimoliaCustom.commands.itp;
import me.pizzafreak08.TimoliaCustom.commands.protnpc;
import me.pizzafreak08.TimoliaCustom.commands.sapopvp;
import me.pizzafreak08.TimoliaCustom.commands.sgames;
import me.pizzafreak08.TimoliaCustom.commands.sgcopy;
import me.pizzafreak08.TimoliaCustom.commands.tbreload;
import me.pizzafreak08.TimoliaCustom.commands.tode;
import me.pizzafreak08.TimoliaCustom.commands.west;
import me.pizzafreak08.TimoliaCustom.events.EntityListener;
import me.pizzafreak08.TimoliaCustom.events.InventoryListener;
import me.pizzafreak08.TimoliaCustom.events.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class TimoliaCustom extends JavaPlugin {

	public static final String PREFIX = ChatColor.GREEN + "[Timolia] " + ChatColor.WHITE;
	public static File dataFolder;

	public static Vector<String> tntarrows = new Vector<String>();
	public static Vector<String> gold = new Vector<String>();
	public static Vector<String> eisen = new Vector<String>();

	public void onEnable() {
		initCommands();
		initEventHandlers();
		initConfig();
		dataFolder = getDataFolder();

		sapopvp.updateArena();
		protnpc.load();
		west.loadWatchedPlayers();
		west.repeatScanning();
		// protnpc, potion damage 64
	}

	public void onDisable() {
		west.saveWatchedPlayers();
		protnpc.save();
	}

	private void initCommands() {
		CommandHandler.addCommand(new checkent());
		CommandHandler.addCommand(new google());
		CommandHandler.addCommand(new itp());
		CommandHandler.addCommand(new protnpc());
		CommandHandler.addCommand(new sapopvp(this));
		CommandHandler.addCommand(new sgames());
		CommandHandler.addCommand(new sgcopy());
		CommandHandler.addCommand(new tbreload(this));
		CommandHandler.addCommand(new tode());
		CommandHandler.addCommand(new west(this));
	}

	private void initEventHandlers() {
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
	}

	private void initConfig() {
		getConfig().addDefault("sapopvp.pos1", "");
		getConfig().addDefault("sapopvp.pos2", "");
		getConfig().addDefault("sapopvp.warpgold", "");
		getConfig().addDefault("sapopvp.warpeisen", "");
		getConfig().addDefault("westwatchwelt", "timolia");
		getConfig().addDefault("sgamesdeathmsg", true);

		getConfig().options().copyDefaults(true);
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

}