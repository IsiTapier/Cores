package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Gamerules;

public class BuildBlockListener implements Listener {
	
	private final int instantWallTime = 5;
	
	@EventHandler
	public void onBuild(BlockPlaceEvent event) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		Block block = event.getBlock();
		Location location = block.getLocation();
		World world = location.getWorld();
		Player player = event.getPlayer();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
		
		if(gameManager.getCurrentGameState() != GameState.INGAME_STATE || !world.equals(gameManager.getCopiedWorld())) {
			//BauProtection via Edit Mode
			if(!playerProfile.isEditMode()) {
				event.setCancelled(true);
				player.sendMessage(Main.PREFIX + "§7Du bist §cnicht§7 berechtigt einen Block zu bauen");
			} 
		} else {
			if(!playerProfile.isEditMode()) {
				if((gameManager.checkCoreProtection(location) && Gamerules.getValue(Gamerules.coreProtection)) || (gameManager.checkSpawnProtection(location) && Gamerules.getValue(Gamerules.spawnProtection))) {
					event.setCancelled(true);
					player.sendMessage(Main.PREFIX + "§7Du darfst hier §ckeine §7Blöcke bauen");
				}
				if(Gamerules.getValue(Gamerules.instantWall) && block.getType().equals(Material.LIGHT_BLUE_STAINED_GLASS)) {
					player.getInventory().getItem(player.getInventory().first(Material.LIGHT_BLUE_STAINED_GLASS)).setAmount(player.getInventory().getItem(player.getInventory().first(Material.LIGHT_BLUE_STAINED_GLASS)).getAmount()-1);
					boolean modifyX = (player.getLocation().getYaw()%180 > 135 || player.getLocation().getYaw()%180 < -135 || (player.getLocation().getYaw()%180 > -45 && player.getLocation().getYaw()%180 < 45));
					for(int x = -2; x < 3; x++) {
						for(int y = 0; y < 5; y++) {
							Location newlocation = block.getLocation();
							newlocation.setY(newlocation.getY()+y);
							if(modifyX)
								newlocation.setX(newlocation.getX()+x);
							else
								newlocation.setZ(newlocation.getZ()+x);
							if(newlocation.getBlock().getType().equals(Material.AIR) && !((gameManager.checkCoreProtection(newlocation) && Gamerules.getValue(Gamerules.coreProtection)) || (gameManager.checkSpawnProtection(newlocation) && Gamerules.getValue(Gamerules.spawnProtection))))
								newlocation.getBlock().setType(Material.LIGHT_BLUE_STAINED_GLASS);
						}
					}
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

						@Override
						public void run() {
							for(int x = -2; x < 3; x++) {
								for(int y = 0; y < 5; y++) {
									Location newlocation = block.getLocation();
									newlocation.setY(newlocation.getY()+y);
									if(modifyX)
										newlocation.setX(newlocation.getX()+x);
									else
										newlocation.setZ(newlocation.getZ()+x);
									if(newlocation.getBlock().getType().equals(Material.LIGHT_BLUE_STAINED_GLASS))
										newlocation.getBlock().setType(Material.AIR);
								}
							}
						}}, instantWallTime*20L);
				}
				if(block.getType().equals(Material.TNT)) {
					if(event.isCancelled())
						return;
					event.setCancelled(true);
					player.getInventory().getItem(player.getInventory().first(Material.TNT)).setAmount(player.getInventory().getItem(player.getInventory().first(Material.TNT)).getAmount()-1);
					TNTPrimed tnt = block.getWorld().spawn(block.getLocation().add(0.5, 0, 0.5), TNTPrimed.class);
					tnt.setGravity(false);
					tnt.setVelocity(new Vector(0, 0, 0));
				}
			} else {
				player.sendMessage("§8[§7Hinweis§8] §7Du bearbeitest gerade die Map: §6" + world.getName());
			}
		}
		

	}
}
