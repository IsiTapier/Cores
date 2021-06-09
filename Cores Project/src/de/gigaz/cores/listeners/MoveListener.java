package de.gigaz.cores.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Gamerules;
import de.gigaz.cores.util.Team;

public class MoveListener implements Listener {
	
	private GameManager gameManager = Main.getPlugin().getGameManager();
	private int floorHight = 0;
	private World map;

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if(!Gamerules.getValue(Gamerules.autoBlockPlace))
			return;
		if(gameManager.getCurrentGameState() != GameState.INGAME_STATE)
			return;
		Player player = event.getPlayer();
		World world = Main.getPlugin().getWorld("currentworld");
		if(player.getWorld() != world)
			return;
        Location location = event.getTo().clone();
		if(floorHight == 0 || map != gameManager.getMap()) {
			floorHight = gameManager.getFloorHight();
			map = gameManager.getMap();
		}
		if(location.getY() > floorHight)
			return;
		location.setY(location.getY()-1);
		Block block = world.getBlockAt(location);
		if(block.getType().equals(Material.AIR)) {
			location.setY(location.getY()-1);
			if(!world.getBlockAt(location).getType().equals(Material.AIR))
				return;
			PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
			Inventory inventory = player.getInventory();
			if(playerProfile.getTeam().equals(Team.BLUE)) {
				if(!placeBlock(block, inventory, Material.WARPED_PLANKS))
					placeBlock(block, inventory, Material.WARPED_STEM);
			} else if(playerProfile.getTeam().equals(Team.RED)) {
				if(!placeBlock(block, inventory, Material.CRIMSON_PLANKS))
					placeBlock(block, inventory, Material.CRIMSON_STEM);
			}
		}
	}
	
	private boolean placeBlock(Block block, Inventory inventory, Material material) {
		if(inventory.contains(material)) {
			block.setType(material);
			int slot = inventory.first(material);
			inventory.getItem(slot).setAmount(inventory.getItem(slot).getAmount()-1);
			return true;
		}
		return false;
	}
	
}
