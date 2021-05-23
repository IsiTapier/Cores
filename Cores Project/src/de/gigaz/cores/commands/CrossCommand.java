package de.gigaz.cores.commands;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gigaz.cores.main.Main;

public class CrossCommand implements CommandExecutor {
	
	private int taskID;
	private int counter;
	private Location crossLocation;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 127, 255), 1);
			crossLocation = player.getLocation();
			
			counter = 0;
			taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					for(int y = 0; y <= 12; y++) {
						player.spawnParticle(Particle.REDSTONE, new Location(player.getWorld(), crossLocation.getX(), crossLocation.getY() + y, crossLocation.getZ()), 20, dustOptions);
					}
					for(int z = 0; z <= 6; z++) {
						player.spawnParticle(Particle.REDSTONE, new Location(player.getWorld(), crossLocation.getX(), crossLocation.getY() + 9, crossLocation.getZ() - 3 + z), 20, dustOptions);
					}
							
					counter++;
					if(counter >= 1000)
						Bukkit.getScheduler().cancelTask(taskID);
				}
			}, 0, 1);

			
		}
		return false;
	}

}
