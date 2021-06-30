package ru.dseymo.libutils.mc.bukkit.packet;

import org.bukkit.Bukkit;

import lombok.AllArgsConstructor;
import ru.dseymo.libutils.ReflUtil;

@AllArgsConstructor
public enum ProtocolVer {
	older(46), v1_8(47), v1_9(107), v1_10(210), v1_11(315), v1_12(335), v1_13(393), v1_14(477), v1_15(573),
	v1_16(735), v1_17(755);
	
	public static int getProtocolVer() {
		Object server = ReflUtil.invoke(Bukkit.getServer(), "getServer");
		
		if(ReflUtil.isMethod(server, "getServerPing"))
			return (int)ReflUtil.invoke(ReflUtil.invoke(ReflUtil.invoke(server, "getServerPing"), "getServerData"), "getProtocolVersion");
		else
			return (int)ReflUtil.invoke(ReflUtil.invoke(ReflUtil.invoke(server, "aG"), "c"), "b");
	}
	
	
	private int version;
	
	public boolean isThatOrNewest() {
		return version <= getProtocolVer();
	}
	
	public boolean isNewest() {
		return version < getProtocolVer();
	}
	
	public boolean isThatOrOldest() {
		return version >= getProtocolVer();
	}
	
	public boolean isOldest() {
		return version > getProtocolVer();
	}
	
}
