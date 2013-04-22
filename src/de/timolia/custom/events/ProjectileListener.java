/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ProjectileListener implements Listener {

	public static List<Integer> active = new ArrayList<Integer>();

	// instant damage stacks verbieten
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (event.getEntity().getShooter() != null && event.getEntity().getShooter() instanceof Player) {
			Projectile ent = event.getEntity();
			if (ent instanceof ThrownPotion)
				for (PotionEffect pe : ((ThrownPotion) ent).getEffects())
					if (pe.getType().equals(PotionEffectType.HARM)) {
						Player p = (Player) ent.getShooter();
						if (p.getGameMode() != GameMode.CREATIVE && p.getItemInHand().getAmount() >= 1) {
							event.setCancelled(true);
							p.getItemInHand().setAmount(p.getItemInHand().getAmount() + 1);
						}
						return;
					}
		}
	}

	// giftpfeile
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityShootBow(EntityShootBowEvent event) {
		if (event.getEntity().getType() == EntityType.PLAYER) {
			Player shooter = (Player) event.getEntity();
			PlayerInventory inv = shooter.getInventory();
			HashMap<Integer, ? extends ItemStack> arrowSlots = inv.all(Material.ARROW);
			Iterator<Integer> iter = arrowSlots.keySet().iterator();
			while (iter.hasNext()) {
				int index = iter.next();
				ItemStack item = arrowSlots.get(index);
				if (item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_RED + "Giftpfeil")) {
					active.add(event.getProjectile().getEntityId());
					int first;
					if (shooter.getGameMode() != GameMode.CREATIVE && index != (first = inv.first(Material.ARROW))) {
						int amount = item.getAmount();
						if (amount == 1)
							inv.clear(index);
						else
							item.setAmount(amount - 1);
						inv.getItem(first).setAmount(inv.getItem(first).getAmount() + 1);
						shooter.updateInventory();
					}
					break;
				}
			}
		}
	}

}