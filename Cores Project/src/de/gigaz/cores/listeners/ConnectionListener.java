package de.gigaz.cores.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;

public class ConnectionListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(Main.PREFIX + "§7§l" + event.getPlayer().getName() + "§r§7 ist dem Spiel §abeigetreten");
		Main.getPlugin().getGameManager().addPlayer(event.getPlayer());
		event.getPlayer().teleport(MainCommand.getConfigGeneralLocation("lobbyspawn"));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(Main.PREFIX + "§7§l" + event.getPlayer().getName() + "§r§7 hat das Spiel §cverlassen");
		Main.getPlugin().getGameManager().removePlayer(event.getPlayer());
	}
	
}
