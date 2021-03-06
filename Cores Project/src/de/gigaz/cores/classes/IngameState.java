package de.gigaz.cores.classes;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.main.MainLoop;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Gamerules;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ScoreboardManager;
import de.gigaz.cores.util.Team;
import de.gigaz.cores.special.ActionBlock;
import de.gigaz.cores.special.SpecialItemManager;

public class IngameState {
	
	private static GameManager gameManager;
	private static int checkUpLoopID;
	
	public static void start() {
		gameManager = Main.getPlugin().getGameManager();
		gameManager.setGameState(GameState.INGAME_STATE);
		gameManager.stockCores();
		specifyActionBlockWorld();
		
		ScoreboardManager.drawAll();
		if(Gamerules.getValue(Gamerules.autoTeam, true)>=1)
			gameManager.setTeams();
		if(Gamerules.getValue(Gamerules.night))
			Main.getPlugin().getWorld("currentworld").setTime(18000);
		for(PlayerProfile playerProfile : gameManager.getPlayerProfiles()) {
			Player player = playerProfile.getPlayer();
			gameManager.getPlayerProfile(player).respawn(true);
		}
		MainLoop mainLoop = new MainLoop();
		gameManager.setSpecialItemManager(new SpecialItemManager());
	}
	
	public static void stop(Team team) {
		EndingState.start(team);
	}
	
	public static void giveItems(PlayerProfile playerProfile) {
		Player player = playerProfile.getPlayer();
		player.getInventory().clear();
		Inventories.setIngameIventory(playerProfile);
		player.setHealth(20);
		player.setFoodLevel(20);
		
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
		player.teleport(location);
	}
	
	public static void deactivateEditMode(PlayerProfile playerProfile) {
		Player player = playerProfile.getPlayer();
		if(playerProfile.isEditMode()) {
			playerProfile.setEditMode(false);
			player.sendMessage(Main.PREFIX + "?7Der Edit Mode wurde aus Sicherheitsgr?nden ?cdeaktiviert");
		}
		
	}
	public static void setGameMode(PlayerProfile playerProfile) {
		playerProfile.getPlayer().setGameMode(GameMode.SURVIVAL);
	}
	
	public static void specifyActionBlockWorld() {
		for(ActionBlock actionBlock : gameManager.getActionBlocks()) {
			actionBlock.getLocation().setWorld(gameManager.getCopiedWorld());
		}
	}
	
}
