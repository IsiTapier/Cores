package de.gigaz.cores.listeners;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.Team;

public class EntityShootBowListener implements Listener {
	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent event) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(gameManager.getCurrentGameState() != GameState.INGAME_STATE || !player.getWorld().equals(Main.getPlugin().getWorld("currentworld")))
				event.setCancelled(true);
			if(gameManager.getGameruleSetting(gameManager.infiniteArrowsGamerule).getValue()) {
				player.getInventory().setItem(player.getInventory().first(Material.ARROW), new ItemBuilder(Material.ARROW).setName("§6Infinity Arrow").addEnchantment(Enchantment.ARROW_INFINITE, 10).setAmount(gameManager.getGameruleSetting(gameManager.superCrossbowGamerule).getValue()||gameManager.getGameruleSetting(gameManager.crossbowGamerule).getValue()?2:1).setBreakable(false).build());
				Arrow arrow = (Arrow) event.getProjectile();
				arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
			}
		}
	}
	
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		if(event.getEntityType().equals(EntityType.WITHER_SKULL)) {
			if(event.getHitEntity() instanceof Player) {
				Player player = (Player) event.getHitEntity();
				PlayerProfile playerProfile = Main.getPlugin().getGameManager().getPlayerProfile(player);
				Team team = playerProfile.getTeam();
				player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20*20, 0, false, false, false));
				player.getInventory().setHelmet(new ItemBuilder(Material.WITHER_SKELETON_SKULL).setName("You've been withered").build());
				Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getPlugin(), new Runnable() {
					
					@Override
					public void run() {
						player.removePotionEffect(PotionEffectType.WITHER);
						player.getInventory().setHelmet(ItemBuilder.setColor(new ItemBuilder(Material.LEATHER_HELMET).setBreakable(false).build(), team.getColor()));
					}
				}, 20*20L);
			}
		}
	}
}