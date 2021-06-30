package ru.dseymo.libutils.mc.bukkit.packet;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;

import ru.dseymo.libutils.ReflUtil;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.debug.LogMessage.LogType;
import ru.dseymo.libutils.mc.bukkit.ColorUtil;
import ru.dseymo.libutils.mc.bukkit.MCUtil;
import ru.dseymo.libutils.mc.bukkit.packet.Packet.PacketBed;
import ru.dseymo.libutils.mc.bukkit.packet.Packet.PacketPlayerSpawn;
import ru.dseymo.libutils.mc.bukkit.packet.Packet.PacketVelocity;
import ru.dseymo.libutils.mc.bukkit.packet.enums.PlayerInfo;
import ru.dseymo.libutils.mc.bukkit.packet.enums.TeamAction;

public class PacketFactory {
	public static enum TitleAction {
		
		CLEAR("CLEAR"), RESET("RESET"), TITLE("TITLE"), SUBTITLE("SUBTITLE"), TIMES("TIMES");
		
		private Object action;
		
		private TitleAction(String action) {
			this.action = ReflUtil.getEnumValue(MCUtil.getNMSClass("PacketPlayOutTitle").getClasses()[0], action);
		}
		
	}
	
	
	private static Packet createPacket(String clazzPacket, Class<?>[] clazzArgs, Object...args) {
		if(clazzArgs != null)
			return new Packet(ReflUtil.instance(MCUtil.getNMSClass(clazzPacket), clazzArgs, args));
		else
			return new Packet(ReflUtil.instance(MCUtil.getNMSClass(clazzPacket)));
	}
	
	public static PacketPlayerSpawn spawnPlayer(GameProfile profile, int entityId) {
		Object player = ReflUtil.instance(MCUtil.getNMSClass("EntityPlayer"),
										  new Class[] {MCUtil.getNMSClass("MinecraftServer"), MCUtil.getNMSClass("WorldServer"), GameProfile.class, MCUtil.getNMSClass("PlayerInteractManager")},
										  ReflUtil.invoke(Bukkit.getServer(), "getServer"), ReflUtil.invoke(Bukkit.getWorlds().get(0), "getHandle"), profile,
										  ReflUtil.instance(MCUtil.getNMSClass("PlayerInteractManager"),
												  			new Class[] {MCUtil.getNMSClass(ProtocolVer.v1_14.isThatOrNewest() ? "WorldServer" : "World")},
												  			ReflUtil.invoke(Bukkit.getWorlds().get(0), "getHandle")));
		
		PacketPlayerSpawn packet = new PacketPlayerSpawn(ReflUtil.instance(MCUtil.getNMSClass("PacketPlayOutNamedEntitySpawn"),
																		   new Class[] {MCUtil.getNMSClass("EntityHuman")},
																		   player), player);
		ReflUtil.setValue(packet.packet, "a", entityId);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:SPAWN_PLAYER: " + profile.getName() + " " + entityId), true);
		
		return packet;
	}
	
	@SuppressWarnings("unchecked")
	public static Packet playerInfo(GameProfile profile, PlayerInfo playerInfo) {
		Packet packet = createPacket("PacketPlayOutPlayerInfo", null);
		Object data;
		if(ProtocolVer.v1_10.isThatOrNewest())
			data = ReflUtil.instance(MCUtil.getNMSClass("PacketPlayOutPlayerInfo").getClasses()[0],
									 new Class[] {MCUtil.getNMSClass("PacketPlayOutPlayerInfo"), GameProfile.class, int.class, MCUtil.getNMSClass("EnumGamemode"), MCUtil.getNMSClass("IChatBaseComponent")},
									 packet.packet, profile, 1, ReflUtil.getEnumValue(MCUtil.getNMSClass("EnumGamemode"), "CREATIVE"), MCUtil.createChatComponentText(profile.getName()));
		else
			data = ReflUtil.instance(MCUtil.getNMSClass("PacketPlayOutPlayerInfo").getClasses()[0],
								   	 new Class[] {MCUtil.getNMSClass("PacketPlayOutPlayerInfo"), GameProfile.class, int.class, MCUtil.getNMSClass("WorldSettings").getClasses()[0], MCUtil.getNMSClass("IChatBaseComponent")},
									 packet.packet, profile, 1, ReflUtil.getEnumValue(MCUtil.getNMSClass("WorldSettings").getClasses()[0], "CREATIVE"), MCUtil.createChatComponentText(profile.getName()));
		List<Object> datas = (List<Object>)ReflUtil.getValue(packet.packet, "b");
		datas.add(data);
		
		ReflUtil.setValue(packet.packet, "a", playerInfo.getInfoAction());
		ReflUtil.setValue(packet.packet, "b", datas);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:PLAYER_INFO: " + profile.getName() + " " + playerInfo.name()), true);
		
		return packet;
	}
	
	public static Packet spawnEntityLiving(Object entityLiving, int entityId) {
		Packet packet = createPacket("PacketPlayOutSpawnEntityLiving", new Class[] {MCUtil.getNMSClass("EntityLiving")}, entityLiving);
		ReflUtil.setValue(packet.packet, "a", entityId);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:SPAWN_ENTITY_LIVING: " + entityLiving.toString() + " " + entityId), true);
		
		return packet;
	}
	
	public static Packet spawnEntityLiving(Object entityLiving) {
		return spawnEntityLiving(entityLiving, MCUtil.getEntityId(entityLiving));
	}
	
	public static Packet spawnEntity(Object entity, int protocolId, int protocolData, int entityId) {
		Packet packet;
		if(ProtocolVer.v1_14.isThatOrNewest())
			packet = createPacket("PacketPlayOutSpawnEntity",
								  new Class[] {MCUtil.getNMSClass("Entity"), int.class},
								  entity, protocolData);
		else
			packet = createPacket("PacketPlayOutSpawnEntity",
								  new Class[] {MCUtil.getNMSClass("Entity"), int.class, int.class},
								  entity, protocolId, protocolData);
		ReflUtil.setValue(packet.packet, "a", entityId);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:SPAWN_ENTITY: " + entity.toString() + " " + protocolId + " " + protocolData + " " + entityId), true);
		
		return packet;
	}
	
	public static Packet entityMetadata(Object entity, int entityId) {
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:ENTITY_METADATA: " + entity.toString() + " " + entityId), true);
		
		return createPacket("PacketPlayOutEntityMetadata",
							new Class[] {int.class, MCUtil.getNMSClass("DataWatcher"), boolean.class},
							entityId, ReflUtil.invoke(entity, "getDataWatcher"), true);
	}
	
	public static Packet destroyEntities(int...entityIds) {
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:DESTROY_ENTITIES: " + Arrays.toString(entityIds)), true);
		
		return createPacket("PacketPlayOutEntityDestroy", new Class[] {int[].class}, entityIds);
	}
	
	public static Packet teleportEntity(Location loc, int entityId) {
		Object entity = MCUtil.createEntity(EntityType.EGG, "EGG");
		ReflUtil.invoke(entity, "setLocation",
					    new Class[] {double.class, double.class, double.class, float.class, float.class},
					    loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		Packet packet = createPacket("PacketPlayOutEntityTeleport", new Class[] {MCUtil.getNMSClass("Entity")}, entity);
		ReflUtil.setValue(packet.packet, "a", entityId);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:TELEPORT_ENTITY: " + loc.toString() + " " + entityId), true);
		
		return packet;
	}
	
	public static PacketVelocity velocityEntity(Vector vector, Location startLoc, int entityId) {
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:VELOCITY_ENTITY: " + vector.toString() + " " + startLoc.toString() + " " + entityId), true);
		
		return new PacketVelocity(startLoc, vector, entityId);
	}
	
	public static Packet title(TitleAction action, String text, int fadeIn, int stay, int fadeOut) {
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:TITLE: " + action.name() + " " + text + " " + fadeIn + " " + stay + " " + fadeOut + " "), true);
		
		return createPacket("PacketPlayOutTitle",
							new Class[] {MCUtil.getNMSClass("PacketPlayOutTitle").getClasses()[0], MCUtil.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class},
							action.action, MCUtil.createChatComponentText(text), fadeIn, stay, fadeOut);
	}
	
	public static Packet actionBar(String text) {
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:ACTION_BAR: " + text), true);
		
		return createPacket("PacketPlayOutChat",
							new Class[] {MCUtil.getNMSClass("IChatBaseComponent"), byte.class},
							MCUtil.createChatComponentText(text), (byte)2);
	}
	
	public static PacketBed animationBed(Location loc, Object entity1_14, int entityId) {
		Location bed = loc.clone();
		bed.setY(1);
		Object blockPos = ReflUtil.instance(MCUtil.getNMSClass("BlockPosition"),
						  new Class[] {double.class, double.class, double.class},
						  bed.getX(), bed.getY(), bed.getZ());
		PacketBed packet;
		if(ProtocolVer.v1_14.isThatOrNewest())
			packet = new PacketBed(null, bed, entityId, entity1_14, blockPos);
		else {
			packet = new PacketBed(ReflUtil.instance(MCUtil.getNMSClass("PacketPlayOutBed")), bed, 0, null, null);
			
			ReflUtil.setValue(packet.packet, "a", entityId);
			ReflUtil.setValue(packet.packet, "b", blockPos);
		}
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:ANIMATION_BED: " + loc.toString() + " " + entity1_14 + " " + entityId), true);
		
		return packet;
	}
	
	public static Packet headerFooterTablist(String header, String footer) {
		Packet packet = createPacket("PacketPlayOutPlayerListHeaderFooter", null);
		
		ReflUtil.setValue(packet.packet, packet.packet.getClass().getDeclaredFields()[0].getName(), MCUtil.createChatComponentText(ColorUtil.color(header)));
		ReflUtil.setValue(packet.packet, packet.packet.getClass().getDeclaredFields()[1].getName(), MCUtil.createChatComponentText(ColorUtil.color(footer)));
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:HEADER_FOOTER_TABLIST: " + header + " " + footer), true);
		
		return packet;
	}
	
	@SuppressWarnings("unchecked")
	public static Packet scoreboardTeam(Team team, TeamAction action, List<String> entities) {
		Object _team = ReflUtil.instance(MCUtil.getNMSClass("ScoreboardTeam"),
										 new Class[] {MCUtil.getNMSClass("Scoreboard"), String.class},
										 null, team.getName());
		Collection<String> _entities = (Collection<String>)ReflUtil.invoke(_team, "getPlayerNameSet");
		_entities.addAll(entities);
		
		ReflUtil.setValue(_team, "g", team.allowFriendlyFire());
		ReflUtil.setValue(_team, "h", team.canSeeFriendlyInvisibles());
		ReflUtil.setValue(_team, "i", tagVisibilityToNMS(team.getNameTagVisibility()));
		
		if(ProtocolVer.v1_13.isThatOrNewest()) {
			ReflUtil.setValue(_team, "d", MCUtil.createChatComponentText(team.getDisplayName()));
			ReflUtil.setValue(_team, "e", MCUtil.createChatComponentText(team.getPrefix()));
			ReflUtil.setValue(_team, "f", MCUtil.createChatComponentText(team.getSuffix()));
		} else {
			ReflUtil.setValue(_team, "d", team.getDisplayName());
			ReflUtil.setValue(_team, "e", team.getPrefix());
			ReflUtil.setValue(_team, "f", team.getSuffix());
		}
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:SCOREBOARD_TEAM: " + team.toString() + " " + action.name() + " " + entities.toString()), true);
		
		return createPacket("PacketPlayOutScoreboardTeam",
							new Class[] {MCUtil.getNMSClass("ScoreboardTeam"), int.class},
							_team, action.getAction());
	}
	
	@SuppressWarnings("unchecked")
	public static Packet scoreboardTeam(String name, TeamAction action, List<String> entities) {
		if(name.length() > 16)
			name = name.substring(0, 15);
		
		Object team = ReflUtil.instance(MCUtil.getNMSClass("ScoreboardTeam"),
										new Class[] {MCUtil.getNMSClass("Scoreboard"), String.class},
										null, name);
		Collection<String> _entities = (Collection<String>)ReflUtil.invoke(team, "getPlayerNameSet");
		_entities.addAll(entities);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:SCOREBOARD_TEAM: " + name + " " + action.name() + " " + entities.toString()), true);
		
		return createPacket("PacketPlayOutScoreboardTeam",
							new Class[] {MCUtil.getNMSClass("ScoreboardTeam"), int.class},
							team, action.getAction());
	}
	
	private static Object tagVisibilityToNMS(NameTagVisibility tag) {
		Class<?> enumClazz = MCUtil.getNMSClass("ScoreboardTeamBase").getClasses()[0];
		
		switch(tag) {
			case ALWAYS: return ReflUtil.getEnumValue(enumClazz, "ALWAYS");
			case HIDE_FOR_OTHER_TEAMS: return ReflUtil.getEnumValue(enumClazz, "HIDE_FOR_OTHER_TEAMS");
			case HIDE_FOR_OWN_TEAM: return ReflUtil.getEnumValue(enumClazz, "HIDE_FOR_OWN_TEAM");
			case NEVER: return ReflUtil.getEnumValue(enumClazz, "NEVER");
			
			default: return null;
		}
	}
	
}
