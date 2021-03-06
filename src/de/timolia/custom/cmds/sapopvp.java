/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import de.timolia.custom.Message;
import de.timolia.custom.events.EntityListener;

public final class sapopvp extends TCommand {

    public static final List<String> gold = new ArrayList<String>();
    public static final List<String> eisen = new ArrayList<String>();

    public static Location warpGold;
    public static Location warpEisen;

    private final String sprefix = ChatColor.RED + "(UtopiaPVP) " + ChatColor.WHITE;

    protected void prepare() {
        permission();
        ingame();
        maxArgs(1);
    }

    public void perform(final CommandSender sender, String[] args) {
        final Player p = (Player) sender;

        if (args.length == 0) {
            final FileConfiguration config = instance.getConfig();

            if (warpEisen == null || warpGold == null || config.getString("sapopvp.pos1").equalsIgnoreCase("") || config.getString("sapopvp.pos2").equalsIgnoreCase("")) {
                p.sendMessage(sprefix + "Arena nicht bereit!");
                return;
            }

            boolean gold = false;

            final int goldSize = sapopvp.gold.size();
            final int eisenSize = sapopvp.eisen.size();

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

        if (!sender.hasPermission("tcustom.sapopvp.admin")) {
            sender.sendMessage(Message.NOPERM);
            return;
        }

        if (args[0].equalsIgnoreCase("pos1") || args[0].equalsIgnoreCase("pos2")) {
            final Location loc = p.getTargetBlock(null, 10).getLocation();
            final String locparse = loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();

            if (args[0].equalsIgnoreCase("pos1")) {
                instance.getConfig().set("sapopvp.pos1", locparse);
                instance.saveConfig();
                p.sendMessage(sprefix + "Punkt 1 der Arena gesetzt zu " + locparse);

            } else if (args[0].equalsIgnoreCase("pos2")) {
                instance.getConfig().set("sapopvp.pos2", locparse);
                instance.saveConfig();
                p.sendMessage(sprefix + "Punkt 2 der Arena gesetzt zu " + locparse);
            }

            updateArena();

        } else if (args[0].equalsIgnoreCase("warpgold") || args[0].equalsIgnoreCase("warpeisen")) {
            final Location loc = p.getLocation();
            final String locparse = loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," + loc.getYaw() + "," + loc.getPitch();

            if (args[0].equalsIgnoreCase("warpgold")) {
                instance.getConfig().set("sapopvp.warpgold", locparse);
                instance.saveConfig();
                p.sendMessage(sprefix + "Warp von Team Gold gesetzt");

            } else if (args[0].equalsIgnoreCase("warpeisen")) {
                instance.getConfig().set("sapopvp.warpeisen", locparse);
                instance.saveConfig();
                p.sendMessage(sprefix + "Warp von Team Eisen gesetzt");
            }

            updateArena();

        } else if (args[0].equalsIgnoreCase("list")) {
            if (sapopvp.gold.size() == 0 && sapopvp.eisen.size() == 0) {
                p.sendMessage(sprefix + "Beide Teams sind leer");
                return;
            }

            String gold = "";
            String eisen = "";

            for (String goldP : sapopvp.gold) {
                gold += goldP + " ";
            }

            for (String eisenP : sapopvp.eisen) {
                eisen += eisenP + " ";
            }

            p.sendMessage(sprefix + "Gold: " + gold);
            p.sendMessage(sprefix + "Eisen: " + eisen);

        } else
            sender.sendMessage(usage);
    }

    private void portToArena(final Player p, final boolean gold) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setExp(0);
        p.setGameMode(GameMode.SURVIVAL);
        for (final PotionEffect pe : p.getActivePotionEffects()) {
            p.removePotionEffect(pe.getType());
        }

        if (gold) {
            sendIngame(sprefix + p.getName() + " ist dem goldenem Team beigetreten!");
            sapopvp.gold.add(p.getName());
            sapopvp.eisen.remove(p.getName());
            p.sendMessage(sprefix + "Du bist dem goldenem Team beigetreten!");

        } else {
            sendIngame(sprefix + p.getName() + " ist dem eisernem Team beigetreten!");
            sapopvp.eisen.add(p.getName());
            sapopvp.gold.remove(p.getName());
            p.sendMessage(sprefix + "Du bist dem eisernem Team beigetreten!");
        }

        p.teleport(gold ? warpGold : warpEisen);
    }

    private void sendIngame(String msg) {
        for (String pl : sapopvp.gold) {
            final Player target = Bukkit.getPlayer(pl);
            if (target != null)
                target.sendMessage(msg);
        }

        for (final String pl : sapopvp.eisen) {
            final Player target = Bukkit.getPlayer(pl);
            if (target != null)
                target.sendMessage(msg);
        }
    }

    public static void updateArena() {
        if (instance.getConfig().getString("sapopvp.pos1").equalsIgnoreCase("") || instance.getConfig().getString("sapopvp.pos2").equalsIgnoreCase(""))
            return;

        EntityListener.saveArenaCoords(instance.getConfig().getString("sapopvp.pos1").split(","), instance.getConfig().getString("sapopvp.pos2").split(","));

        if (instance.getConfig().getString("sapopvp.warpgold").equalsIgnoreCase("") || instance.getConfig().getString("sapopvp.warpeisen").equalsIgnoreCase(""))
            return;

        final String[] dataG = instance.getConfig().getString("sapopvp.warpgold").split(",");
        final String[] dataE = instance.getConfig().getString("sapopvp.warpeisen").split(",");

        warpGold = new Location(Bukkit.getWorld(dataG[0]), Integer.valueOf(dataG[1]), Integer.valueOf(dataG[2]), Integer.valueOf(dataG[3]), Float.valueOf(dataG[4]), Float.valueOf(dataG[5]));
        warpEisen = new Location(Bukkit.getWorld(dataE[0]), Integer.valueOf(dataE[1]), Integer.valueOf(dataE[2]), Integer.valueOf(dataE[3]), Float.valueOf(dataE[4]), Float.valueOf(dataE[5]));
    }

}