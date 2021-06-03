package de.gigaz.cores.inventories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.Team;

public class ManageTeamsInventory {
	
	private static String title = "§8Manage Teams";
	private static int rows =3;// (int) Math.ceil(Main.getPlugin().getGameManager().getPlayerProfiles().size()/2.0)*2;

	
	private static Inventory buildInventory(String title, int rows) {
		Inventory inventory = Bukkit.createInventory(null, rows*9, title);
		int index = 0;
		GameManager gameManager = Main.getPlugin().getGameManager();
		
		ItemStack barrier = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemStack random = new ItemBuilder(Material.COMMAND_BLOCK).setName(Team.UNSET.getDisplayColor()).hideEnchants().build();
		for(PlayerProfile playerProfile : gameManager.getPlayerProfiles()) {
			Player player = playerProfile.getPlayer();
			if(index%9==4) {
				inventory.setItem(index, barrier);
				index++;
			}
			/*ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(player.getName());
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + player.getName());
            skull.setItemMeta(meta);*/
			ItemStack skull = createSkull(getHeadValue(player.getName()));
			inventory.setItem(index, skull);
			inventory.setItem(index+1, Inventories.getTeamBlueSelector().build());
			inventory.setItem(index+2, Inventories.getTeamRedSelector().build());
			inventory.setItem(index+3, random);
			index+=4;
			if(index%9==4) {
				inventory.setItem(index, barrier);
				index++;
			}
			inventory.setItem(index, skull);
			inventory.setItem(index+1, Inventories.getTeamBlueSelector().build());
			inventory.setItem(index+2, Inventories.getTeamRedSelector().build());
			inventory.setItem(index+3, random);
			index+=4;
			if(index%9==4) {
				inventory.setItem(index, barrier);
				index++;
			}
			inventory.setItem(index, skull);
			inventory.setItem(index+1, Inventories.getTeamBlueSelector().build());
			inventory.setItem(index+2, Inventories.getTeamRedSelector().build());
			inventory.setItem(index+3, random);
			index+=4;
		}
		
		
		return inventory;
	}
	
	public static Inventory getInventory() {
		return buildInventory(title, rows);
	}
	
	@SuppressWarnings("deprecation")
	private static ItemStack createSkull(String url) {
        ItemStack head = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
        if (url.isEmpty())
            return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }
	
	static String getHeadValue(String name){
	    try {
	        String result = getURLContent("https://api.mojang.com/users/profiles/minecraft/" + name);
	        Gson g = new Gson();
	        JsonObject obj = g.fromJson(result, JsonObject.class);
	        String uid = obj.get("id").toString().replace("\"","");
	        String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uid);
	        obj = g.fromJson(signature, JsonObject.class);
	        String value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
	        String decoded = new String(Base64.getDecoder().decode(value));
	        obj = g.fromJson(decoded,JsonObject.class);
	        String skinURL = obj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
	        byte[] skinByte = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes();
	        return new String(Base64.getEncoder().encode(skinByte));
	    } catch (Exception ignored){ }
	    return null;
	}
	
	private static String getURLContent(String urlStr) {
        URL url;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try{
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8) );
            String str;
            while((str = in.readLine()) != null) {
                sb.append( str );
            }
        } catch (Exception ignored) { }
        finally{
            try{
                if(in!=null) {
                    in.close();
                }
            }catch(IOException ignored) { }
        }
        return sb.toString();
    }
}
	
	