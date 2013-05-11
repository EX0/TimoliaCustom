/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class rmprot extends TCommand {

	protected void prepare() {
		permission();
		ingame();
		maxArgs(0);
	}

	public void perform(final CommandSender sender, String[] args) {
		final Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldEdit");

		if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
			sender.sendMessage(prefix + "WorldEdit ist nicht geladen!");
			return;
		}

		final WorldEditPlugin we = (WorldEditPlugin) plugin;
		final Selection sel = we.getSelection((Player) sender);

		if (sel == null) {
			sender.sendMessage(prefix + "Du hast keine WorldEdit Selektierung gemacht!");
			return;
		}

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			sender.sendMessage(prefix + "Der Datenbanktreiber wurde nicht gefunden!");
			e.printStackTrace();
			return;
		}

		final String host = instance.getConfig().getString("dbHost");
		final String port = instance.getConfig().getString("dbPort");
		final String name = instance.getConfig().getString("dbName");
		final String user = instance.getConfig().getString("dbUser");
		final String pass = instance.getConfig().getString("dbPass");
		final Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + name, user, pass);
			final PreparedStatement stmt = conn.prepareStatement("DELETE FROM lwc_protections WHERE x<=? AND y<=? AND z<=? AND x>=? AND y>=? AND z>=? AND world=?");
			stmt.setInt(1, sel.getMaximumPoint().getBlockX());
			stmt.setInt(2, sel.getMaximumPoint().getBlockY());
			stmt.setInt(3, sel.getMaximumPoint().getBlockZ());
			stmt.setInt(4, sel.getMinimumPoint().getBlockX());
			stmt.setInt(5, sel.getMinimumPoint().getBlockY());
			stmt.setInt(6, sel.getMinimumPoint().getBlockZ());
			stmt.setString(7, ((Player) sender).getWorld().getName());
			final int affected = stmt.executeUpdate();
			sender.sendMessage(prefix + affected + " Einträge wurden gelöscht!");
		} catch (SQLException e) {
			sender.sendMessage(prefix + "Fehler bei der Datenbankabfrage!");
			e.printStackTrace();
			return;
		}

	}
}