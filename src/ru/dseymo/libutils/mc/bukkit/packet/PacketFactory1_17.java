package ru.dseymo.libutils.mc.bukkit.packet;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.projectile.EntityEgg;
import net.minecraft.world.level.World;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import ru.dseymo.libutils.ReflUtil;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.debug.LogMessage.LogType;
import ru.dseymo.libutils.mc.bukkit.ColorUtil;
import ru.dseymo.libutils.mc.bukkit.packet.Packet1_17.PacketBed;
import ru.dseymo.libutils.mc.bukkit.packet.Packet1_17.PacketPlayerSpawn;
import ru.dseymo.libutils.mc.bukkit.packet.Packet1_17.PacketTitle;
import ru.dseymo.libutils.mc.bukkit.packet.Packet1_17.PacketVelocity;
import ru.dseymo.libutils.mc.bukkit.packet.enums.PlayerInfo1_17;
import ru.dseymo.libutils.mc.bukkit.packet.enums.TeamAction;

public class PacketFactory1_17 {
	
	public static PacketPlayerSpawn spawnPlayer(GameProfile profile, int entityId) {
		EntityPlayer entPl = new EntityPlayer((MinecraftServer)ReflUtil.invoke(Bukkit.getServer(), "getServer"), (WorldServer)ReflUtil.invoke(Bukkit.getWorlds().get(0), "getHandle"), profile);
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(entPl);
		
		ReflUtil.setValue(packet, "a", entityId);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:SPAWN_PLAYER: " + profile.getName() + " " + entityId), true);
		
		return new PacketPlayerSpawn(packet, entPl);
	}
	
	public static Packet1_17 playerInfo(GameProfile profile, PlayerInfo1_17 playerInfo) {
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:PLAYER_INFO: " + profile.getName() + " " + playerInfo.name()), true);
		
		return new Packet1_17(new PacketPlayOutPlayerInfo((PacketPlayOutPlayerInfo.EnumPlayerInfoAction)playerInfo.getInfoAction(), new EntityPlayer((MinecraftServer)ReflUtil.invoke(Bukkit.getServer(), "getServer"), (WorldServer)ReflUtil.invoke(Bukkit.getWorlds().get(0), "getHandle"), profile)));
	}
	
	public static Packet1_17 spawnEntityLiving(EntityLiving entityLiving, int entityId) {
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entityLiving);
		
		ReflUtil.setValue(packet, "a", entityId);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:SPAWN_ENTITY_LIVING: " + entityLiving.toString() + " " + entityId), true);
		
		return new Packet1_17(packet);
	}
	
	public static Packet1_17 spawnEntityLiving(EntityLiving entityLiving) {
		return spawnEntityLiving(entityLiving, entityLiving.getId());
	}
	
	public static Packet1_17 spawnEntity(Entity entity, int protocolId, int protocolData, int entityId) {
		PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(entity, protocolData);
		
		ReflUtil.setValue(packet, "a", entityId);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:SPAWN_ENTITY: " + entity.toString() + " " + protocolData + " " + entityId), true);
		
		return new Packet1_17(packet);
	}
	
	public static Packet1_17 entityMetadata(Entity entity, int entityId) {
		PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entityId, entity.getDataWatcher(), true);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:ENTITY_METADATA: " + entity.toString() + " " + entityId), true);
		
		return new Packet1_17(packet);
	}
	
	public static Packet1_17 destroyEntities(int entityId) {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityId);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:DESTROY_ENTITY: " + entityId), true);
		
		return new Packet1_17(packet);
	}
	
	public static Packet1_17 teleportEntity(Location loc, int entityId) {
		EntityEgg egg = new EntityEgg(EntityTypes.aM, (World)ReflUtil.invoke(Bukkit.getWorlds().get(0), "getHandle"));
		
		egg.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		
		PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(egg);
		
		ReflUtil.setValue(packet, "a", entityId);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:TELEPORT_ENTITY: " + loc.toString() + " " + entityId), true);
		
		return new Packet1_17(packet);
	}
	
	public static PacketVelocity velocityEntity(Vector vector, Location startLoc, int entityId) {
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:VELOCITY_ENTITY: " + vector.toString() + " " + startLoc.toString() + " " + entityId), true);
		
		return new PacketVelocity(startLoc, vector, entityId);
	}
	
	public static PacketTitle title(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:TITLE: " + " " + title + " " + subTitle + " " + fadeIn + " " + stay + " " + fadeOut + " "), true);
		
		return new PacketTitle(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut),
							   new ClientboundSetTitleTextPacket(new ChatComponentText(ColorUtil.color(title))),
							   new ClientboundSetSubtitleTextPacket(new ChatComponentText(ColorUtil.color(subTitle))));
	}
	
	public static Packet1_17 resetTitle() {
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:RESET_TITLE"), true);
		
		return new Packet1_17(new ClientboundClearTitlesPacket(true));
	}
	
	public static Packet1_17 actionBar(String text) {
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:ACTION_BAR: " + text), true);
		
		return new Packet1_17(new ClientboundSetActionBarTextPacket(new ChatComponentText(ColorUtil.color(text))));
	}
	
	public static PacketBed animationBed(Location loc, Entity entity, int entityId) {
		Location bed = loc.clone();
		PacketBed packet = new PacketBed(bed, entity, entityId);
		
		bed.setY(1);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:ANIMATION_BED: " + loc.toString() + " " + entity + " " + entityId), true);
		
		return packet;
	}
	
	public static Packet1_17 headerFooterTablist(String header, String footer) {
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:HEADER_FOOTER_TABLIST: " + header + " " + footer), true);
			
		return new Packet1_17(new PacketPlayOutPlayerListHeaderFooter(new ChatComponentText(ColorUtil.color(header)), new ChatComponentText(ColorUtil.color(footer))));
	}
	
	public static Packet1_17 scoreboardTeam(Team team, TeamAction action, List<String> entities) {
		ScoreboardTeam _team = new ScoreboardTeam(null, team.getName());
		
		ReflUtil.setValue(_team, "g", team.allowFriendlyFire());
		ReflUtil.setValue(_team, "h", team.canSeeFriendlyInvisibles());
		ReflUtil.setValue(_team, "i", tagVisibilityToNM(team.getNameTagVisibility()));
		ReflUtil.setValue(_team, "d", new ChatComponentText(ColorUtil.color(team.getDisplayName())));
		ReflUtil.setValue(_team, "e", new ChatComponentText(ColorUtil.color(team.getPrefix())));
		ReflUtil.setValue(_team, "f", new ChatComponentText(ColorUtil.color(team.getSuffix())));
		
		_team.getPlayerNameSet().addAll(entities);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:SCOREBOARD_TEAM: " + team.toString() + " " + action.name() + " " + entities.toString()), true);
		
		if(action == TeamAction.REMOVE)
			return new Packet1_17(PacketPlayOutScoreboardTeam.a(_team));
		else if(action == TeamAction.CREATE)
			return new Packet1_17(PacketPlayOutScoreboardTeam.a(_team, false));
		else
			return new Packet1_17(PacketPlayOutScoreboardTeam.a(_team, true));
	}
	
	public static Packet1_17 scoreboardTeam(String name, TeamAction action, List<String> entities) {
		if(name.length() > 16)
			name = name.substring(0, 15);
		
		ScoreboardTeam _team = new net.minecraft.world.scores.ScoreboardTeam(null, name);
		
		_team.getPlayerNameSet().addAll(entities);
		
		Debug.message(LogMessage.create("MC-PACKET", LogType.INFO, "Packet:1_17+:SCOREBOARD_TEAM: " + name + " " + action.name() + " " + entities.toString()), true);
		
		if(action == TeamAction.REMOVE)
			return new Packet1_17(PacketPlayOutScoreboardTeam.a(_team));
		else if(action == TeamAction.CREATE)
			return new Packet1_17(PacketPlayOutScoreboardTeam.a(_team, false));
		else
			return new Packet1_17(PacketPlayOutScoreboardTeam.a(_team, true));
	}
	
	private static Object tagVisibilityToNM(NameTagVisibility tag) {
		switch(tag) {
			case ALWAYS: return ScoreboardTeamBase.EnumNameTagVisibility.a;
			case HIDE_FOR_OTHER_TEAMS: return ScoreboardTeamBase.EnumNameTagVisibility.c;
			case HIDE_FOR_OWN_TEAM: return ScoreboardTeamBase.EnumNameTagVisibility.d;
			case NEVER: return ScoreboardTeamBase.EnumNameTagVisibility.b;
			
			default: return null;
		}
	}
	
}
