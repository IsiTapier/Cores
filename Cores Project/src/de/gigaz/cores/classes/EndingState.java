package de.gigaz.cores.classes;

import org.bukkit.Bukkit;
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
		
		for(Block block : gameManager.getBuiltBlocks()) {
			block.setType(Material.AIR);
		}
		for(Block block : gameManager.getBreakedBlocks()) {
			if(gameManager.getBuiltBlocks().contains(block))
				block.getLocation().getBlock().setType(block.getType());
		}

		
	}
	public static void teleportPlayers() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(player.getWorld().getName());
			player.teleport(Main.getPlugin().getGameManager().getLobbySpawn());
			player.sendMessage(player.getWorld().getName());
		}
	}
}
