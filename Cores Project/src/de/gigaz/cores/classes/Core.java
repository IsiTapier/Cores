package de.gigaz.cores.classes;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Team;

public class Core {

	private Location location;
	private Team team;
	private String number;
	private String name;
	private boolean attacked = false;

	public Core(Location location, Team team, String number) {
		this.location = location;
		this.team = team;
		this.number = number;
		this.name = getConfigName();
	}

	
	public static void setConfigCoreName(Location location, Team team, String number, String name) {
		FileConfiguration config = Main.getPlugin().getConfig();
		config.set(Main.CONFIG_ROOT + "worlds." + location.getWorld().getName() + "." + team.getDebugColor() + ".core." + number + ".name", name);
		//config.set(Main.CONFIG_ROOT + "worlds." + location.getWorld().getName() + "." + team.getDebugColor() + ".core." + number + ".name", "testname");
		Main.getPlugin().saveConfig();
	}
	
	public static void setConfigCoreName(Core core, String name) {
		setConfigCoreName(core.getLocation(), core.getTeam(), core.getNumber(), name);
	}
	
	public static String getConfigCoreName(Location location, Team team, String number, String name) {
		FileConfiguration config = Main.getPlugin().getConfig();
		return config.getString(Main.CONFIG_ROOT + "worlds." + location.getWorld().getName() + "." + team.getDebugColor() + ".core." + number + ".name");
	}
	
	public String getConfigName() {
		FileConfiguration config = Main.getPlugin().getConfig();
		String root = Main.CONFIG_ROOT + "worlds." + location.getWorld().getName() + "." + team.getDebugColor() + ".core." + number;
		if(config.contains(root + ".name")) {
			return config.getString(root + ".name");
		} else {
			return null;
		}	
	}
	
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
	
		this.name = name;
	}
	
	public String getDisplayName() {
		if(name == null) {
			return number;
		}
		return name;
	}
	
	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
	}
	
	public boolean getAttacked() {
		// TODO Auto-generated method stub
		return attacked;
	}
}
