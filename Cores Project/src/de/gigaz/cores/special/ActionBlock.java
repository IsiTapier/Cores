package de.gigaz.cores.special;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.ItemBuilder;

public class ActionBlock {
    
	public static final Material ACTION_BLOCK_MATERIAL = Material.RESPAWN_ANCHOR;
	
    FileConfiguration config = Main.getPlugin().getConfig();
    private Material oldBlockMaterial;
    private Location location;
    private ArrayList<SpecialItemDrop> items = new ArrayList<SpecialItemDrop>();
    private int number;
    
    public ActionBlock(Location location, Material oldBlockMaterial) {
    	
        this.location = location;
        this.oldBlockMaterial = oldBlockMaterial;
        
        location.getWorld().getBlockAt(location).setType(ACTION_BLOCK_MATERIAL);             
    }

    public void spawnItem() {
        for(SpecialItemDrop item : items) {
            item.spawnItem(location);
        }       
    }

    public Material getOldBlockMaterial() {
        return oldBlockMaterial;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<SpecialItemDrop> getItems() {
        return items;
    }

    public World getWorld() {
        return location.getWorld();
    }
    
    public void replaceOldBlock() {
    	location.getBlock().setType(oldBlockMaterial);
    }

    public int getNumber() {
		return number;
	}


    public void addSpecialItem(SpecialItemDrop specialItem) {
        if(!items.contains(specialItem)) {
        	GameManager gameManager = Main.getPlugin().getGameManager();
        	String root = Main.CONFIG_ROOT + "actionBlocks." + (gameManager.getActionBlocks().size() + ".specialItems");
        	List<String> specialItems = config.getStringList(root);
        	for(String name : specialItems) {
        		System.out.println(name);
        	}
        	specialItems.add(specialItem.getName());
        	config.set(root, specialItems);
            items.add(specialItem);
            System.out.println("added item " + specialItem.getName());
            Main.getPlugin().saveConfig();
        }
        
    }
    public void removeSpecialItem(SpecialItemDrop specialItem) {
        if(items.contains(specialItem)) {
        	GameManager gameManager = Main.getPlugin().getGameManager();
        	String root = Main.CONFIG_ROOT + "actionBlocks." + (gameManager.getActionBlocks().size() + ".specialItems");
        	List<String> specialItems = config.getStringList(root);
        	specialItems.remove(specialItem.getName());
        	config.set(root, specialItems);
            items.remove(specialItem);
            Main.getPlugin().saveConfig();
        }
    }

    public void saveInConfig() {
    	GameManager gameManager = Main.getPlugin().getGameManager();
    	gameManager.getActionBlocks().add(this);
    	String root = Main.CONFIG_ROOT + "actionBlocks." + (gameManager.getActionBlocks().size());
        this.number = gameManager.getActionBlocks().size();
        
        config.set(root + ".location", location);
        config.set(root + ".oldMaterial", new ItemBuilder(oldBlockMaterial).build());
        ArrayList<String> names = new ArrayList<String>();
        for(SpecialItemDrop item : items) {
            names.add(item.getName());          
        }
        config.set(root + ".specialItems", names);
        config.set(Main.CONFIG_ROOT + "actionBlocks.size", gameManager.getActionBlocks().size());
        
        Main.getPlugin().saveConfig();
    }

    public static ActionBlock getFromConfig(String root) {
        FileConfiguration config = Main.getPlugin().getConfig();
        if(config.contains(root + ".location")) {
            if(config.contains(root + ".oldMaterial")) {
                List<String> output = new ArrayList<String>();
                if(config.contains(root + ".specialItems")) {
                    output = config.getStringList(root + ".specialItems");
                }
                Location t_loc = (Location) config.get(root + ".location");
                ItemStack t_is = (ItemStack) config.get(root + ".oldMaterial");
                Material t_material = t_is.getType();
                ActionBlock actionBlock = new ActionBlock(t_loc, t_material);
                for(String name : output) {
                    actionBlock.addSpecialItem(SpecialItemDrop.getItemByName(name));
                }
                
                return actionBlock;
            }
        }
        return null;
    }
    
}
