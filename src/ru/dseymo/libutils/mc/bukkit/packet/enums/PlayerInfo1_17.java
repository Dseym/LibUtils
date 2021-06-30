package ru.dseymo.libutils.mc.bukkit.packet.enums;

import lombok.Getter;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;

public enum PlayerInfo1_17 {
	ADD_PLAYER(0), REMOVE_PLAYER(1), UPDATE_DISPLAY_NAME(2), UPDATE_GAME_MODE(3), UPDATE_LATENCY(4);
	
	@Getter
	private Object infoAction;
	
	private PlayerInfo1_17(int order) {
		infoAction = PacketPlayOutPlayerInfo.EnumPlayerInfoAction.values()[order];
	}
}
