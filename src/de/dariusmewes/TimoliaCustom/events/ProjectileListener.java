/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.dariusmewes.TimoliaCustom.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ProjectileListener implements Listener {

	// instant damage stacks verbieten
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Projectile ent = event.getEntity();
		if (ent instanceof ThrownPotion)
			for (PotionEffect pe : ((ThrownPotion) ent).getEffects())
				if (pe.getType().equals(PotionEffectType.HARM)) {
					if (ent.getShooter() instanceof Player && ((Player) ent.getShooter()).getGameMode() != GameMode.CREATIVE) {
						Player p = (Player) ent.getShooter();
						if (p.getItemInHand().getAmount() >= 1) {
							event.setCancelled(true);
							p.getItemInHand().setAmount(p.getItemInHand().getAmount() + 1);
						}
					}
					return;
				}
	}

}