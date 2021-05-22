package de.gigaz.cores.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Team;

public class BreakBlockListener implements Listener {

	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		if(event.getBlock().getType().equals(Material.BEACON)) {
			if(!(Main.getPlugin().getGameManager().getCurrentGameState() == GameState.INGAME_STATE))
				return;
			
			GameManager gameManager = Main.getPlugin().getGameManager();
			Player player = event.getPlayer();
			FileConfiguration config = Main.getPlugin().getConfig();
			Team team = gameManager.getPlayerProfile(player).getTeam();
			World world = gameManager.getMap();
			Location location = event.getBlock().getLocation();

			if(MainCommand.getConfigLocation(team.getOponentColor() + ".core.1", world).equals(location) && gameManager.getCoreState(team.getOponentTeam(), false)) {
				player.sendMessage(Main.PREFIX + "Du hast einen Core zerstört!");
				gameManager.setCoreState(team.getOponentTeam(), false, false);
				player.sendMessage(Main.PREFIX + "Du hast einen Core zerstört!");
				event.setCancelled(true);
				event.getBlock().setType(Material.BEDROCK);
			} else if(MainCommand.getConfigLocation(team.getOponentColor() + ".core.2", world).equals(location) && gameManager.getCoreState(team.getOponentTeam(), true)) {
				
				player.sendMessage(Main.PREFIX + "Du hast einen Core zerstört!");
				gameManager.setCoreState(team.getOponentTeam(), true, false);
				event.setCancelled(true);
				event.getBlock().setType(Material.BEDROCK);
			} else if(MainCommand.getConfigLocation(team.getDebugColor() + ".core.1", world).equals(location) || MainCommand.getConfigLocation(team.getDebugColor() + ".core.2", world).equals(location)) {
				player.sendMessage(Main.PREFIX + "Du kannst deinen eigenen Core nicht zerstören. Gehe zu deinen Gegnern!");
				event.setCancelled(true);
			}
		}
	}	
}
