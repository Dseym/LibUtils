package ru.dseymo.libutils.mc.bukkit.packet.enums;

import lombok.Getter;
import ru.dseymo.libutils.ReflUtil;
import ru.dseymo.libutils.mc.bukkit.MCUtil;

public enum TitleAction {
	CLEAR("CLEAR"), RESET("RESET"), TITLE("TITLE"), SUBTITLE("SUBTITLE"), TIMES("TIMES");
	
	@Getter
	private Object action;
	
	private TitleAction(String action) {
		
		
		this.action = ReflUtil.getEnumValue(MCUtil.getNMSClass("PacketPlayOutTitle").getClasses()[0], action);
	}
}
