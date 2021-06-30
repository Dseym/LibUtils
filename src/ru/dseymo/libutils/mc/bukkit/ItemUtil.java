package ru.dseymo.libutils.mc.bukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import ru.dseymo.libutils.ReflUtil;
import ru.dseymo.libutils.mc.bukkit.packet.ProtocolVer;

public class ItemUtil {
	
	public static ItemStack takeItem(ItemStack item) {
		if(item.getAmount() == 1)
            return new ItemStack(Material.AIR);
        else {
        	ItemStack newI = item.clone();
            newI.setAmount(newI.getAmount() - 1);
            return newI;
        }
	}
	
	public static ItemStack setLore(ItemStack item, String... lines) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<>();
		for (String line: lines)
			lore.add(ColorUtil.color(line));
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack insertLoreLines(ItemStack item, int afterLines, String... lines) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<>();
		List<String> oldLore = meta.getLore();
		
		int currentLine = 0;
		while (currentLine < afterLines) {
			lore.add(oldLore.get(currentLine));
			currentLine++;
		}

		for (String line: lines)
			lore.add(ColorUtil.color(line));
		
		while (currentLine < oldLore.size()) {
			lore.add(oldLore.get(currentLine));
			currentLine++;
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack unbreakable(ItemStack item, boolean enable) {
		ItemMeta meta = item.getItemMeta();
		Object obj = meta;
		
		if(ProtocolVer.v1_15.isOldest())
			obj = ReflUtil.invoke(obj, "spigot");
		
		ReflUtil.invoke(obj, "setUnbreakable", new Class[] {boolean.class}, true);
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static String getTag(ItemStack item, String tag) {
		try {
			Object nms = MCUtil.getNMSStack(item);
			Object nbt = (boolean)ReflUtil.invoke(nms, "hasTag") ? ReflUtil.invoke(nms, "getTag") : MCUtil.getNMSClass("NBTTagCompound").newInstance();
			
			return (String)ReflUtil.invoke(nbt, "getString", new Class[] {String.class}, tag);
		} catch (Exception e) {e.printStackTrace(); return null;}
	}
	
	public static ItemStack setTag(ItemStack item, String tag, String value) {
		try {
			Object nms = MCUtil.getNMSStack(item);
			Object nbt = (boolean)ReflUtil.invoke(nms, "hasTag") ? ReflUtil.invoke(nms, "getTag") : MCUtil.getNMSClass("NBTTagCompound").newInstance();
			
			ReflUtil.invoke(nbt, "set", new Class[] {String.class, MCUtil.getNMSClass("NBTBase")}, tag, ReflUtil.instance(MCUtil.getNMSClass("NBTTagString"), new Class[] {String.class}, value));
			ReflUtil.invoke(nms, "setTag", new Class[] {MCUtil.getNMSClass("NBTTagCompound")}, nbt);
			
			ItemStack itemAfter = (ItemStack)ReflUtil.invokeStatic(MCUtil.getCBClass("inventory.CraftItemStack"), "asBukkitCopy", new Class[] {MCUtil.getNMSClass("ItemStack")}, nms);
			item.setItemMeta(itemAfter.getItemMeta());
		} catch (Exception e) {e.printStackTrace();}
		
		return item;
	}
	
	public static ItemStack addToLore(ItemStack item, String... newLines) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
		for (String line: newLines)
			lore.add(ColorUtil.color(line));
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack setName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ColorUtil.color(name));
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack changeLore(ItemStack item, int line, String newText) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(line, ColorUtil.color(newText));
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack addGlow(ItemStack item) {
		item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		
		return item;
    }
	
	public static ItemStack removeGlow(ItemStack item) {
		item.removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
		ItemMeta meta = item.getItemMeta();
		meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		
		return item;
    }
	
	public static ItemStack generateItem(Material type, byte data, int amount, String name, String... lines) {
		ItemStack item = new ItemStack(type, amount, data);
		setName(item, name);
		setLore(item, lines);
		
		return item;
	}
	
	public static ItemStack generateItem(Material type, byte data, String name, String... lines) {
		return generateItem(type, data, 1, name, lines);
	}
	
	public static ItemStack generateItem(Material type, int amount, String name, String... lines) {
		return generateItem(type, (byte)0, amount, name, lines);
	}
	
	public static ItemStack generateItem(Material type, String name, String... lines) {
		return generateItem(type, (byte)0, 1, name, lines);
	}
	
	public static ItemStack generateSkull(String playerName, String displayName, String... lines) {
		ItemStack item;
		
		if(ProtocolVer.v1_13.isThatOrNewest())
			item = generateItem(Material.valueOf("PLAYER_HEAD"), displayName, lines);
		else
			item = generateItem(Material.valueOf("SKULL_ITEM"), (byte)3, displayName, lines);
		
		SkullMeta meta = (SkullMeta)item.getItemMeta();
		
		meta.setOwner(playerName);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static String getFriendlyName(ItemStack item) {
		return ChatColor.stripColor(getName(item));
	}
	
	public static String getName(ItemStack item) {
		if (item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasDisplayName())
				return meta.getDisplayName();
		}
		
		return "";
	}
	
}
