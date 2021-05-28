package de.gigaz.cores.classes;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;

import de.gigaz.cores.util.Team;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Inventories;

public class PlayerProfile {
	private Player player;
	private boolean editMode = false;
	private Team team = Team.UNSET;
	private int kills = 0;
	private int deaths = 0;
	private Scoreboard currentScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	private Player lastAttacker;

	
	public PlayerProfile(Player player) {
		this.player = player;

	}
	
	public Player getPlayer() {
		
		return player;
	}
	
	public Team getTeam() {
		return team;
	}

	public boolean isEditMode() {
		return editMode;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public void setTeam(Team team) {
		this.team = team;
		this.player.setPlayerListName(team.getColorCode() + player.getName());
		Inventories.setLobbyInventory(this);
	}

	public Scoreboard getCurrentScoreboard() {
		return currentScoreboard;
	}
	
	public void setCurrentScoreboard(Scoreboard scoreboard) {
		this.currentScoreboard = scoreboard;
	}
	
	public void addKill() {
		kills++;
	}
	
	public void setKills(int number) {
		kills = number;
	}
	
	public void resetKills() {
		setKills(0);
	}
	
	public int getKills() {
		return kills;
	}
	
	public void addDeath() {
		deaths++;
	}
	
	public void setDeaths(int number) {
		deaths = number;
	}
	
	public void resetDeaths() {
		setDeaths(0);
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public void resetStats() {
		resetKills();
		resetDeaths();
	}
	
	public void setLastAttacker(Player player) {
		lastAttacker = player;
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                clearLastAttacker();
            }
        }, 7*20);
	}
	
	public void clearLastAttacker() {
		lastAttacker = null;
	}
	
	public Player getLastAttacker() {
		return lastAttacker;
	}
	
	public String getName() {
		return getTeam().getColorCode()+getPlayer().getName()+"�7";
	}
}
