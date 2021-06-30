package ru.dseymo.libutils.mc.bukkit.packet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import ru.dseymo.libutils.ReflUtil;
import ru.dseymo.libutils.mc.bukkit.MCUtil;

public class Packet {
	
	public static class PacketBed extends Packet {
		
		private Location bed;
		private int entityId1_14;
		private Object entity1_14, blockPos1_14;
		
		PacketBed(Object packet, Location bed, int entityId1_14, Object entity1_14, Object blockPos1_14) {
			super(packet);
			
			this.bed = bed;
			this.entity1_14 = entity1_14;
			this.entityId1_14 = entityId1_14;
			this.blockPos1_14 = blockPos1_14;
		};
		
		@SuppressWarnings("deprecation")
		@Override
		public void send(Player player) {
			player.sendBlockChange(bed, Material.valueOf("BED_BLOCK"), (byte)0);
			
			if(ProtocolVer.v1_14.isThatOrNewest()) {
				ReflUtil.invoke(entity1_14, "e", new Class[] {MCUtil.getNMSClass("BlockPosition")}, blockPos1_14);
				ReflUtil.invoke(entity1_14, "setPose", new Class[] {MCUtil.getNMSClass("EntityPose")}, ReflUtil.getEnumValue(MCUtil.getNMSClass("EntityPose"), "SLEEPING"));
				PacketFactory.entityMetadata(entity1_14, entityId1_14).send(player);
			} else
				super.send(player);
		}
		
	}
	
	public static class PacketPlayerSpawn extends Packet {
		
		private Object entityPlayer;
		
		PacketPlayerSpawn(Object packet, Object entityPlayer) {
			super(packet);
			
			this.entityPlayer = entityPlayer;
		};
		
		public Object getEntityPlayer() {
			return entityPlayer;
		}
		
	}
	
	public static class PacketVelocity extends Packet {
		
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
					PacketFactory.teleportEntity(loc, entityId).send(player);
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
	
	
	Object packet;
	
	public Packet(Object packet) {
		this.packet = packet;
	};
	
	public void send(Player player) {
		try {
			Object conn = ReflUtil.getValue(ReflUtil.invoke(player, "getHandle"), "playerConnection");
			ReflUtil.invoke(conn, "sendPacket", new Class[] {MCUtil.getNMSClass("Packet")}, packet);
		} catch (Exception e) {e.printStackTrace();}
	}

	public void sendAll() {
		for(Player player: Bukkit.getOnlinePlayers())
			send(player);
	}
	
}
