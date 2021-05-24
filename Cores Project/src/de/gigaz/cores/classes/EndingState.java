package de.gigaz.cores.classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;

public class EndingState {
	
	public static void start() {
		Main.getPlugin().getGameManager().setGameState(GameState.ENDING_STATE);
		replaceBlocks();
		teleportPlayers();
		Bukkit.broadcastMessage("ende");
	}
	
	public static void stop() {
		LobbyState.start();
	}
	
	public static void replaceBlocks() {
		GameManager gameManager = Main.getPlugin().getGameManager();
		
		for(Location location : gameManager.getBuiltBlocks()) {
			location.getBlock().setType(Material.AIR);
			Bukkit.broadcastMessage("cleared " + location.getX());
		}
		for(Location location : gameManager.getBreakedBlocks().keySet()) {
			Bukkit.broadcastMessage(location.getBlock().getType() + " in list breaked blocks. was " + gameManager.getBreakedBlocks().get(location));
			if(!gameManager.getBuiltBlocks().contains(location)) {
				location.getBlock().setType(gameManager.getBreakedBlocks().get(location));
				Bukkit.broadcastMessage("replaced " + location.getBlock().getType());
			}
		}
		gameManager.getBuiltBlocks().clear();
		gameManager.getBreakedBlocks().clear();
		
	}
	public static void teleportPlayers() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(player.getWorld().getName());
			player.teleport(Main.getPlugin().getGameManager().getLobbySpawn());
			player.sendMessage(player.getWorld().getName());
		}
	}
}
