package de.gigaz.cores.classes;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.Team;

public class IngameState {
	
	private static GameManager gameManager;
	
	public static void start() {
		
		gameManager = Main.getPlugin().getGameManager();
		Bukkit.broadcastMessage("1");
		gameManager.registerCores();
		Bukkit.broadcastMessage("2");
		for(PlayerProfile playerProfile : gameManager.getPlayerProfiles().values()) {
			
			teleportPlayer(playerProfile);
			giveItems(playerProfile);
			deactivateEditMode(playerProfile);
			setGameMode(playerProfile);
		}
		
		
		
		Main.getPlugin().getGameManager().setGameState(GameState.INGAME_STATE);
		
	}	
	public static void stop() {
		EndingState.start();
	}
	
	public static void giveItems(PlayerProfile playerProfile) {
		
		playerProfile.getPlayer().getInventory().clear();
		Inventories.setIngameIventory(playerProfile);
		playerProfile.getPlayer().setHealth(20);
		playerProfile.getPlayer().setFoodLevel(20);
		
	}
	
	public static void teleportPlayer(PlayerProfile playerProfile) {
		World world = gameManager.getMap();
		Player player = playerProfile.getPlayer();
		Location location = player.getLocation();
		if(playerProfile.getTeam() == Team.BLUE) {
			location = MainCommand.getConfigLocation("blue.spawn", world);
		}
		if(playerProfile.getTeam() == Team.RED) {
			location = MainCommand.getConfigLocation("red.spawn", world);
		}
		World lastWorld = player.getWorld();
		player.teleport(location);
		player.sendMessage(player.getWorld().getName());
		if(lastWorld == player.getWorld()) {
			player.sendMessage("du bist in der selben Welt");
		}
		
	}
	
	public static void deactivateEditMode(PlayerProfile playerProfile) {
		Player player = playerProfile.getPlayer();
		if(playerProfile.isEditMode()) {
			playerProfile.setEditMode(false);
			player.sendMessage(Main.PREFIX + "§7Der Edit Mode wurde aus Sicherheitsgründen §cdeaktiviert");
		}
		
	}
	public static void setGameMode(PlayerProfile playerProfile) {
		playerProfile.getPlayer().setGameMode(GameMode.SURVIVAL);
	}
}
