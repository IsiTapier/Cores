package de.gigaz.cores.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.GameruleSetting;
import de.gigaz.cores.classes.GameruleSetting.GameruleCategory;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Gamerules;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.SkullCreator;

public class GameruleSettings {
	
	private static final String categoryMenuTitle = "Gamerule Category Menu";
	private static final String settingsMenuTitle = "Gamerule Settings";
	
	private static final boolean moreInputsForLargeMenu = true;
	
	private static ItemStack disable = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("disabled").build();
	private static ItemStack enable = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("enabled").build();
	private static ItemStack barrier = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build();
	private static ItemStack last = ItemBuilder.rename(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFjOTZhNWMzZDEzYzMxOTkxODNlMWJjN2YwODZmNTRjYTJhNjUyNzEyNjMwM2FjOGUyNWQ2M2UxNmI2NGNjZiJ9fX0="),"last page");
	private static ItemStack next = ItemBuilder.rename(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMzYWU4ZGU3ZWQwNzllMzhkMmM4MmRkNDJiNzRjZmNiZDk0YjM0ODAzNDhkYmI1ZWNkOTNkYThiODEwMTVlMyJ9fX0="),"next page");
	private static ItemStack lastGray = ItemBuilder.rename(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY5NzFkZDg4MWRiYWY0ZmQ2YmNhYTkzNjE0NDkzYzYxMmY4Njk2NDFlZDU5ZDFjOTM2M2EzNjY2YTVmYTYifX19="),"no further page");
	private static ItemStack nextGray = ItemBuilder.rename(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMyY2E2NjA1NmI3Mjg2M2U5OGY3ZjMyYmQ3ZDk0YzdhMGQ3OTZhZjY5MWM5YWMzYTkxMzYzMzEzNTIyODhmOSJ9fX0=="),"no further page");
	private static ItemStack home = new ItemBuilder(Material.WRITABLE_BOOK).setName(settingsMenuTitle).build();
	
	public static Inventory buildCategoryMenu() {
		GameManager gameManager = Main.getPlugin().getGameManager();
		Inventory inventory = Bukkit.createInventory(null, 1*9, categoryMenuTitle);
		inventory.setItem(2, GameruleCategory.INVENTORY.getItem());
		inventory.setItem(4, GameruleCategory.GAME.getItem()); 
		inventory.setItem(6, GameruleCategory.SPECIAL.getItem()); 
		for(int i = 0; i < 9; i++)
			if(inventory.getItem(i) == null)
				inventory.setItem(i, barrier);
		return inventory;
	}
	
	public static Inventory buildInventory(GameruleCategory category, Player player) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
		ArrayList<GameruleSetting> gamerules = new ArrayList<GameruleSetting>();
		for(Entry<String, GameruleSetting> gameruleSetting : Gamerules.getGameruleSettings().entrySet())
			if(gameruleSetting.getValue().getCategory().equals(category))
				gamerules.add(gameruleSetting.getValue());
		for(int i = 1; i < gamerules.size(); i++) {
			for(int j = 1; j < gamerules.size(); j++) {
				if(gamerules.get(j).getPriority() < gamerules.get(j-1).getPriority()) {
					GameruleSetting temp = gamerules.get(j);
					gamerules.set(j, gamerules.get(j-1));
					gamerules.set(j-1, temp);
				}
			}
		}
		int inputs = (Gamerules.getValue(Gamerules.moreGamerules)?3:(Gamerules.getValue(Gamerules.evenMoreGamerules)?4:2));
		int rows = (int)Math.ceil((double)gamerules.size()/inputs);
		int pages = (int)Math.ceil((double)rows/5);
		int page = playerProfile.getGamerulePage();
		if(page < 0)
			page = pages-1;
		if(page >= pages)
			page = 0;
		rows -= 5*page;
		if(rows > 5)
			rows = 5;
		playerProfile.setGamerulePage(page);
		Inventory inventory = Bukkit.createInventory(null, (rows+1)*9, settingsMenuTitle);
		int i = 0;
		for(GameruleSetting gamerule : gamerules) {
			if(gamerules.indexOf(gamerule) < page*5*inputs)
				continue;
			if(i%9==0&&!Gamerules.getValue(Gamerules.moreGamerules)) {
				inventory.setItem((i), barrier);
				i++;
			}
			ItemStack inputSlot = null;
			if(gamerule.getValue()) {
				if(!Gamerules.getValue(Gamerules.evenMoreGamerules)) {
					ItemMeta meta = enable.getItemMeta();
					meta.addEnchant(Enchantment.ARROW_INFINITE, 10, true);
					enable.setItemMeta(meta);
					if(disable.containsEnchantment(Enchantment.ARROW_INFINITE))
						disable.removeEnchantment(Enchantment.ARROW_INFINITE);
				} else
					inputSlot = enable;
			} else {
				if(!Gamerules.getValue(Gamerules.evenMoreGamerules)) {
					ItemMeta meta = disable.getItemMeta();
					meta.addEnchant(Enchantment.ARROW_INFINITE, 10, true);
					disable.setItemMeta(meta);
					if(enable.containsEnchantment(Enchantment.ARROW_INFINITE))
						enable.removeEnchantment(Enchantment.ARROW_INFINITE);
				} else
					inputSlot = disable;
			}
			if(!Gamerules.getValue(Gamerules.evenMoreGamerules)) {
				inventory.setItem(i, disable);
				inventory.setItem(i+1, gamerule.getItem());
				inventory.setItem(i+2, enable);
				if(Gamerules.getValue(Gamerules.moreGamerules))
					i+=3;
				else {
					inventory.setItem(i+3, barrier);
					i+=4;
				}
			} else {
				inventory.setItem(i, gamerule.getItem());
				inventory.setItem(i+1, inputSlot);
				i+=2;
			}
			if(i >= rows*9)
				break;
		}
		while(i%9>0) {
			inventory.setItem(i, barrier);
			i++;
		}
		inventory.setItem(i, category.getItem());
		inventory.setItem(i+1, barrier);
		inventory.setItem(i+2, barrier);
		inventory.setItem(i+3, pages==1?lastGray:last);
		inventory.setItem(i+4, new ItemBuilder(Material.PAPER).setName((page+1)+"/"+pages).build());
		inventory.setItem(i+5, pages==1?nextGray:next);
		inventory.setItem(i+6, barrier);
		inventory.setItem(i+7, barrier);
		inventory.setItem(i+8, home);
		//inventory.setItem(0, category.getItem());
		//inventory.setItem((rows>2?1:0)*9, home);
		//inventory.setItem((rows>2?2:rows-1)*9, next);
		return inventory;
	}
	
	public static boolean getUiMode() {
		return !Gamerules.getValue(Gamerules.evenMoreGamerules);
	}
	
	public static ItemStack getEnable() {
		if(enable.containsEnchantment(Enchantment.ARROW_INFINITE))
			enable.removeEnchantment(Enchantment.ARROW_INFINITE);
		return enable;
	}
	
	public static ItemStack getDisable() {
		if(disable.containsEnchantment(Enchantment.ARROW_INFINITE))
			disable.removeEnchantment(Enchantment.ARROW_INFINITE);
		return disable;
	}
	
	public static void lastPage(Player player) {
		PlayerProfile playerProfile = Main.getPlugin().getGameManager().getPlayerProfile(player);
		playerProfile.setGamerulePage(playerProfile.getGamerulePage()-1);
		player.openInventory(buildInventory(GameruleCategory.getCategory(player.getOpenInventory().getTopInventory()), player));
	}
	
	public static void nextPage(Player player) {
		PlayerProfile playerProfile = Main.getPlugin().getGameManager().getPlayerProfile(player);
		playerProfile.setGamerulePage(playerProfile.getGamerulePage()+1);
		player.openInventory(buildInventory(GameruleCategory.getCategory(player.getOpenInventory().getTopInventory()), player));
	}
	
	public static String getCategorymenutitle() {
		return categoryMenuTitle;
	}
	
	public static String getSettingsmenutitle() {
		return settingsMenuTitle;
	}
	
	public static ItemStack getLastPageItem() {
		return last;
	}
	
	public static ItemStack getNextPageItem() {
		return next;
	}
	
	public static ItemStack getMainMenuItem() {
		return home;
	}
}
