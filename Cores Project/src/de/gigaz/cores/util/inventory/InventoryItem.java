package de.gigaz.cores.util.inventory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.DecoderException;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Hex;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryItem {
	private int id;
	private int priority;
	private ItemStack item;
	private DisplayCondition condition;
	private ItemAmount amount;
	//TODO set item id
	
	public interface DisplayCondition {
        public boolean getCondition();
    }
	
	public interface ItemAmount {
		public int getAmount();
	}
	
	public InventoryItem(int id, int priority, ItemStack item, DisplayCondition condition, ItemAmount amount) {
		this.id = id;
		this.priority = priority;
		this.item = item;
		this.condition = condition;
		this.amount = amount;
	}
	
	public InventoryItem(int priority, ItemStack item, DisplayCondition condition, ItemAmount amount) {
		this.priority = priority;
		this.item = item;
		this.condition = condition;
		this.amount = amount;
	}
	
	public InventoryItem(ItemStack item, DisplayCondition condition, ItemAmount amount) {
		this.priority = -1;
		this.item = item;
		this.condition = condition;
		this.amount = amount;
	}
	
	public InventoryItem(int id, int priority, ItemStack item, DisplayCondition condition) {
		this.id = id;
		this.priority = priority;
		this.item = item;
		this.condition = condition;
	}
	
	public InventoryItem(int priority, ItemStack item, DisplayCondition condition) {
		this.priority = priority;
		this.item = item;
		this.condition = condition;
	}
	
	public InventoryItem(ItemStack item, DisplayCondition condition) {
		this.priority = -1;
		this.item = item;
		this.condition = condition;
	}
	
	public InventoryItem(int id,  InventoryItem instance) {
		this.id = id;
		this.priority = instance.getPriority();
		this.item = instance.getItem();
		this.condition = instance.getCondition();
		this.amount = instance.getAmount();
		setId(id);
	}

	public ItemAmount getAmount() {
		return amount;
	}

	public void setAmount(ItemAmount amount) {
		this.amount = amount;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		ItemMeta meta = item.getItemMeta();
		List<String> lore;
		if(meta.hasLore())
			lore = meta.getLore();
		else
			lore = new ArrayList<String>();	
		lore.add(convertToInvisibleString(String.valueOf(id)));
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public ItemStack getItem() {
		if(amount != null)
			item.setAmount(amount.getAmount());
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public DisplayCondition getCondition() {
		return condition;
	}

	public void setCondition(DisplayCondition condition) {
		this.condition = condition;
	}
	
	public boolean isValid() {
		return condition.getCondition();
	}
	
	//util
	public static String convertToInvisibleString(String text) {
		String hidden = "";
        for (char c : text.toCharArray()) hidden += ChatColor.COLOR_CHAR+""+c;
        return hidden;
    }
	
	public static String convertToVisibleString(String text) {
		return text.replaceAll("§", "");
    }
	
}
