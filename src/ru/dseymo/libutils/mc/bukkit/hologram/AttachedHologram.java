package ru.dseymo.libutils.mc.bukkit.hologram;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import ru.dseymo.libutils.mc.bukkit.LocUtil;

public class AttachedHologram implements Listener {
	@Getter
	private Hologram holo;
	private UUID player;
	@Getter @Setter
	private int x = -40, y = 0;
	@Getter @Setter
	private float distanceFromPlayer = 0; // 0 - auto adapt
	@Getter @Setter
	private boolean onHeightPlayer = true;
	
	public AttachedHologram(Plugin plugin) {
		holo = new Hologram(plugin, LocUtil.ZERO);
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public void remove() {
		clearPlayer();
		
		holo.remove();
		
		PlayerMoveEvent.getHandlerList().unregister(this);
		PlayerQuitEvent.getHandlerList().unregister(this);
	}
	
	
	public void setPlayer(Player p) {
		clearPlayer();
		
		holo.addPlayer(p);
		
		player = p.getUniqueId();
	}
	
	public void clearPlayer() {
		if(player != null) {
			holo.removePlayer(Bukkit.getPlayer(player));
			
			player = null;
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void move(PlayerMoveEvent e) {
		if(!e.getPlayer().getUniqueId().equals(player))
			return;
		
		Location loc = e.getTo().clone();
		
		loc.setYaw(loc.getYaw()+x);
		loc.setPitch(loc.getPitch()+y);
		loc.add(loc.getDirection().multiply(distanceFromPlayer == 0 ? (float)holo.getSize()/1.7f : distanceFromPlayer));
		
		if(onHeightPlayer)
			loc.setY(e.getTo().getY()-((float)y/5f));
		
		holo.setLocation(loc);
		holo.update();
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		if(e.getPlayer().getUniqueId().equals(player))
			clearPlayer();
	}

}
