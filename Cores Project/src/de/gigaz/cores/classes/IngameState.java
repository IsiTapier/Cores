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
		if(Main.autoteam)
			gameManager.setTeams();
		
		for(PlayerProfile playerProfile : gameManager.getPlayerProfiles()) {
			ScoreboardManager.draw(playerProfile.getPlayer());
			teleportPlayer(playerProfile);
			giveItems(playerProfile);
			deactivateEditMode(playerProfile);
			setGameMode(playerProfile);
		}
		
		checkUpLoop();
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
				for(Core core : gameManager.getCores()) {
					boolean requestAttacked = false;
					for(Player player : Main.getPlugin().getWorld("currentworld").getPlayers()) {
						PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
						if(core.getTeam() != playerProfile.getTeam()) {
							if(core.getLocation().distance(player.getLocation()) <= 7)
								requestAttacked = true;
						}
					}
					
					if(requestAttacked) {
						if(!core.isAttacked()) {
							core.setAttacked(true);
							for(Player loopPlayer : gameManager.getMap().getPlayers()) {
								Team loopTeam = gameManager.getPlayerProfile(loopPlayer).getTeam();
								if(core.getTeam() == loopTeam) {
									loopPlayer.sendMessage(Main.PREFIX + "§4Der Core §6" + core.getDisplayName()+ "§4 wird attackiert");
									loopPlayer.playSound(loopPlayer.getLocation(), Sound.BLOCK_BELL_RESONATE, 3, 1);
									
								}
							}
							ScoreboardManager.drawAll();
						}
						core.setAttacked(true);
					} else {
						if(core.isAttacked()) {
							core.setAttacked(false);
							ScoreboardManager.drawAll();
						}
						core.setAttacked(false);
					}
				
				}				
				for(Player player : Main.getPlugin().getWorld("currentworld").getPlayers()) {
					PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
					Team team = playerProfile.getTeam();
					if(player.getLocation().getY() <= MainCommand.getConfigLocation("deathhight", gameManager.getMap()).getY()) {
						if(playerProfile.getLastAttacker() != null) {
							Player attacker = playerProfile.getLastAttacker();
							PlayerProfile attackerProfile = Main.getPlugin().getGameManager().getPlayerProfile(attacker);
							attackerProfile.addKill();
							ScoreboardManager.draw(attacker);
							Bukkit.broadcastMessage(Main.getPlugin().getVoidDeathMessage(playerProfile, attackerProfile));
						} else
							Bukkit.broadcastMessage(Main.getPlugin().getVoidDeathMessage(playerProfile));
						playerProfile.addDeath();
						ScoreboardManager.draw(player);
						Location location = MainCommand.getConfigLocation(team.getDebugColor() + ".spawn", gameManager.getMap());
						player.teleport(location);
						IngameState.giveItems(gameManager.getPlayerProfile(player));
						//Bukkit.broadcastMessage(Main.PREFIX + playerProfile.getTeam().getColorCode() + player.getName() + "§7 ist gestorben");
					}
							
					if(Main.getPlugin().getGameManager().getCurrentGameState() != GameState.INGAME_STATE) {
						Bukkit.getScheduler().cancelTask(checkUpLoopID);
					}
				}
			}
		}, 0, 10);	
		
		
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				for(Core core : gameManager.getCores()) {
					if(core.isAttacked()) {
						playSound(core);
					}
				}
				
			}
		}, 0, 18);
	}
	
	private static void playSound(Core core) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		for(Player player : gameManager.getMap().getPlayers()) {
			if(gameManager.getPlayerProfile(player).getTeam() == core.getTeam()) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5, 1);
			}
		}
	}
	
}
