/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Message {

	public static final String NOPERM = TimoliaCustom.PREFIX + "Du hast keine Berechtigung f�r diesen Befehl!";
	public static final String INGAME = TimoliaCustom.PREFIX + "Dieser Befehl kann nicht in der Konsole benutzt werden!";
	
	public static void console(String message) {
		Logger.getLogger("Minecraft").info("[TCustom] " + message);
	}

	public static void online(String message) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(message);
		}
	}

	public static void certain(String message, String permission) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission(permission)) {
				p.sendMessage(message);
			}
		}
	}
	
}