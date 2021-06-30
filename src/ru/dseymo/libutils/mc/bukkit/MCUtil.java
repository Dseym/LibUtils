package ru.dseymo.libutils.mc.bukkit;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mojang.authlib.GameProfile;

import ru.dseymo.libutils.ReflUtil;
import ru.dseymo.libutils.mc.bukkit.packet.ProtocolVer;

public class MCUtil {
	
	public static String getPacketVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}
	
	public static Class<?> getNMSClass(String name) {
		try {
			return Class.forName("net.minecraft.server." + getPacketVersion() + "." + name);
		} catch (ClassNotFoundException e) {e.printStackTrace(); return null;}
	}
	
	public static Class<?> getCBClass(String name) {
		try {
			return Class.forName("org.bukkit.craftbukkit." + getPacketVersion() + "." + name);
		} catch (ClassNotFoundException e) {e.printStackTrace(); return null;}
	}
	
	public static GameProfile copyProfile(Player p, boolean randomUUID) {
		GameProfile profile = new GameProfile(randomUUID ? UUID.randomUUID() : p.getUniqueId(), p.getName());
		profile.getProperties().putAll(((GameProfile)ReflUtil.invoke(p, "getProfile")).getProperties());
		
		return profile;
	}
	
	public static void setTitleChest(Chest chest, String name) {
		ReflUtil.invoke(ReflUtil.getValue(chest.getBlock().getState(), "chest"), "a", new Class[] {String.class}, name);
	}
	
	public static Object createEntity(EntityType type, String entityTypes) {
		return createEntity(type.getEntityClass().getSimpleName(), entityTypes);
	}
	
	public static Object createEntity(String nmsType, String entityTypes) {
		if(ProtocolVer.v1_13.isThatOrNewest())
			return ReflUtil.instance(MCUtil.getNMSClass("Entity" + nmsType),
									 new Class[] {getNMSClass("EntityTypes"), MCUtil.getNMSClass("World")},
									 ReflUtil.getStaticValue(getNMSClass("EntityTypes"), entityTypes), ReflUtil.invoke(Bukkit.getWorlds().get(0), "getHandle"));
		else
			return ReflUtil.instance(MCUtil.getNMSClass("Entity" + nmsType),
									 new Class[] {MCUtil.getNMSClass("World")},
									 ReflUtil.invoke(Bukkit.getWorlds().get(0), "getHandle"));
	}
	
	public static Object createChatComponentText(String text) {
		return ReflUtil.instance(getNMSClass("ChatComponentText"), new Class[] {String.class}, text);
	}
	
	public static int getEntityId(Object entity) {
		try {
			return (int)ReflUtil.invoke(entity, "getId");
		} catch (Exception e) {e.printStackTrace(); return -1;}
	}
	
	public static Object getNMSStack(ItemStack stack) {
		return ReflUtil.invokeStatic(getCBClass("inventory.CraftItemStack"), "asNMSCopy", new Class[] {ItemStack.class}, stack);
	}
	
	public static Object getNMSEntityStack(ItemStack stack) {
		Object entity = createEntity("Item", "ITEM");
		ReflUtil.invoke(entity, "setItemStack", new Class[] {getNMSClass("ItemStack")}, getNMSStack(stack));
		
		return entity;
	}
	
}
