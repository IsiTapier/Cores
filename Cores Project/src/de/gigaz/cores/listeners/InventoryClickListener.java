package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.inventories.AdminToolInventory;
import de.gigaz.cores.inventories.MapSelectInventory;
import de.gigaz.cores.inventories.MultiToolInventory;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.Team;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void onInventoryInteract(InventoryClickEvent event) {
		InventoryAction action = event.getAction();
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		Inventory clicked = event.getClickedInventory();
		ItemStack item = event.getCurrentItem();
		GameManager gameManager = Main.getPlugin().getGameManager();
		
		if(gameManager.getCurrentGameState() != GameState.INGAME_STATE || !player.getWorld().equals(Main.getPlugin().getWorld("currentworld"))) {
			if(player.getOpenInventory().getTitle().equalsIgnoreCase(AdminToolInventory.getTitle())) {
				
				if(item.getType() == AdminToolInventory.getAdminEditMode().getType()) {
					player.chat("/c edit");	
					player.closeInventory();
				}
				if(item.getType() == AdminToolInventory.getAdminEditTeams().getType()) {
					player.sendMessage(Main.PREFIX + "§7edit teams");
					player.closeInventory();
				}
				if(item.getType() == AdminToolInventory.getAdminSelectMap().getType()) {
					player.closeInventory();
					player.openInventory(MapSelectInventory.getInventory());		
				}
				if(item.getType() == AdminToolInventory.getAdminStartGame().getType()) {
					player.chat("/c start");
					player.closeInventory();
				}
				
				event.setCancelled(true);	
			}
			if(player.getOpenInventory().getTitle().equalsIgnoreCase(MapSelectInventory.getTitle())) {
				player.chat("/c setmap " + item.getItemMeta().getDisplayName());
				event.setCancelled(true);		
			}
			if(player.getOpenInventory().getTitle().equalsIgnoreCase(MultiToolInventory.getTitle())) {
				if(item.getType().equals(Inventories.getTeamRedSelector().build().getType())) {
					player.chat("/c join red");
				}
				if(item.getType().equals(Inventories.getTeamBlueSelector().build().getType())) {
					player.chat("/c join blue");
				}
				player.closeInventory();
				event.setCancelled(true);		
			}
		}
		/*Bukkit.broadcastMessage(player.getInventory().toString());
		//Bukkit.broadcastMessage(player.getOpenInventory().toString());
		//Bukkit.broadcastMessage(player.getOpenInventory().getTopInventory().toString());
		//Bukkit.broadcastMessage(player.getOpenInventory().getBottomInventory().toString());
		Bukkit.broadcastMessage(clicked.toString());
		//Bukkit.broadcastMessage(inv.toString());
		Bukkit.broadcastMessage(String.valueOf(player.getInventory().equals(clicked)));*/
		
		
		//prevent Moving items between/in inventories
		if(player.getWorld().equals(gameManager.getLobbySpawn().getWorld()) && !gameManager.getPlayerProfile(player).isEditMode()) {
			if(!player.getOpenInventory().getTitle().contentEquals(Inventories.defaultInventoryName))
				event.setCancelled(true);
			if(player.getInventory().equals(clicked))
				event.setCancelled(true);
			if(inv != clicked)
				event.setCancelled(true);
			if(action.equals(InventoryAction.MOVE_TO_OTHER_INVENTORY))
				event.setCancelled(true);
			if(action.equals(InventoryAction.DROP_ALL_SLOT))
				event.setCancelled(true);
			if(action.equals(InventoryAction.DROP_ONE_SLOT))
				event.setCancelled(true);
			/*if(action.equals(InventoryAction.PLACE_ALL))
				event.setCancelled(true);
			if(action.equals(InventoryAction.PLACE_ONE))
				event.setCancelled(true);
			if(action.equals(InventoryAction.PLACE_SOME))
				event.setCancelled(true);*/
		}
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(player.getWorld().getName().equals(Main.getPlugin().getGameManager().getLobbySpawn().getWorld().getName()) && !Main.getPlugin().getGameManager().getPlayerProfile(player).isEditMode()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		PlayerProfile playerProfile = Main.getPlugin().getGameManager().getPlayerProfile(player);
		Inventory inventory = event.getInventory();
		String name = player.getOpenInventory().getTitle();
		if(name.contentEquals(Inventories.defaultInventoryName)) {
			if(!event.getView().getCursor().getType().equals(Material.AIR)) {
				inventory.addItem(event.getView().getCursor());
				//Inventories.setLobbyInventory(playerProfile);
				//TODO remove Items/prevent picking up items, no matching event or cause found yet, attention needed
			}
			playerProfile.setInventory(inventory);
		}
	}

}
