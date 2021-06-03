package de.gigaz.cores.classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.Team;

public class EndingState {

	
	public static void start(Team team) {
		GameManager gameManager = Main.getPlugin().getGameManager();	
		Main.getPlugin().getGameManager().setGameState(GameState.ENDING_STATE);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				teleportPlayers();
				showTitle(team);
				gameManager.playSound(Sound.UI_TOAST_CHALLENGE_COMPLETE, gameManager.getLobbySpawn().getWorld(), 5);
				replaceBlocks();
				stop();
			}
		}, 2*20);
			
	}
	
	public static void stop() {
		LobbyState.start();
	}
	
	public static void replaceBlocks() {
		GameManager gameManager = Main.getPlugin().getGameManager();
		
		for(Location location : gameManager.getBuiltBlocks()) {
			location.getBlock().setType(Material.AIR);
			//Bukkit.broadcastMessage("cleared " + location.getX());
		}
		for(Location location : gameManager.getBreakedBlocks().keySet()) {
			
			if(!gameManager.getBuiltBlocks().contains(location)) {
				location.getBlock().setType(gameManager.getBreakedBlocks().get(location));
				//Bukkit.broadcastMessage("replaced " + location.getBlock().getType());
			}
		}
		gameManager.getBuiltBlocks().clear();
		gameManager.getBreakedBlocks().clear();
		
	}
	public static void showTitle(Team team) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); 
			Main.getPlugin().getGameManager().getPlayerProfile(player).resetStats();
			player.sendTitle(team.getDisplayColor(), "§7hat gewonnen");
		}
	}
	
	public static void teleportPlayers() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			//player.sendMessage(player.getWorld().getName());
			player.teleport(Main.getPlugin().getGameManager().getLobbySpawn());
			//player.sendMessage(player.getWorld().getName());
			
			player.setHealth(20);
			player.setFoodLevel(20);
			Inventories.setLobbyInventory(Main.getPlugin().getGameManager().getPlayerProfile(player));
		}
	}
}
