package de.gigaz.cores.classes;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ScoreboardManager;
import de.gigaz.cores.util.Team;

public class IngameState {
	
	private static GameManager gameManager;
	private static int checkUpLoopID;
	
	public static void start() {
		Main.getPlugin().getGameManager().setGameState(GameState.INGAME_STATE);
		gameManager = Main.getPlugin().getGameManager();
		gameManager.stockCores();
		ScoreboardManager.drawAll();
		
		for(PlayerProfile playerProfile : gameManager.getPlayerProfiles().values()) {
			ScoreboardManager.draw(playerProfile.getPlayer());
			teleportPlayer(playerProfile);
			giveItems(playerProfile);
			deactivateEditMode(playerProfile);
			setGameMode(playerProfile);
		}
		
		checkUpLoop();
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
	
	private static void checkUpLoop() {
		GameManager gameManager = Main.getPlugin().getGameManager();
		
		checkUpLoopID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				for(Player player : gameManager.getMap().getPlayers()) {
					PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
					Team team = playerProfile.getTeam();
					for(Core core : gameManager.getCores()) {
						core.setAttacked(false);
						if(core.getTeam() != playerProfile.getTeam()) {
							if(core.getLocation().distance(player.getLocation()) <= 7) {
								
								//Bukkit.broadcastMessage(Main.PREFIX + "§7Ein Spieler ist bei " + team.getOponentTeam(team).getDisplayColor() + "§7 eingedrungen");
								core.setAttacked(true);
							}						
						}
						if(core.getAttacked()) {
							playSound(core);
							ScoreboardManager.drawAll();
						}
					}
					
					
					if(player.getLocation().getY() <= MainCommand.getConfigLocation("deathhight", player.getWorld()).getY()) {
						
						Location location = MainCommand.getConfigLocation(team.getDebugColor() + ".spawn", player.getWorld());
						player.teleport(location);
						IngameState.giveItems(gameManager.getPlayerProfile(player));
						Bukkit.broadcastMessage(Main.PREFIX + playerProfile.getTeam().getColorCode() + player.getName() + "§7 ist gestorben");
					}
					
					
					
					
					if(Main.getPlugin().getGameManager().getCurrentGameState() != GameState.INGAME_STATE) {
						Bukkit.getScheduler().cancelTask(checkUpLoopID);
					}
				}
			}
		}, 0, 10);		
	}
	
	private static void playSound(Core core) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		for(Player player : gameManager.getMap().getPlayers()) {
			if(gameManager.getPlayerProfile(player).getTeam() == core.getTeam()) {
				player.playSound(core.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5, 1);
			}
		}
	}
	
}
