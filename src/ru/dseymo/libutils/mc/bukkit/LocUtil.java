package ru.dseymo.libutils.mc.bukkit;

import java.text.ParseException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class LocUtil {
	
	public static String toString(Location loc, boolean exactLoc, boolean rotate) {
		if(loc == null)
			return "";
		
		String str = loc.getWorld().getName() + " " +
					(exactLoc ? loc.getX() : loc.getBlockX()) + " " +
					(exactLoc ? loc.getY() : loc.getBlockY()) + " " +
					(exactLoc ? loc.getZ() : loc.getBlockZ());
		if(rotate)
			str += " " + loc.getYaw() + " " + loc.getPitch();
		
		return str;
	}
	
	public static Location parseLoc(String str) throws ParseException {
		if(str == null || str.isEmpty())
			throw new ParseException("Location: " + str + " - String empty", 0);
		
		String[] split = str.split(" ");
		World world = Bukkit.getWorld(split[0]);
		if(world == null)
			throw new ParseException("Location: " + str + " - World not found", 0);
		
		try {
			double x = Double.parseDouble(split[1]),
				   y = Double.parseDouble(split[2]),
				   z = Double.parseDouble(split[3]);
			
			Location loc;
			if (split.length > 5) {
				float yaw = Float.parseFloat(split[4]),
					  pitch = Float.parseFloat(split[5]);

				loc = new Location(world, x, y, z, yaw, pitch);
			} else loc = new Location(world, x, y, z);
			
			return loc;
		} catch (Exception e) {throw new ParseException("Location: " + str, 0);}
	}
	
	public static boolean isLocBeetwenPoints(Location point1, Location point2, Location loc) {
		if(point1 == null || point2 == null || loc == null || !loc.getWorld().equals(point1.getWorld()) || !loc.getWorld().equals(point2.getWorld()))
			return false;
		
		if((point1.getBlockX() <= loc.getBlockX() && point2.getBlockX() >= loc.getBlockX()) || (point1.getBlockX() >= loc.getBlockX() && point2.getBlockX() <= loc.getBlockX()))
			if((point1.getBlockY() <= loc.getBlockY() && point2.getBlockY() >= loc.getBlockY()) || (point1.getBlockY() >= loc.getBlockY() && point2.getBlockY() <= loc.getBlockY()))
				if((point1.getBlockZ() <= loc.getBlockZ() && point2.getBlockZ() >= loc.getBlockZ()) || (point1.getBlockZ() >= loc.getBlockZ() && point2.getBlockZ() <= loc.getBlockZ()))
					return true;
		
		return false;
	}
	
	public static ArrayList<Block> getBlocksBetweenPoints(Location point1, Location point2) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		if(point1 == null || point2 == null || !point1.getWorld().equals(point2.getWorld()))
			return blocks;
 
        int topBlockX = point1.getBlockX() < point2.getBlockX() ? point2.getBlockX() : point1.getBlockX();
        int bottomBlockX = point1.getBlockX() > point2.getBlockX() ? point2.getBlockX() : point1.getBlockX();
 
        int topBlockY = point1.getBlockY() < point2.getBlockY() ? point2.getBlockY() : point1.getBlockY();
        int bottomBlockY = point1.getBlockY() > point2.getBlockY() ? point2.getBlockY() : point1.getBlockY();
 
        int topBlockZ = point1.getBlockZ() < point2.getBlockZ() ? point2.getBlockZ() : point1.getBlockZ();
        int bottomBlockZ = point1.getBlockZ() > point2.getBlockZ() ? point2.getBlockZ() : point1.getBlockZ();
 
        for(int x = bottomBlockX; x <= topBlockX; x++)
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
                for(int y = bottomBlockY; y <= topBlockY; y++)
                    blocks.add(point1.getWorld().getBlockAt(x, y, z));

        return blocks;
    }
	
	public static ArrayList<Entity> getNearbyEntities(Location loc, double distance) {
		return new ArrayList<>(loc.getWorld().getNearbyEntities(loc, distance, distance, distance));
	}
	
}
