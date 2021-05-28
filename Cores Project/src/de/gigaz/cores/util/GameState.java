package de.gigaz.cores.util;

public enum GameState {
	LOBBY_STATE(1, "Lobby-State"), INGAME_STATE(2, "Ingame-State"), ENDING_STATE(3, "Ending-State");
	
	private int number;
	private String name;

	GameState(int number, String name) {
		this.number = number;
		this.name = name;
	}
	
	public int getNumber() {
		return number;
	}
	
	public String getName() {
		return name;
	}
	
}
