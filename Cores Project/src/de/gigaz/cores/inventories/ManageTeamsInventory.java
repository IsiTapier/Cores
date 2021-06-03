package de.gigaz.cores.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.SkullCreator;
import de.gigaz.cores.util.Team;

public class ManageTeamsInventory {
	
	private static String title = "§8Manage Teams";
	private static int rows = (int) Math.ceil(Main.getPlugin().getGameManager().getPlayerProfiles().size()/2.0);

	
	private static Inventory buildInventory(String title, int rows) {
		Inventory inventory = Bukkit.createInventory(null, rows*9, title);
		int index = 0;
		GameManager gameManager = Main.getPlugin().getGameManager();
		
		ItemStack barrier = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build();
		for(PlayerProfile playerProfile : gameManager.getPlayerProfiles()) {
			Player player = playerProfile.getPlayer();
			
			Inventories.getTeamBlueSelector().disenchant().clearLore();
			Inventories.getTeamRedSelector().disenchant().clearLore();
			Inventories.getTeamRandomSelector().disenchant().clearLore();
			switch(playerProfile.getTeam()) {
			case BLUE: Inventories.getTeamBlueSelector().addEnchantment(Enchantment.ARROW_INFINITE, 10); break;
			case RED: Inventories.getTeamRedSelector().addEnchantment(Enchantment.ARROW_INFINITE, 10); break;
			case UNSET: break;
			}
			
			ItemStack skull = ItemBuilder.rename(SkullCreator.itemFromUuid(player.getUniqueId()), player.getName());
			ItemStack red = Inventories.getTeamRedSelector().build();
			ItemStack blue = Inventories.getTeamBlueSelector().build();
			ItemStack random = Inventories.getTeamRandomSelector().build();
			
			if(index%9==4) {
				inventory.setItem(index, barrier);
				index++;
			}
			inventory.setItem(index, skull);
			inventory.setItem(index+1, blue);
			inventory.setItem(index+2, red);
			inventory.setItem(index+3, random);
			index+=4;
		}
		//fill rest
		while(index%9>0) {
			inventory.setItem(index, barrier);
			index++;
		}
		
		
		return inventory;
	}
	
	public static Inventory getInventory() {
		return buildInventory(title, rows);
	}
	
	public static String getTitle() {
		return title;
	}
}