package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GUIs;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.Team;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void onInventoryInteract(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		ItemStack item = event.getCurrentItem();
		
		if(player.getOpenInventory().getTitle().equalsIgnoreCase(Inventories.getAdminTool().getName())) {
			Bukkit.broadcastMessage(GUIs.getAdminToolInventory().getType().getDefaultTitle());
			if(item.getType() == GUIs.getAdminEditMode().getType()) {
				player.chat("/c edit");	
			}
			if(item.getType() == GUIs.getAdminEditTeams().getType()) {
				player.sendMessage(Main.PREFIX + "§7edit teams");	
			}
			if(item.getType() == GUIs.getAdminSelectMap().getType()) {
				player.closeInventory();
				player.openInventory(GUIs.getMapSelectInventory());		
			}
			if(item.getType() == GUIs.getAdminStartGame().getType()) {
				player.chat("/c start");
			}
			event.setCancelled(true);
			player.closeInventory();
			
		}
	
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		PlayerProfile playerProfile = Main.getPlugin().getGameManager().getPlayerProfile(player);
		Inventory inventory = event.getInventory();
		String name = player.getOpenInventory().getTitle();
		if(name.contentEquals("customize inventory")) {
			playerProfile.setInventory(inventory);
		}
	}

}
