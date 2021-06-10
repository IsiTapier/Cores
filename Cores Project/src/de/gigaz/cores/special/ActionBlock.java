package de.gigaz.cores.special;

import java.util.ArrayList;
import java.util.List;

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
        ArrayList<String> names = new ArrayList<String>();
        for(SpecialItemDrop item : items) {
            names.add(item.getName());          
        }    
        config.set(root + ".specialItems", names);  
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
                Material t_material = (Material) config.get(root + ".oldMaterial");
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
