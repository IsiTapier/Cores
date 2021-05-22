package de.gigaz.cores.classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.Team;

public class IngameState {
	
	public static void start() {
		teleportPlayers();
		giveItems();
		Main.getPlugin().getGameManager().setGameState(GameState.INGAME_STATE);
		
	}	
	public static void stop() {
		EndingState.start();
	}
	
	private static void giveItems() {		
		for(PlayerProfile playerProfile : Main.getPlugin().getGameManager().getPlayerProfiles().values()) {
			Inventories.setIngameIventory(playerProfile);
		}
	}
	
	public static void teleportPlayers() {
		for(PlayerProfile playerProfile : Main.getPlugin().getGameManager().getPlayerProfiles().values()) {
			World world = Main.getPlugin().getGameManager().getMap();
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
	}
}
