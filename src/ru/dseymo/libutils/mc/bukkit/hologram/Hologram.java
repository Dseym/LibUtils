package ru.dseymo.libutils.mc.bukkit.hologram;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Setter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import ru.dseymo.libutils.mc.bukkit.MCUtil;
import ru.dseymo.libutils.mc.bukkit.packet.PacketFactory;
import ru.dseymo.libutils.mc.bukkit.packet.PacketFactory1_17;
import ru.dseymo.libutils.mc.bukkit.packet.ProtocolVer;

public class Hologram implements Listener {
	public static int INDENT = 23;
	private final static ArrayList<Hologram> HOLOGRAMS = new ArrayList<>();
	private static BukkitTask TASK = null;
	
	public static void removeAll() {
		for(Hologram holo: new ArrayList<>(HOLOGRAMS))
			holo.remove();
	}
	
	
	ArrayList<UUID> uuids = new ArrayList<>();
	ArrayList<IHologramLine> lines = new ArrayList<>();
	Location loc;
	@Setter
	private boolean reloadForAll;
	
	public Hologram(Plugin plugin, Location loc) {
		this.loc = loc.clone();
		if(TASK == null)
			TASK = new BukkitRunnable() {
				
				@Override
				public void run() {
					ArrayList<UUID> uuids = new ArrayList<>();
					
					for(Player p: Bukkit.getOnlinePlayers())
						uuids.add(p.getUniqueId());
					
					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
						for(Hologram holo: HOLOGRAMS) {
							if(holo.reloadForAll)
								holo.uuids = new ArrayList<>(uuids);
							
							holo.update();
						}
					});
				}
				
			}.runTaskTimer(plugin, 100, 20);
		
		HOLOGRAMS.add(this);
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public void remove() {
		PlayerQuitEvent.getHandlerList().unregister(this);
		
		for(UUID uuid: new ArrayList<>(uuids))
			removePlayer(Bukkit.getPlayer(uuid));
		for(int i = 0; i < lines.size(); i++)
			removeLine(i);
		
		HOLOGRAMS.remove(this);
		
		if(HOLOGRAMS.size() == 0 && TASK != null) {
			TASK.cancel();
			TASK = null;
		}
	}
	
	
	public void setLocation(Location loc) {
		this.loc = loc;
		int i = 0;
		
		for(IHologramLine line: lines) {
			IHologramLine lastLine = null;
			
			if(i != 0)
				lastLine = lines.get(i-1);
			
			i++;
			
			line.setLocation(lastLine != null ? lastLine.getLocation().add(0, -((double)INDENT/100.0), 0) : loc);
		}
	}
	
	public void setText(int index, String text) {
		if(index+1 <= lines.size())
			lines.get(index).setText(text);
	}
	
	public String getText(int index) {
		if(index+1 <= lines.size())
			return lines.get(index).getText();
		else
			return null;
	}
	
	public int getSize() {
		return lines.size();
	}
	
	public void addLines(String...texts) {
		for(String line: texts) {
			IHologramLine lastLine = null;
			
			if(lines.size() != 0)
				lastLine = lines.get(lines.size()-1);
			
			lines.add(new HologramLine(line, lastLine != null ? lastLine.getLocation().add(0, -((double)INDENT/100.0), 0) : loc));
		}
	}
	
	public void removeLine(int index) {
		if(index+1 > lines.size())
			return;
		
		if(ProtocolVer.v1_17.isThatOrNewest())
			PacketFactory1_17.destroyEntities(index);
		else
			PacketFactory.destroyEntities(MCUtil.getEntityId(lines.get(index).getStand())).sendAll();
		
		lines.remove(index);
	}
	
	public void addPlayer(Player p) {
		if(contains(p)) return;
		uuids.add(p.getUniqueId());
	}
	
	public void removePlayer(Player p) {
		if(!contains(p)) return;
		uuids.remove(p.getUniqueId());
		for(IHologramLine line: lines)
			PacketFactory.destroyEntities(MCUtil.getEntityId(line.getStand())).send(p);
	}
	
	public boolean contains(Player p) {
		return uuids.contains(p.getUniqueId());
	}
	
	public void update() {
		for(IHologramLine line: lines)
			for(UUID uuid: uuids) {
				if(ProtocolVer.v1_17.isThatOrNewest()) {
					PacketFactory1_17.spawnEntityLiving((EntityLiving)line.getStand()).send(Bukkit.getPlayer(uuid));
					PacketFactory1_17.entityMetadata((Entity)line.getStand(), MCUtil.getEntityId(line.getStand())).send(Bukkit.getPlayer(uuid));
				} else {
					PacketFactory.spawnEntityLiving(line.getStand()).send(Bukkit.getPlayer(uuid));
					PacketFactory.entityMetadata(line.getStand(), MCUtil.getEntityId(line.getStand())).send(Bukkit.getPlayer(uuid));
				}
			}
	}
	
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {removePlayer(e.getPlayer());}
	
}
