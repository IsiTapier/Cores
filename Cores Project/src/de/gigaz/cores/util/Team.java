package de.gigaz.cores.util;

public enum Team {
	BLUE("�9Blau�r", "blue", "�9", "red"),
	RED("�cRot�r", "red", "�c", "blue"),
	UNSET("�7Niemand�r", "unset", "�7", "");
	
	private String displayColor;
	private String debugColor;
	private String colorCode;
	private String oponentColor;
	
	private Team(String displayColor, String debugColor, String colorCode, String oponentColor) {
		this.displayColor = displayColor;
		this.debugColor = debugColor;
		this.colorCode = colorCode;
		this.oponentColor = oponentColor;
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

}
