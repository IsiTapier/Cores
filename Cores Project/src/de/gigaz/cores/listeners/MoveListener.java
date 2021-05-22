package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Team;

public class MoveListener implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		World world = gameManager.getMap();
		Player player = event.getPlayer();
		if(!(world == player.getWorld())) {
			return;
		}
		
		if(player.getLocation().getY() <= MainCommand.getConfigLocation("deathhight", world).getY()) {
			Team team = gameManager.getPlayerProfile(player).getTeam();
			Location location = MainCommand.getConfigLocation(team.getDebugColor() + ".spawn", world);
			player.teleport(location);
			Bukkit.broadcastMessage("Du bist gestorben");
		}
	}
	
}
