package ru.dseymo.libutils.mc.bukkit.packet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.Entity;
import ru.dseymo.libutils.ReflUtil;
import ru.dseymo.libutils.mc.bukkit.MCUtil;

public class Packet1_17 {
	public static class PacketBed extends Packet {
		
		private Location bed;
		private Entity entity;
		private int entityId;
		
		PacketBed(Location bed, Entity entity, int entityId) {
			super(null);
			
			this.bed = bed;
			this.entity = entity;
			this.entityId = entityId;
		};
		
		@SuppressWarnings("deprecation")
		@Override
		public void send(Player player) {
			player.sendBlockChange(bed, Material.valueOf("BED_BLOCK"), (byte)0);
			
			ReflUtil.invoke(entity, "e", new Class[] {MCUtil.getNMSClass("BlockPosition")}, new BlockPosition(bed.getX(), bed.getY(), bed.getZ()));
			ReflUtil.invoke(entity, "setPose", new Class[] {MCUtil.getNMSClass("EntityPose")}, ReflUtil.getEnumValue(MCUtil.getNMSClass("EntityPose"), "c"));
			PacketFactory.entityMetadata(entityId, entityId).send(player);
			
			super.send(player);
		}
		
	}
	
	public static class PacketTitle extends Packet1_17 {
		
		private net.minecraft.network.protocol.Packet<?> times, title, subTitle;
		
		PacketTitle(net.minecraft.network.protocol.Packet<?> times, net.minecraft.network.protocol.Packet<?> title, net.minecraft.network.protocol.Packet<?> subTitle) {
			super(null);
			
			this.times = times;
			this.title = title;
			this.subTitle = subTitle;
		};
		
		@Override
		public void send(Player player) {
			new Packet1_17(times).send(player);
			new Packet1_17(title).send(player);
			new Packet1_17(subTitle).send(player);
		}
		
	}
	
	public static class PacketPlayerSpawn extends Packet1_17 {
		
		private Object entityPlayer;
		
		PacketPlayerSpawn(net.minecraft.network.protocol.Packet<?> packet, Object entityPlayer) {
			super(packet);
			
			this.entityPlayer = entityPlayer;
		};
		
		public Object getEntityPlayer() {
			return entityPlayer;
		}
		
	}
	
	public static class PacketVelocity extends Packet1_17 {
		
		private Location loc;
		private Vector vector;
		private int entityId;
		private BukkitRunnable run;
		
		PacketVelocity(Location loc, Vector vector, int entityId) {
			super(null);
			
			this.loc = loc.clone();
			this.vector = vector;
			this.entityId = entityId;
		};
		
		@Override
		public void send(Player player) {
			run = new BukkitRunnable() {
				
				@Override
				public void run() {
					if(isDone())
						cancel();
					
					loc.add(vector);
					vector.multiply(0.9);
					PacketFactory1_17.teleportEntity(loc, entityId).send(player);
				}
				
			};
			
			run.runTaskTimer(Bukkit.getPluginManager().getPlugins()[0], 1, 1);
		}
		
		public void setVector(Vector vector) {
			this.vector = vector;
		}
		
		public Vector getVector() {
			return vector;
		}
		
		public void stop() {
			run.cancel();
			vector = vector.zero();
		}
		
		public Location getEntityLoc() {
			return loc;
		}
		
		public boolean isDone() {
			return Math.abs(vector.getX()) < 0.05 && Math.abs(vector.getY()) < 0.05 && Math.abs(vector.getZ()) < 0.05;
		}
		
	}
	
	
	net.minecraft.network.protocol.Packet<?> packet;
	
	public Packet1_17(net.minecraft.network.protocol.Packet<?> packet) {
		this.packet = packet;
	};
	
	public void send(Player player) {
		try {
			((EntityPlayer)ReflUtil.invoke(player, "getHandle")).b.sendPacket((net.minecraft.network.protocol.Packet<?>)packet);
		} catch (Exception e) {e.printStackTrace();}
	}

	public void sendAll() {
		for(Player player: Bukkit.getOnlinePlayers())
			send(player);
	}
	
}
