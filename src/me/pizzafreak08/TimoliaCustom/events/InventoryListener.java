package me.pizzafreak08.TimoliaCustom.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

	// sapopvp
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		if (EntityListener.insideArena((Player) event.getWhoClicked())) {
			if (event.getRawSlot() == 6) {
				event.setCancelled(true);
			}
		}
	}
}