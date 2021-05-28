package de.gigaz.cores.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Team;

public class DeathListener implements Listener {
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		GameManager gameManager = Main.getPlugin().getGameManager();
		World world = gameManager.getMap();
		Player player = event.getEntity();
		event.setDeathMessage(null);
		Team team = gameManager.getPlayerProfile(player).getTeam();
		Location location = MainCommand.getConfigLocation(team.getDebugColor() + ".spawn", world);
		player.teleport(location);
	}
}
