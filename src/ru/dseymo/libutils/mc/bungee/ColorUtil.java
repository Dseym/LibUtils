package ru.dseymo.libutils.mc.bungee;

import net.md_5.bungee.api.ChatColor;

public class ColorUtil {
	public static char ALTER_COLOR = '&';
	
	public static String color(String str) {
		return ChatColor.translateAlternateColorCodes(ALTER_COLOR, str);
	}
	
	public static String clear(String str) {
		return ChatColor.stripColor(str);
	}
	
}
