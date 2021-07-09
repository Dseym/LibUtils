package ru.dseymo.libutils.mc.bukkit;

import org.bukkit.entity.Player;

import ru.dseymo.libutils.mc.bukkit.packet.PacketFactory;
import ru.dseymo.libutils.mc.bukkit.packet.PacketFactory.TitleAction;
import ru.dseymo.libutils.mc.bukkit.packet.PacketFactory1_17;
import ru.dseymo.libutils.mc.bukkit.packet.ProtocolVer;

public class Title {
	
	public static void send(Player p, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
		if(ProtocolVer.v1_17.isThatOrNewest())
			PacketFactory1_17.title(title, subTitle, fadeIn, stay, fadeOut).send(p);
		else {
			PacketFactory.title(TitleAction.TITLE, ColorUtil.color(title), fadeIn, stay, fadeOut).send(p);
			PacketFactory.title(TitleAction.SUBTITLE, ColorUtil.color(subTitle), fadeIn, stay, fadeOut).send(p);
		}
	}
	
	public static void send(Player p, String title, int fadeIn, int stay, int fadeOut) {
		send(p, title, "", fadeIn, stay, fadeOut);
	}
	
	public static void send(Player p, String title) {
		send(p, title, 10, 0, 10);
	}
	
	public static void send(Player p, String title, String subTitle) {
		send(p, title, subTitle, 10, 0, 10);
	}
	
	public static void clear(Player p) {
		if(ProtocolVer.v1_17.isThatOrNewest())
			PacketFactory1_17.resetTitle().send(p);
		else
			PacketFactory.title(TitleAction.CLEAR, "", 0, 0, 0).send(p);
	}
	
	public static void sendActionBar(Player p, String text) {
		if(ProtocolVer.v1_17.isThatOrNewest())
			PacketFactory1_17.actionBar(text).send(p);
		else
			PacketFactory.actionBar(ColorUtil.color(text)).send(p);
	}
	
}
