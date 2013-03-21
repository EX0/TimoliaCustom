/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom;

import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class BukkitCheck {

	private static String url = "http://dl.bukkit.org/api/1.0/downloads/projects/craftbukkit/artifacts/beta/";
	@SuppressWarnings("unused")
	private static BukkitTask task;
	private static String prefix = TimoliaCustom.PREFIX;

	private BukkitCheck() {

	}

	public static void start(JavaPlugin instance) {
		task = Bukkit.getScheduler().runTaskTimer(instance, new Runnable() {
			public void run() {
				action();
			}
		}, 200L, 12000L);
	}

	public static void action() {
		Message.console(prefix + "Prüfe ob eine neue Version verfügbar ist...");
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new URL(url).openStream());
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("list-item");
			Node nNode = nList.item(0);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String bukkitVersion = eElement.getElementsByTagName("version").item(0).getTextContent();
				Message.console(bukkitVersion);
			} else {
				System.out.println("nope");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}