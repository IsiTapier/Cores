package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.GameruleSetting;
import de.gigaz.cores.classes.GameruleSetting.GameruleCategory;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.inventories.AdminToolInventory;
import de.gigaz.cores.inventories.GameruleSettings;
import de.gigaz.cores.inventories.ManageTeamsInventory;
import de.gigaz.cores.inventories.MapSelectInventory;
import de.gigaz.cores.inventories.MultiToolInventory;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Gamerules;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ItemBuilder;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void onInventoryInteract(InventoryClickEvent event) {
		InventoryAction action = event.getAction();
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		Inventory clicked = event.getClickedInventory();
		ItemStack item = event.getCurrentItem();
		if(item == null)
			return;
		GameManager gameManager = Main.getPlugin().getGameManager();
		int slot = event.getSlot();
		
		if(clicked.getType().equals(InventoryType.ANVIL)) {
			return;
		}
		
		if(gameManager.getCurrentGameState() != GameState.INGAME_STATE || !player.getWorld().equals(Main.getPlugin().getWorld("currentworld"))) {
			if(player.getOpenInventory().getTitle().equalsIgnoreCase(AdminToolInventory.getTitle())) {
				
				if(item.getType() == AdminToolInventory.getAdminEditMode().getType()) {
					player.chat("/c edit");	
					player.closeInventory();
				}
				if(item.getType() == AdminToolInventory.getAdminEditTeams().getType()) {
					player.sendMessage(Main.PREFIX + "§7edit teams");
					//player.closeInventory();
					player.openInventory(ManageTeamsInventory.getInventory());
				}
				if(item.getType() == AdminToolInventory.getAdminSelectMap().getType()) {
					player.closeInventory();
					player.openInventory(MapSelectInventory.getAdminInventory());		
				}
				if(item.getType() == AdminToolInventory.getAdminStartGame().getType()) {
					player.chat("/c start");
					player.closeInventory();
				}
				event.setCancelled(true);
				
			} else if(player.getOpenInventory().getTitle().equalsIgnoreCase(MapSelectInventory.getTitle(true))) {
				event.setCancelled(true);
				if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
					if(item.getType().equals(Material.BARRIER) || item.getType().equals(Material.WHITE_STAINED_GLASS_PANE)) {
						player.sendMessage(Main.PREFIX+"Diese Welt ist nicht vollständig konfiguriert und kann daher nicht ausgewählt werden");
						return;
					}
					player.getOpenInventory().close();
					player.chat("/c setmap " + item.getItemMeta().getDisplayName());
				}
						
			} else if(player.getOpenInventory().getTitle().equalsIgnoreCase(MapSelectInventory.getTitle(false))) {
				event.setCancelled(true);
				if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
					if(item.getType().equals(Material.BARRIER) || item.getType().equals(Material.WHITE_STAINED_GLASS_PANE)) {
						player.sendMessage(Main.PREFIX+"Diese Welt ist nicht vollständig konfiguriert und kann daher nicht ausgewählt werden");
						return;
					}
					player.getOpenInventory().close();
					player.chat("/c votemap " + item.getItemMeta().getDisplayName());
				}
						
			} else if(player.getOpenInventory().getTitle().equalsIgnoreCase(MultiToolInventory.getTitle())) {
				if(item.getType().equals(Inventories.getTeamRedSelector().build().getType())) {
					player.chat("/c join red");
				}
				if(item.getType().equals(Inventories.getTeamBlueSelector().build().getType())) {
					player.chat("/c join blue");
				}
				player.closeInventory();
				event.setCancelled(true);
				
			} else if(player.getOpenInventory().getTitle().equalsIgnoreCase(ManageTeamsInventory.getTitle())) {
				event.setCancelled(true);
				if(item.getType().equals(Material.PLAYER_HEAD))
					return;
				if(item.getType().equals(Material.GRAY_STAINED_GLASS_PANE))
					return;
				if(item.containsEnchantment(Enchantment.ARROW_INFINITE))
					item.removeEnchantment(Enchantment.ARROW_INFINITE);
				int shift = 0;
				if(item.equals(Inventories.getTeamBlueSelector().disenchant().build()))
					shift = 1;
				else if(item.equals(Inventories.getTeamRedSelector().disenchant().build()))
					shift = 2;
				else if(item.getType().equals(Material.COMMAND_BLOCK))
					shift = 3;
				Player selectedPlayer = Bukkit.getPlayer(clicked.getItem(slot-shift).getItemMeta().getDisplayName());
				if(item.equals(Inventories.getTeamBlueSelector().disenchant().build()))
					/*Bukkit.broadcastMessage(selectedPlayer.getName()+" blue");*/selectedPlayer.chat("/c join blue");
				else if(item.equals(Inventories.getTeamRedSelector().disenchant().build()))
					/*Bukkit.broadcastMessage(selectedPlayer.getName()+" red");*/selectedPlayer.chat("/c join red");
				else if(item.getType().equals(Material.COMMAND_BLOCK))
					/*Bukkit.broadcastMessage(selectedPlayer.getName()+" random");*/selectedPlayer.chat("/c join random");
				player.openInventory(ManageTeamsInventory.getInventory());
				
			} else if(player.getOpenInventory().getTitle().equals(GameruleSettings.getCategorymenutitle())) {
				event.setCancelled(true);
				if(item.equals(GameruleSettings.getReset()))
					Gamerules.reset();
				GameruleCategory category = GameruleCategory.getCategory(item);
				if(category == null)
					return;
				gameManager.getPlayerProfile(player).setGamerulePage(0);
				player.openInventory(GameruleSettings.buildInventory(category, player));
			} else if(player.getOpenInventory().getTitle().equals(GameruleSettings.getSettingsmenutitle())) {
				event.setCancelled(true);
				if(item.equals(GameruleSettings.getBarrier()) || (item.getType().equals(Material.PAPER) && slot == clicked.getSize()-5))
					return;
				if(item.equals(GameruleSettings.getMainMenuItem()))
					player.openInventory(GameruleSettings.buildCategoryMenu());
				if(item.equals(GameruleSettings.getLastPageItem()))
					GameruleSettings.lastPage(player);
				if(item.equals(GameruleSettings.getNextPageItem()))
					GameruleSettings.nextPage(player);
				GameruleCategory category = GameruleCategory.getCategory(clicked);
				if(ItemBuilder.removeLore(item).equals(ItemBuilder.removeLore(category.getItem()))) {
					Gamerules.reset(category);
					player.openInventory(GameruleSettings.buildInventory(category, player));
				}
				GameruleSetting setting = Gamerules.getGameruleSetting(item);
				if(setting != null) {
					setting.nextItem();
					player.openInventory(GameruleSettings.buildInventory(category, player));
				}
				if(!item.equals(GameruleSettings.getDisable()) && !item.equals(GameruleSettings.getEnable()))
					return;
				int shift;
				if(GameruleSettings.getUiMode() && item.equals(GameruleSettings.getDisable()))
					shift = 1;
				else
					shift = -1;
				setting = Gamerules.getGameruleSetting(player.getOpenInventory().getItem(slot+shift));
				if(GameruleSettings.getUiMode()) {
					if(item.equals(GameruleSettings.getDisable()))
						Gamerules.setGameruleSetting(setting, false);
					else
						Gamerules.setGameruleSetting(setting, true);
				} else
					Gamerules.setGameruleSetting(setting, !setting.getValue());//gameruleSetting.switchValue();
				player.openInventory(GameruleSettings.buildInventory(category, player));
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
			if(!(player.getOpenInventory().getTitle().contentEquals(Inventories.defaultInventoryName) || player.getOpenInventory().getTitle().equals(gameManager.getPlayerProfile(player).getInventoryName())))
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
		if(name.contentEquals(Inventories.defaultInventoryName) || name.contentEquals(playerProfile.getInventoryName())) {
			if(!event.getView().getCursor().getType().equals(Material.AIR)) {
				inventory.addItem(event.getView().getCursor());
				//Inventories.setLobbyInventory(playerProfile);
				//TODO remove Items/prevent picking up items, no matching event or cause found yet, attention needed
			}
			playerProfile.setInventory(inventory);
		}
	}

}
