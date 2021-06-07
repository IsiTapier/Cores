package de.gigaz.cores.util;

import org.bukkit.Color;

public enum Team {
	BLUE("§9Blau§r", "blue", "§9", "red", Color.fromRGB(0, 0, 255)),
	RED("§cRot§r", "red", "§c", "blue", Color.fromRGB(255, 0, 0)),
	UNSET("§7Niemand§r", "unset", "§7", "", Color.fromRGB(255, 255, 255));
	
	private String displayColor;
	private String debugColor;
	private String colorCode;
	private String oponentColor;
	private Color color;
	
	private Team(String displayColor, String debugColor, String colorCode, String oponentColor, Color color) {
		this.displayColor = displayColor;
		this.debugColor = debugColor;
		this.colorCode = colorCode;
		this.oponentColor = oponentColor;
		this.color = color;
	}
	
	public String getDisplayColor() {
		return(displayColor);
	}
	
	public String getDebugColor() {
		return(debugColor);
	}
	
	public String getColorCode() {
		return(colorCode);
	}

	public static Team getTeam(String name) {
		if(name.equals("red"))
			return Team.RED;
		else if(name.equals("blue"))
			return Team.BLUE;
		else
			return Team.UNSET;
	}
	
	public Team getOponentTeam(Team team) {
		if(team.equals(Team.BLUE)) {
			return Team.RED;
		}
		if(team.equals(Team.RED)) {
			return Team.BLUE;
		}
		return Team.UNSET;
	}
	
	public Color getColor() {
		return color;
	}

}
