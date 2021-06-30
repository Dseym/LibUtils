package ru.dseymo.libutils.example;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ru.dseymo.libutils.mc.bukkit.packet.ProtocolVer;

public enum PlaySound {

	FAIL("ITEM_BREAK", "ENTITY_ITEM_BREAK", 1F);
	
	private Sound sound;
	private float pitch;
	
	private PlaySound(String sound1_8, String sound1_9, float pitch) {
		this.sound = Sound.valueOf(ProtocolVer.v1_9.isThatOrNewest() ? sound1_9 : sound1_8);
		this.pitch=pitch;
	}
	
	public void play(Player p) {
		p.playSound(p.getLocation(), sound, 1, pitch);
	}
	
}