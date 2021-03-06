package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Gamerules;
import de.gigaz.cores.util.ScoreboardManager;

public class EntityDamageListener implements Listener {

	@EventHandler
	public void onDamager(EntityDamageByEntityEvent event) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		if(event.getEntity() instanceof Player) {
			Player target = (Player) event.getEntity(); 
			Player attacker;
			if(event.getDamager() instanceof Player || event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player) {
				if(event.getDamager() instanceof Player) {
					attacker = (Player) event.getDamager();
				} else if(event.getDamager() instanceof Arrow) {
					Arrow arrow = (Arrow) event.getDamager();
					if(arrow.getShooter() instanceof Player) {
						attacker = (Player) arrow.getShooter();
					} else
						return;
				} else
					return;
				PlayerProfile targetProfile = gameManager.getPlayerProfile(target);
				PlayerProfile attackerProfile = gameManager.getPlayerProfile(attacker);
				targetProfile.setLastAttacker(attacker);
				checkKill(event, targetProfile, attackerProfile, event.getDamager() instanceof Arrow);
			}
		}
	}
	
	public void checkKill(EntityDamageByEntityEvent event, PlayerProfile targetProfile, PlayerProfile attackerProfile, boolean isBowKill) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		Player attacker = attackerProfile.getPlayer();
		Player target = targetProfile.getPlayer();
		
		if(gameManager.getCurrentGameState() == GameState.INGAME_STATE && targetProfile.getTeam() != attackerProfile.getTeam()) {	
			if(Gamerules.getValue(Gamerules.onehit))
				event.setDamage(Double.MAX_VALUE);
			if(target.getHealth() - event.getDamage() < 1) {
				event.setCancelled(true);
				if(attackerProfile.getPlayer() != targetProfile.getPlayer())
					attackerProfile.addKill();
					ScoreboardManager.draw(attacker);
				if(isBowKill)
					Bukkit.broadcastMessage(Main.getPlugin().getBowDeathMessage(targetProfile, attackerProfile));
				else
					Bukkit.broadcastMessage(Main.getPlugin().getPVPDeathMessage(targetProfile, attackerProfile));
				targetProfile.playSound(Sound.ENTITY_VILLAGER_DEATH);
				attackerProfile.playSound(Sound.ENTITY_VILLAGER_DEATH);
				respawn(targetProfile);
				//Bukkit.broadcastMessage(Main.PREFIX + "?7" + attackerProfile.getPlayer().getName() + " hat " + target.getName() + " get?tet");
				//attackerProfile.getPlayer().sendMessage("Kill #"+attackerProfile.getKills());
				//target.sendMessage("?6K?7/?6D?7: "+targetProfile.getKills()+ "/" + targetProfile.getDeaths());
			}
			if(Gamerules.getValue(Gamerules.noKnockback)) {
				event.setCancelled(true);
				target.damage(event.getDamage());
			}
		} else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
			if(gameManager.getCurrentGameState() == GameState.INGAME_STATE && player.getWorld().equals(Main.getPlugin().getWorld("currentworld"))) {
				if(event.getCause().equals(DamageCause.FALL)) {
					Location location = gameManager.getSpawnOfTeam(playerProfile.getTeam(), gameManager.getMap());
					if(location.distance(player.getLocation()) <= 3 || Gamerules.getValue(Gamerules.noFallDamage)) {
						event.setCancelled(true);
						return;
					}
				}
				if(player.getHealth() - event.getDamage() < 1) {
					if(!event.getCause().equals(DamageCause.ENTITY_ATTACK) && !event.getCause().equals(DamageCause.PROJECTILE) && !event.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK)) {
						if(event.getCause().equals(DamageCause.FALL)) {
							//Cancel event on fall Damage after DeathTP
							if(playerProfile.getLastAttacker() != null) {
								Player attacker = playerProfile.getLastAttacker();
								PlayerProfile attackerProfile = Main.getPlugin().getGameManager().getPlayerProfile(attacker);
								attackerProfile.addKill();
								ScoreboardManager.draw(attacker);
								Bukkit.broadcastMessage(Main.getPlugin().getFallDeathMessage(playerProfile, attackerProfile));
							} else
								Bukkit.broadcastMessage(Main.getPlugin().getFallDeathMessage(playerProfile));
						} else if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.LAVA)) {
							if(playerProfile.getLastAttacker() != null) {
								Player attacker = playerProfile.getLastAttacker();
								PlayerProfile attackerProfile = Main.getPlugin().getGameManager().getPlayerProfile(attacker);
								attackerProfile.addKill();
								ScoreboardManager.draw(attacker);
								Bukkit.broadcastMessage(Main.getPlugin().getFireDeathMessage(playerProfile, attackerProfile));
							} else
								Bukkit.broadcastMessage(Main.getPlugin().getFireDeathMessage(playerProfile));
						}
						event.setCancelled(true);
						respawn(playerProfile);
						//player.sendMessage(Main.PREFIX + "?6K?7/?6D?7: "+playerProfile.getKills()+ "/" + playerProfile.getDeaths());
					}
				}
				
			} else {
				event.setCancelled(true);
			}
		}
	}

	
	public void respawn(PlayerProfile playerProfile) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		Player player = playerProfile.getPlayer();
		playerProfile.addDeath();
		
		gameManager.getPlayerProfile(player).respawn();
	}
	
}
