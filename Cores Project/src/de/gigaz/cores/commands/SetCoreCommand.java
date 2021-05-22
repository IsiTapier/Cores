package de.gigaz.cores.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Team;

public class SetCoreCommand {
	public SetCoreCommand(Player player, Team team, int number) {		
		Block block = player.getWorld().getBlockAt(player.getLocation());
		Location location = block.getLocation();
		location.setY(block.getLocation().getY() - 1);
		player.getWorld().getBlockAt(location).setType(Material.BEACON);
		MainCommand.setConfigLocation(team.getDebugColor() + ".core." + number, location);
		player.sendMessage(Main.PREFIX + "§7der Beacon " + number + " von " + team.getDisplayColor() + "§7 wurde gesetzt");
	}
}
