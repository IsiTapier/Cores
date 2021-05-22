package de.gigaz.cores.classes;

import java.util.HashMap;

import org.bukkit.entity.Player;

import de.gigaz.cores.util.Team;
import de.gigaz.cores.util.Inventories;

public class PlayerProfile {
	private Player player;
	private boolean editMode = false;
	private Team team = Team.UNSET;
	private int kills = 0;
	private int deaths = 0;
	
	public PlayerProfile(Player player) {
		this.player = player;
		Inventories.setLobbyInventory(this);
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
		Inventories.setLobbyInventory(this);
	}
}
