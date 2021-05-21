package de.gigaz.cores.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gigaz.cores.main.Main;

public class MainCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			if(player.hasPermission("cores.admin")) {
				if(args[0].equalsIgnoreCase("configure")) {				
					if(args.length == 2) {
						player.teleport(Main.getPlugin().getWorld(args[1]).getSpawnLocation());
					}
				}
				if(args[0].equalsIgnoreCase("setlobby")) {
					if(args.length == 1) {
						//setlobby
					}
				} else if(args[0].equalsIgnoreCase("setspawn")) {
					if(args[1].equalsIgnoreCase("blue")) {
						
					} else if(args[1].equalsIgnoreCase("red")) {
						
					} else {
						
					}
				} else if(args[0].equalsIgnoreCase("setcore")) {
					if(args[1].equalsIgnoreCase("blue")) {
						
					} else if(args[1].equalsIgnoreCase("red")) {
						
					} else {
						
					}
				} else if(args[0].equalsIgnoreCase("start")) {
					
				} else if(args[0].equalsIgnoreCase("stop")) {
					
				} else if(args[0].equalsIgnoreCase("join")) {
					if(args[1].equalsIgnoreCase("blue")) {
						
					} else if(args[1].equalsIgnoreCase("red")) {
						
					} else {
						
					}
				} else if(args[0].equalsIgnoreCase("setmap")) {
					
				}  else if(args[0].equalsIgnoreCase("deletemap")) {
					
				}
			}
		}
		return false;
		
	}

}
