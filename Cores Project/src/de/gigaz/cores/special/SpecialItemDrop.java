package de.gigaz.cores.special;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.GameruleSetting;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Gamerules;
import de.gigaz.cores.util.ItemBuilder;

public class SpecialItemDrop {
    
	
	// INFO: If you change the contentent of this arraylist, be aware to manually remove the actionBlock section in the config afterwards
    public static ArrayList<SpecialItemDrop> specialItems = new ArrayList<SpecialItemDrop>() {{
    	add(new SpecialItemDrop("§dRepair-Item", new ItemBuilder(Material.END_CRYSTAL).build(), 200, Gamerules.getGameruleValue(Gamerules.repairCore), false));
        add(new SpecialItemDrop("§6Kartoffel", new ItemBuilder(Material.POTATO).build(), 20, true, true));
        add(new SpecialItemDrop("§3Diamante", new ItemBuilder(Material.DIAMOND).build(), 400, true, true));
    }};

    private GameManager gameManager = Main.getPlugin().getGameManager();
    private ItemStack item;
    private String broadcastMessage;
    private int spawnInterval;
    private boolean isActive; 
    private boolean isSpawningAtStart;
    private String name;

    public SpecialItemDrop(String name, ItemStack item, int spawnInterval, boolean isActive, boolean isSpawningAtStart, String broadcastMessage) {
        this.item = item;
        this.name = name;
        this.spawnInterval = spawnInterval;
        this.isActive = isActive;
        this.broadcastMessage = broadcastMessage;
        this.isSpawningAtStart = isSpawningAtStart;
    }

    public SpecialItemDrop(String name, ItemStack item, int spawnInterval, boolean isActive, boolean isSpawningAtStart) {
        this.item = item;
        this.spawnInterval = spawnInterval;
        this.isActive = isActive;
        this.isSpawningAtStart = isSpawningAtStart;
        this.broadcastMessage = "";
        this.name = name;
    }

    public void spawnItem(Location location) {
        Bukkit.broadcastMessage(location.getWorld() + "");
        gameManager.getCopiedWorld().dropItemNaturally(location, item);      
    }

    public void broadcastMessage() {
        if(broadcastMessage == "") {
            Bukkit.broadcastMessage(Main.PREFIX + "§8§k--§r§7" + name + "§8§k--§r§7" + "§7 ist gespawned");
        }
    }

    public ItemStack getItem() {
        return item;
    }

    public String getBroadcastMessage() {
        return broadcastMessage;
    }
    
    public int getSpawnInterval() {
        return spawnInterval;
    }
    
    public boolean isActive() {
		return isActive;
	}

	public boolean isSpawningAtStart() {
        return isSpawningAtStart;
    }

    public static ArrayList<SpecialItemDrop> getSpecialItems() {
        return specialItems;
    }

    public String getName() {
        return name;
    }

    public static SpecialItemDrop getItemByName(String name) {
        for(SpecialItemDrop item : getSpecialItems()) {
            if(item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
