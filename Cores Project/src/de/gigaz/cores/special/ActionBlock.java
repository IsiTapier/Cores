package de.gigaz.cores.special;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Material;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.main.Main;

public class ActionBlock {
    
    private GameManager gameManager = Main.getPlugin().getGameManager();
    private Material oldBlockMaterial;
    private Location location;
    private ArrayList<SpecialItemDrop> items = new ArrayList<SpecialItemDrop>();
    
    public ActionBlock(Location location, Material oldBlockMaterial) {
        this.location = location;
        this.oldBlockMaterial = oldBlockMaterial;
        items.add(SpecialItemDrop.getSpecialItems().get(0));       
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

    public void addSpecialItem(SpecialItemDrop specialItem) {
        if(!items.contains(specialItem)) {
            items.add(specialItem);
        }
        
    }
    public void removeSpecialItem(SpecialItemDrop specialItem) {
        if(items.contains(specialItem)) {
            items.remove(specialItem);
        }
    }

    public void saveInConfig(String root) {
        FileConfiguration config = Main.getPlugin().getConfig();
        config.set(root + ".location", location);
        config.set(root + ".oldMaterial", oldBlockMaterial);
        for(SpecialItemDrop item : items) {
            //config.set(root + ".specialItems.");
            //ItemStack
            //config.set
        }      
    }

    public static ActionBlock getFromConfig(String root) {

        return null;
    }
    
}
