package ru.dseymo.libutils.mc.bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.minecraft.server.v1_8_R3.EntityWither;
import ru.dseymo.libutils.mc.bukkit.packet.PacketFactory;

public class BossBar implements Listener {
	private static ArrayList<BossBar> BARS = new ArrayList<>();
	private static BukkitTask TASK = null;
	
	public static ArrayList<BossBar> getBars() {
		return BARS;
	}
	
	public static void removeAll() {
		for(BossBar bar: BARS)
			bar.remove();
	}
	
	 
    private String title;
    private Map<UUID, EntityWither> withers = new HashMap<>();
    private BukkitRunnable run;

    public BossBar(Plugin plugin, String title) {
        this.title = title;
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        if(TASK == null)
			TASK = new BukkitRunnable() {
				
				@Override
				public void run() {
					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
						for(BossBar bar: BARS)
							bar.update();
					});
				}
				
			}.runTaskTimer(plugin, 5, 5);
    }
    
    public void remove() {
    	PlayerQuitEvent.getHandlerList().unregister(this);
    	
    	for(EntityWither wither: new ArrayList<>(withers.values()))
    		PacketFactory.destroyEntities(wither.getId()).sendAll();
    	
    	run.cancel();
    	withers.clear();
    	
    	if(BARS.size() == 0 && TASK != null) {
			TASK.cancel();
			TASK = null;
    	}
    }
 
    public void addPlayer(Player p) {
    	if(withers.containsKey(p.getUniqueId())) return;
    	
        EntityWither wither = new EntityWither(((CraftWorld)p.getWorld()).getHandle());
        Location l = getWitherLocation(p.getLocation());
        
        wither.setCustomName(title);
        wither.setInvisible(true);
        wither.setLocation(l.getX(), l.getY(), l.getZ(), 0, 0);
        
        PacketFactory.spawnEntityLiving(wither).send(p);
        withers.put(p.getUniqueId(), wither);
    }

    public void removePlayer(Player p) {
    	if(!withers.containsKey(p.getUniqueId())) return;
    	
        EntityWither wither = withers.remove(p.getUniqueId());
        PacketFactory.destroyEntities(MCUtil.getEntityId(wither)).send(p);
    }
    
    public boolean contains(Player p) {
    	return withers.containsKey(p.getUniqueId());
    }
 
    public void setTitle(String title) {
        this.title = title;
        for (UUID uuid: withers.keySet())
        	withers.get(uuid).setCustomName(title);
    }
 
    public void setProgress(double progress) {
    	for (UUID uuid: withers.keySet())
            withers.get(uuid).setHealth((float) (progress * withers.get(uuid).getMaxHealth()));
    }
 
    public Location getWitherLocation(Location l) {
        return l.add(l.getDirection().multiply(30));
    }

    public void update() {
    	for (UUID uuid: withers.keySet()) {
    		Player p = Bukkit.getPlayer(uuid);
            EntityWither wither = withers.get(uuid);
            Location l = getWitherLocation(p.getLocation());
            wither.setLocation(l.getX(), l.getY(), l.getZ(), 0, 0);
            PacketFactory.spawnEntityLiving(wither).send(p);
            PacketFactory.teleportEntity(l, MCUtil.getEntityId(wither)).send(p);
        }
    }
    
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		if(withers.containsKey(e.getPlayer().getUniqueId()))
			withers.remove(e.getPlayer().getUniqueId());
	}

}