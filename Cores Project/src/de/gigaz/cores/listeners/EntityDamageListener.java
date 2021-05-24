package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.IngameState;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Team;

public class EntityDamageListener implements Listener {

	@EventHandler
	public void onDamager(EntityDamageByEntityEvent event) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		if(event.getEntity() instanceof Player) {
			Player target = (Player) event.getEntity();
			PlayerProfile targetProfile = gameManager.getPlayerProfile(target);
			PlayerProfile attackerProfile = new PlayerProfile(target);
			if(event.getDamager() instanceof Player) {
				Player attacker = (Player) event.getDamager();
				attackerProfile = gameManager.getPlayerProfile(attacker);
			}
			if(gameManager.getCurrentGameState() != GameState.INGAME_STATE || (event.getDamager() instanceof Player && attackerProfile != null && targetProfile.getTeam() != attackerProfile.getTeam())) {
								
				if(target.getHealth() - event.getDamage() <= 0) {
					if(attackerProfile.getPlayer() != targetProfile.getPlayer())
						attackerProfile.addKill();
					event.setCancelled(true);
					respawn(targetProfile);
					Bukkit.broadcastMessage(Main.PREFIX + "§7" + attackerProfile.getPlayer().getName() + " hat " + target.getName() + " getötet");
					attackerProfile.getPlayer().sendMessage("Kill #"+attackerProfile.getKills());
					target.sendMessage("§6K§7/§6D§7: "+targetProfile.getKills()+ "/" + targetProfile.getDeaths());
				}
			} else {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
			if(gameManager.getCurrentGameState() != GameState.INGAME_STATE) {
				respawn(playerProfile);
			}
			event.setCancelled(true);
		}
	}
	
	public void respawn(PlayerProfile playerProfile) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		Player player = playerProfile.getPlayer();
		playerProfile.addDeath();
		player.getInventory().clear();
		Team team = gameManager.getPlayerProfile(player).getTeam();
		Location location = MainCommand.getConfigLocation(team.getDebugColor() + ".spawn", player.getWorld());
		player.teleport(location);
		player.setHealth(20);
		player.setFoodLevel(20);
		
		IngameState.giveItems(gameManager.getPlayerProfile(player));
	}
	
}
