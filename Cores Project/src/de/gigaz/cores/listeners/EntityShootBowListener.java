package de.gigaz.cores.listeners;

import java.awt.Color;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.Team;
import net.minecraft.server.v1_16_R3.BlockBase.e;

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
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
					
					@Override
					public void run() {
						player.removePotionEffect(PotionEffectType.WITHER);
						player.getInventory().setHelmet(ItemBuilder.setColor(new ItemBuilder(Material.LEATHER_HELMET).setBreakable(false).build(), team.getColor()));
					}
				}, 20*20L);
			}
		}
	}
	
	private Integer eggTimer;
	
	@EventHandler
	public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
		if(!(event.getEntity().getShooter() instanceof Player))
			return;
		if(!(event.getEntity() instanceof Egg))
			return;
		GameManager gameManager = Main.getPlugin().getGameManager();
		Player player = (Player) event.getEntity().getShooter();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
		Team team = playerProfile.getTeam();
		Egg egg = (Egg) event.getEntity();
		Location loc = egg.getLocation();
		loc.setY(loc.getY()-1.5);
		egg.teleport(loc);
		
		if(eggTimer != null)
			Bukkit.getScheduler().cancelTask(eggTimer);
		
		eggTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			int ticks = 0;
			@Override
			public void run() {
				ticks += 1;
				if(egg.isDead()) {
					Bukkit.getScheduler().cancelTask(eggTimer);
					eggTimer = null;
					return;
				}
				if(ticks >= 20){
					egg.remove();
					Bukkit.getScheduler().cancelTask(eggTimer);
					eggTimer = null;
				}
				Location location = egg.getLocation().clone();
				if(location.getBlock().getType().equals(Material.AIR))
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
						@Override
						public void run() {
							Location newlocation = location.getBlock().getLocation();
							if(newlocation.getZ() < location.getZ())
								newlocation.setZ(newlocation.getZ()-1);
							else
								newlocation.setZ(newlocation.getZ()+1);
							if(newlocation.getX() < location.getX())
								newlocation.setX(newlocation.getX()-1);
							else
								newlocation.setX(newlocation.getX()+1);
							newlocation.getBlock().setType(team.equals(Team.BLUE)?Material.WARPED_PLANKS:team.equals(Team.RED)?Material.CRIMSON_PLANKS:Material.OAK_PLANKS);
							Random random = new Random();
							newlocation.setX(location.getX()-1+random.nextFloat()*2);
							//newlocation.setY(location.getY()-1+random.nextFloat()*2);
							newlocation.setZ(location.getZ()-1+random.nextFloat()*2);
							newlocation.getBlock().setType(team.equals(Team.BLUE)?Material.WARPED_PLANKS:team.equals(Team.RED)?Material.CRIMSON_PLANKS:Material.OAK_PLANKS);
							//newlocation = location.getBlock().getLocation();
							//newlocation.setX(location.getX()-1+random.nextFloat()*2);
							//newlocation.setY(location.getY()-1+random.nextFloat()*2);
							//newlocation.setZ(location.getZ()-1+random.nextFloat()*2);
							//newlocation.getBlock().setType(team.equals(Team.BLUE)?Material.WARPED_PLANKS:team.equals(Team.RED)?Material.CRIMSON_PLANKS:Material.OAK_PLANKS);
							location.getBlock().setType(team.equals(Team.BLUE)?Material.WARPED_PLANKS:team.equals(Team.RED)?Material.CRIMSON_PLANKS:Material.OAK_PLANKS);
							playerProfile.playSound(Sound.ENTITY_CHICKEN_EGG);
						}
					}, 2L);
			}
		}, 3L, 1L);
	}
}