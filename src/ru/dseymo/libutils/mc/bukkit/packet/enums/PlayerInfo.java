package ru.dseymo.libutils.mc.bukkit.packet.enums;

import lombok.Getter;
import ru.dseymo.libutils.ReflUtil;
import ru.dseymo.libutils.mc.bukkit.MCUtil;

public enum PlayerInfo {
	ADD_PLAYER("ADD_PLAYER"), REMOVE_PLAYER("REMOVE_PLAYER"), UPDATE_DISPLAY_NAME("UPDATE_DISPLAY_NAME"),
	UPDATE_GAME_MODE("UPDATE_GAME_MODE"), UPDATE_LATENCY("UPDATE_LATENCY");
	
	@Getter
	private Object infoAction;
	
	private PlayerInfo(String infoAction) {
		this.infoAction = ReflUtil.getEnumValue(MCUtil.getNMSClass("PacketPlayOutPlayerInfo").getClasses()[1], infoAction);
	}
}
