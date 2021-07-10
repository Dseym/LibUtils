package ru.dseymo.libutils.mc.bukkit.hologram;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import ru.dseymo.libutils.ReflUtil;
import ru.dseymo.libutils.mc.bukkit.ColorUtil;
import ru.dseymo.libutils.mc.bukkit.MCUtil;
import ru.dseymo.libutils.mc.bukkit.packet.ProtocolVer;

public class HologramLine implements IHologramLine {
	
	Object stand;
	Location loc;
	
	public HologramLine(String text, Location loc) {
		stand = MCUtil.createEntity(EntityType.ARMOR_STAND, "ARMOR_STAND");
		
		ReflUtil.invoke(stand, "setInvisible", new Class[] {boolean.class}, true);
		
		setText(text);
		
		if(ProtocolVer.v1_10.isThatOrNewest())
			ReflUtil.invoke(stand, "setNoGravity", new Class[] {boolean.class}, true);
		else
			ReflUtil.invoke(stand, "setGravity", new Class[] {boolean.class}, false);
		
		ReflUtil.invoke(stand, "setCustomNameVisible", new Class[] {boolean.class}, true);
		
		setLocation(loc);
	}

	@Override
	public Location getLocation() {
		return loc;
	}

	@Override
	public Object getStand() {
		return stand;
	}

	@Override
	public void setLocation(Location loc) {
		this.loc = loc;
		
		ReflUtil.invoke(stand, "setLocation", new Class[] {double.class, double.class, double.class, float.class, float.class}, loc.getX(), loc.getY(), loc.getZ(), 0, 0);
	}

	@Override
	public void setText(String text) {
		text = ColorUtil.color(text);
		
		if(ProtocolVer.v1_13.isThatOrNewest()) {
			ReflUtil.invoke(stand, "setCustomName",
							new Class[] {MCUtil.getNMSClass("IChatBaseComponent")},
							MCUtil.createChatComponentText(text));
		} else
			ReflUtil.invoke(stand, "setCustomName", new Class[] {String.class}, text);
	}
	
	@Override
	public String getText() {
		if(ProtocolVer.v1_13.isThatOrNewest()) {
			return (String)ReflUtil.invoke(ReflUtil.invoke(stand, "getCustomName"), "getText");
		} else
			return (String)ReflUtil.invoke(stand, "getCustomName");
	}
	
}
