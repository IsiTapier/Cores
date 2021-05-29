package de.gigaz.cores.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.gigaz.cores.classes.Core;
import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Team;

public class SetCoreCommand {
	public SetCoreCommand(Player player, Team team, String number, String name) {		
		Block block = player.getWorld().getBlockAt(player.getLocation());
		Location location = block.getLocation();
		GameManager gameManager = Main.getPlugin().getGameManager();
		
		location.setY(block.getLocation().getY() - 1);
		player.getWorld().getBlockAt(location).setType(Material.BEACON);
		
		MainCommand.setConfigLocation(team.getDebugColor() + ".core." + number + ".location", location);
		if(name != null) {
			player.sendMessage(Main.PREFIX + "§7Der §7 Core §6" + name + "§7 von " + team.getDisplayColor() + "§7 wurde gesetzt");
			Core.setConfigCoreName(location, team, number, name);
		} else {
			player.sendMessage(Main.PREFIX + "§7Der Core " + number + " von " + team.getDisplayColor() + "§7 wurde gesetzt");
		}
		if(gameManager.getMap() != null) {
			player.sendMessage(Main.PREFIX + "§7Bereits konfigurierte Cores §7:");
			gameManager.registerCores();
			for(Core core : gameManager.getCores()) {		
				player.sendMessage("§8> " + core.getTeam().getColorCode() + core.getNumber() + "§8(§7Name: §6" + core.getName() + "§8)");
			}
		}
		
	}
	
	public SetCoreCommand(Player player, Team team, String number) {		
		new SetCoreCommand(player, team, number, null);
	}
}
