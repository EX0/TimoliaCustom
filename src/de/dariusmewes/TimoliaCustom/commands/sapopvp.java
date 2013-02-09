package de.dariusmewes.TimoliaCustom.commands;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import de.dariusmewes.TimoliaCustom.Message;
import de.dariusmewes.TimoliaCustom.TimoliaCustom;
import de.dariusmewes.TimoliaCustom.events.EntityListener;

public class sapopvp extends TCommand {

	private String sprefix = ChatColor.RED + "(UtopiaPVP) " + ChatColor.WHITE;
	private static TimoliaCustom plugin;

	public static Location warpGold;
	public static Location warpEisen;

	public sapopvp(TimoliaCustom plugin) {
		sapopvp.plugin = plugin;
		setName("sapopvp");
		setPermission("timolia.sapopvp");
		setIngame();
		setMaxArgs(1);
		setUsage("/sapopvp [gold/eisen/pos1/pos2/warpgold/warpeisen/list]");
	}

	public void perform(CommandSender sender, String[] args) {
		Player p = (Player) sender;

		if (args.length == 0) {
			FileConfiguration config = plugin.getConfig();

			if (warpEisen == null || warpGold == null || config.getString("sapopvp.pos1").equalsIgnoreCase("") || config.getString("sapopvp.pos2").equalsIgnoreCase("")) {
				p.sendMessage(sprefix + "Arena nicht bereit!");
				return;
			}

			boolean gold = false;

			int goldSize = TimoliaCustom.gold.size();
			int eisenSize = TimoliaCustom.eisen.size();

			if (goldSize > eisenSize)
				gold = false;
			if (eisenSize > goldSize)
				gold = true;
			if (eisenSize == goldSize)
				gold = new java.util.Random().nextInt(2) == 1 ? true : false;

			portToArena(p, gold);
			return;

		}

		if (args[0].equalsIgnoreCase("gold")) {
			portToArena(p, true);
			return;
		}

		if (args[0].equalsIgnoreCase("eisen")) {
			portToArena(p, false);
			return;
		}

		if (!sender.hasPermission("timolia.sapopvp.admin")) {
			sender.sendMessage(Message.NOPERM);
			return;
		}

		if (args[0].equalsIgnoreCase("pos1") || args[0].equalsIgnoreCase("pos2")) {
			Location loc = p.getTargetBlock(null, 10).getLocation();
			String locparse = loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();

			if (args[0].equalsIgnoreCase("pos1")) {
				plugin.getConfig().set("sapopvp.pos1", locparse);
				plugin.saveConfig();
				p.sendMessage(prefix + "Punkt 1 der Arena gesetzt zu " + locparse);

			} else if (args[0].equalsIgnoreCase("pos2")) {
				plugin.getConfig().set("sapopvp.pos2", locparse);
				plugin.saveConfig();
				p.sendMessage(prefix + "Punkt 2 der Arena gesetzt zu " + locparse);
			}

			updateArena();

		} else if (args[0].equalsIgnoreCase("warpgold") || args[0].equalsIgnoreCase("warpeisen")) {
			Location loc = p.getLocation();
			String locparse = loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," + loc.getYaw() + "," + loc.getPitch();

			if (args[0].equalsIgnoreCase("warpgold")) {
				plugin.getConfig().set("sapopvp.warpgold", locparse);
				plugin.saveConfig();
				p.sendMessage(prefix + "Warp von Team Gold gesetzt");

			} else if (args[0].equalsIgnoreCase("warpeisen")) {
				plugin.getConfig().set("sapopvp.warpeisen", locparse);
				plugin.saveConfig();
				p.sendMessage(prefix + "Warp von Team Eisen gesetzt");
			}

			updateArena();

		} else if (args[0].equalsIgnoreCase("list")) {
			if (TimoliaCustom.gold.size() == 0 && TimoliaCustom.eisen.size() == 0) {
				p.sendMessage(sprefix + "Beide Teams sind leer");
				return;
			}

			String gold = "";
			String eisen = "";

			for (String goldP : TimoliaCustom.gold) {
				gold += goldP + " ";
			}

			for (String eisenP : TimoliaCustom.eisen) {
				eisen += eisenP + " ";
			}

			p.sendMessage(sprefix + "Gold: " + gold);
			p.sendMessage(sprefix + "Eisen: " + eisen);

		} else
			sender.sendMessage(usage);
	}

	private void portToArena(Player p, boolean gold) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setExp(0);
		p.setGameMode(GameMode.SURVIVAL);
		for (PotionEffect pe : p.getActivePotionEffects()) {
			p.removePotionEffect(pe.getType());
		}

		if (gold) {
			sendIngame(sprefix + p.getName() + " ist dem goldenem Team beigetreten!");
			TimoliaCustom.gold.add(p.getName());
			TimoliaCustom.eisen.remove(p.getName());
			p.sendMessage(sprefix + "Du bist dem goldenem Team beigetreten!");

		} else {
			sendIngame(sprefix + p.getName() + " ist dem eisernem Team beigetreten!");
			TimoliaCustom.eisen.add(p.getName());
			TimoliaCustom.gold.remove(p.getName());
			p.sendMessage(sprefix + "Du bist dem eisernem Team beigetreten!");
		}

		p.teleport(gold ? warpGold : warpEisen);
	}

	private void sendIngame(String msg) {
		for (String pl : TimoliaCustom.gold) {
			Player target = Bukkit.getPlayer(pl);
			if (target != null)
				target.sendMessage(msg);
		}

		for (String pl : TimoliaCustom.eisen) {
			Player target = Bukkit.getPlayer(pl);
			if (target != null)
				target.sendMessage(msg);
		}
	}

	public static void updateArena() {
		if (plugin.getConfig().getString("sapopvp.pos1").equalsIgnoreCase("") || plugin.getConfig().getString("sapopvp.pos2").equalsIgnoreCase(""))
			return;

		EntityListener.saveArenaCoords(plugin.getConfig().getString("sapopvp.pos1").split(","), plugin.getConfig().getString("sapopvp.pos2").split(","));

		if (plugin.getConfig().getString("sapopvp.warpgold").equalsIgnoreCase("") || plugin.getConfig().getString("sapopvp.warpeisen").equalsIgnoreCase(""))
			return;

		String[] dataG = plugin.getConfig().getString("sapopvp.warpgold").split(",");
		String[] dataE = plugin.getConfig().getString("sapopvp.warpeisen").split(",");

		warpGold = new Location(Bukkit.getWorld(dataG[0]), Integer.valueOf(dataG[1]), Integer.valueOf(dataG[2]), Integer.valueOf(dataG[3]), Float.valueOf(dataG[4]), Float.valueOf(dataG[5]));
		warpEisen = new Location(Bukkit.getWorld(dataE[0]), Integer.valueOf(dataE[1]), Integer.valueOf(dataE[2]), Integer.valueOf(dataE[3]), Float.valueOf(dataE[4]), Float.valueOf(dataE[5]));
	}
}