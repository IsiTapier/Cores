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
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ScoreboardManager;
import de.gigaz.cores.util.Team;

public class IngameState {
	
	private static GameManager gameManager;
	private static int checkUpLoopID;
	
	public static void start() {
		gameManager = Main.getPlugin().getGameManager();
		gameManager.setGameState(GameState.INGAME_STATE);
		gameManager.stockCores();
		ScoreboardManager.drawAll();
		if(gameManager.getGameruleSetting(gameManager.autoTeamGamerule).getValue())
			gameManager.setTeams();
		
		for(PlayerProfile playerProfile : gameManager.getPlayerProfiles()) {
			Player player = playerProfile.getPlayer();
			ScoreboardManager.draw(player);
			teleportPlayer(playerProfile);
			giveItems(playerProfile);
			deactivateEditMode(playerProfile);
			setGameMode(playerProfile);
			if(gameManager.getGameruleSetting(gameManager.aquaGamerule).getValue()) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
				player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, gameManager.getGameruleSetting(gameManager.speedGamerule).getValue() ? 1 : 0, false, false, false));
				player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
			}
			if(gameManager.getGameruleSetting(gameManager.hasteGamerule).getValue())
				player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 2, false, false, false));
			if(gameManager.getGameruleSetting(gameManager.jumpboostGamerule).getValue())
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, false, false));
			if(gameManager.getGameruleSetting(gameManager.speedGamerule).getValue())
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false, false));
			if(gameManager.getGameruleSetting(gameManager.invisibilityGamerule).getValue())
				player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
			if(gameManager.getGameruleSetting(gameManager.glowingGamerule).getValue())
				player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false));
		
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
			player.sendMessage(Main.PREFIX + "�7Der Edit Mode wurde aus Sicherheitsgr�nden �cdeaktiviert");
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
									loopPlayer.sendMessage(Main.PREFIX + "�4Der Core �6" + core.getDisplayName()+ "�4 wird attackiert");
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
						//Bukkit.broadcastMessage(Main.PREFIX + playerProfile.getTeam().getColorCode() + player.getName() + "�7 ist gestorben");
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
