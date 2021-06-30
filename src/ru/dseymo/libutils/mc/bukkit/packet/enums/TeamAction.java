package ru.dseymo.libutils.mc.bukkit.packet.enums;

import lombok.Getter;

public enum TeamAction {
	CREATE(0), UPDATE(2), REMOVE(1);
	
	@Getter
	private int action;
	
	private TeamAction(int action) {
		this.action = action;
	}
}
