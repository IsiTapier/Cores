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
	
	public static final String SCOREBOARD_TITILE = "§8------ §b§lCores §r§8------";
	
	public static void draw(Player player) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
		
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		if(gameManager.getCurrentGameState() == GameState.INGAME_STATE) {
			Objective objective = scoreboard.registerNewObjective("asdf", "asdf");
			objective.setDisplayName(SCOREBOARD_TITILE);
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			int slotsTeamBlue = gameManager.getStockedCores(Team.BLUE).size();
			int slotsTeamRed = gameManager.getStockedCores(Team.RED).size();
			int slotsKD = 3;
			int slotsTeamSpace = 2;
			int slotsDesignSpace = 3;
			
			/*Bukkit.broadcastMessage("-------------");
			Bukkit.broadcastMessage("§bblue size stocked:§7 " + gameManager.getStockedCores(Team.BLUE).size());
			Bukkit.broadcastMessage("§bblue size now: §7" + gameManager.getCores(Team.BLUE).size());
			Bukkit.broadcastMessage("§cred size stocked: §7" + gameManager.getStockedCores(Team.RED).size());
			Bukkit.broadcastMessage("§cred size now: §7" + gameManager.getCores(Team.RED).size());*/
			
			int totalSlots = slotsTeamBlue + slotsTeamRed + slotsKD + slotsTeamSpace + slotsDesignSpace;
			
			objective.getScore("§7Team " + Team.BLUE.getDisplayColor()).setScore(totalSlots); totalSlots--;
			for(Core core : gameManager.getStockedCores(Team.BLUE)) {
				if(gameManager.getCores().contains(core)) {
					if(core.isAttacked()) {
						objective.getScore("§e⚠ §6Core §6" + core.getDisplayName() + core.getTeam().getColorCode()).setScore(totalSlots);
					} else {
						objective.getScore("§a✔ §7Core §6" + core.getDisplayName() + core.getTeam().getColorCode()).setScore(totalSlots);
					}
				} else {
					objective.getScore("§c✘ §7Core §6" + core.getDisplayName() + core.getTeam().getColorCode()).setScore(totalSlots);
				}			//🞮
				totalSlots--;
			}
			objective.getScore("").setScore(totalSlots); totalSlots--;
			objective.getScore("§7Team " + Team.RED.getDisplayColor()).setScore(totalSlots); totalSlots--;
			for(Core core : gameManager.getStockedCores(Team.RED)) {
				if(gameManager.getCores().contains(core)) {
					if(core.isAttacked()) {
						objective.getScore("§e⚠ §6Core §6" + core.getDisplayName() + core.getTeam().getColorCode()).setScore(totalSlots);
					} else {
						objective.getScore("§a✔ §7Core §6" + core.getDisplayName() + core.getTeam().getColorCode()).setScore(totalSlots);					
					}
				} else {
					objective.getScore("§c✘ §7Core §6" + core.getDisplayName() + core.getTeam().getColorCode()).setScore(totalSlots);
				}
				totalSlots--;
			}	
			objective.getScore("     ").setScore(totalSlots); totalSlots--;
			
			objective.getScore("§7Kills: §6" + playerProfile.getKills()).setScore(totalSlots); totalSlots--;
			objective.getScore("§7Tode: §6" + playerProfile.getDeaths()).setScore(totalSlots); totalSlots--;
			if(playerProfile.getDeaths() == 0) {
				objective.getScore("§7K/D: §6" + playerProfile.getKills()).setScore(totalSlots); totalSlots--;
			} else {
				double kd = (Math.round(playerProfile.getKills()/playerProfile.getDeaths()*100)/100);
				objective.getScore("§7K/D: §6" + String.valueOf(kd)).setScore(totalSlots); totalSlots--;
			}
			
			
			objective.getScore("§8-------------------").setScore(totalSlots); totalSlots--;
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

}
