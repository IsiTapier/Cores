package de.gigaz.cores.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;	
import org.bukkit.scoreboard.Scoreboard;

import de.gigaz.cores.classes.Core;
import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;

public class ScoreboardManager implements Listener {
	
	public static final String SCOREBOARD_TITILE = "§8╔══ §b§lCores §r§8══╗";
	
	public static void draw(Player player) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
		
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective("asdf", "asdf");
		objective.setDisplayName(SCOREBOARD_TITILE);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			
		if(gameManager.getCurrentGameState() == GameState.INGAME_STATE) {
			drawIngameScoreboard(objective, player);			
		
		} else if(gameManager.getCurrentGameState().equals(GameState.LOBBY_STATE)) {
			drawLobbyScoreboard(objective, player);
		}
		
		if(!playerProfile.getCurrentScoreboard().equals(scoreboard)) {	
			player.setScoreboard(scoreboard);
			playerProfile.setCurrentScoreboard(scoreboard);
		}
	}
	
	
	public static void drawAll() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			draw(player);
		}
	}
	
	public static void drawLobbyScoreboard(Objective objective, Player player) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);	
		
		int totalSlots = 20;
		
		objective.getScore("§8╠ §7Map§8: §6" + (gameManager.getMap()==null?"unset":gameManager.getMap().getName())).setScore(totalSlots); totalSlots--;
		objective.getScore("§8╠ §7Spieler§8: §6" + Bukkit.getOnlinePlayers().size()).setScore(totalSlots); totalSlots--;
		objective.getScore("§8║    ").setScore(totalSlots); totalSlots--;
		objective.getScore("§8╠ §7Team " + Team.BLUE.getDisplayColor() + "§7[§6" + gameManager.getPlayersOfTeam(Team.BLUE).size() +"§7]").setScore(totalSlots); totalSlots--;
		for(Player loopPlayer : gameManager.getPlayersOfTeam(Team.BLUE)) {		
			objective.getScore("§8╠  §7" + loopPlayer.getName()).setScore(totalSlots); totalSlots--;
		}
		objective.getScore("§8╠     ").setScore(totalSlots); totalSlots--;	
		objective.getScore("§8╠ §7Team " + Team.RED.getDisplayColor() + " §7[§6" + gameManager.getPlayersOfTeam(Team.RED).size() +"§7]").setScore(totalSlots); totalSlots--;
		for(Player loopPlayer : gameManager.getPlayersOfTeam(Team.RED)) {		
			objective.getScore("§8╠  §7" + loopPlayer.getName()).setScore(totalSlots); totalSlots--;
		}
		objective.getScore("§8╚════════╝").setScore(totalSlots); totalSlots--;	
	}
	
	public static void drawIngameScoreboard(Objective objective, Player player) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);	
		
		int totalSlots = 20;
		objective.getScore("§8╠ §7Team " + Team.BLUE.getDisplayColor()).setScore(totalSlots); totalSlots--;
		for(Core core : gameManager.getStockedCores(Team.BLUE)) {
			if(gameManager.getCores().contains(core)) {
				if(core.isAttacked()) {
					objective.getScore("§8║ §e⚠ §6Core §6" + core.getDisplayName() + core.getTeam().getColorCode()).setScore(totalSlots);
				} else {
					objective.getScore("§8║ §a✔ §7Core §6" + core.getDisplayName() + core.getTeam().getColorCode()).setScore(totalSlots);
				}
			} else {
				objective.getScore("§8║ §c✘ §7Core §6" + core.getDisplayName() + core.getTeam().getColorCode()).setScore(totalSlots);
			}			//🞮
			totalSlots--;
		}
		objective.getScore("§8║").setScore(totalSlots); totalSlots--;
		objective.getScore("§8╠ §7Team " + Team.RED.getDisplayColor()).setScore(totalSlots); totalSlots--;
		for(Core core : gameManager.getStockedCores(Team.RED)) {
			if(gameManager.getCores().contains(core)) {
				if(core.isAttacked()) {
					objective.getScore("§8║ §e⚠ §6Core §6" + core.getDisplayName() + core.getTeam().getColorCode()).setScore(totalSlots);
				} else {
					objective.getScore("§8║ §a✔ §7Core §6" + core.getDisplayName() + core.getTeam().getColorCode()).setScore(totalSlots);					
				}
			} else {
				objective.getScore("§8║ §c✘ §7Core §6" + core.getDisplayName() + core.getTeam().getColorCode()).setScore(totalSlots);
			}
			totalSlots--;
		}	
		objective.getScore("§8║     ").setScore(totalSlots); totalSlots--;	
		objective.getScore("§8╠ §7Kills: §6" + playerProfile.getKills()).setScore(totalSlots); totalSlots--;
		objective.getScore("§8╠ §7Tode: §6" + playerProfile.getDeaths()).setScore(totalSlots); totalSlots--;
		if(playerProfile.getDeaths() == 0) {
			objective.getScore("§8╠ §7K/D: §6" + playerProfile.getKills()).setScore(totalSlots); totalSlots--;
		} else {
			double kd = Math.round(1.0*playerProfile.getKills()/playerProfile.getDeaths()*1000)/1000.0;
			objective.getScore("§8╠ §7K/D: §6" + String.valueOf(kd)).setScore(totalSlots); totalSlots--;
		}	
		objective.getScore("§8╚════════╝").setScore(totalSlots); totalSlots--;	
	}
		

}
