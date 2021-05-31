package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.IngameState;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ScoreboardManager;
import de.gigaz.cores.util.Team;

public class ConnectionListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		GameManager gameManager = Main.getPlugin().getGameManager();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
		event.setJoinMessage(Main.PREFIX + "§7§l" + player.getName() + "§r§7 ist dem Spiel §abeigetreten");
		if(playerProfile == null) {
			gameManager.addPlayer(player);
			playerProfile = gameManager.getPlayerProfile(player);
			if(Main.autoteamrejoin) {
				gameManager.autoTeam(playerProfile);
				ScoreboardManager.draw(playerProfile.getPlayer());
				IngameState.teleportPlayer(playerProfile);
				IngameState.giveItems(playerProfile);
				IngameState.deactivateEditMode(playerProfile);
				IngameState.setGameMode(playerProfile);
			} else {
				player.teleport(MainCommand.getConfigGeneralLocation("lobbyspawn"));
				Inventories.setLobbyInventory(gameManager.getPlayerProfile(player));
			}
		} else {
			playerProfile.setPlayer(player);
			playerProfile = gameManager.getPlayerProfile(player);
			playerProfile.setTeam(playerProfile.getTeam());
			if(gameManager.getCurrentGameState().equals(GameState.INGAME_STATE) && !playerProfile.getTeam().equals(Team.UNSET)) {
				ScoreboardManager.draw(playerProfile.getPlayer());
				IngameState.teleportPlayer(playerProfile);
				IngameState.giveItems(playerProfile);
				IngameState.deactivateEditMode(playerProfile);
				IngameState.setGameMode(playerProfile);
			} else {
				player.teleport(MainCommand.getConfigGeneralLocation("lobbyspawn"));
				Inventories.setLobbyInventory(playerProfile);
			}
		}
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(Main.PREFIX + "§7§l" + event.getPlayer().getName() + "§r§7 hat das Spiel §cverlassen");
		//Main.getPlugin().getGameManager().removePlayer(event.getPlayer());
	}
	
}
