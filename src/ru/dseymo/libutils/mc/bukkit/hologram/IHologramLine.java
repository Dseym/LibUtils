package ru.dseymo.libutils.mc.bukkit.hologram;

import org.bukkit.Location;

public interface IHologramLine {
	
	Location getLocation();
	
	Object getStand();
	
	void setLocation(Location loc);
	
	void setText(String text);
	
	String getText();
	
}
