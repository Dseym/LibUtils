package ru.dseymo.libutils.mc.bungee.board;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.protocol.packet.ScoreboardDisplay;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;
import net.md_5.bungee.protocol.packet.ScoreboardObjective.HealthDisplay;
import net.md_5.bungee.protocol.packet.ScoreboardScore;
import net.md_5.bungee.scheduler.BungeeScheduler;
import ru.dseymo.libutils.mc.bungee.ColorUtil;

public abstract class Board {
	
	private String id;
	@Getter
	private Plugin plugin;
	private ScoreboardObjective obj;
	private ArrayList<ScoreboardScore> scores = new ArrayList<>();
	private ScheduledTask task;
	private ArrayList<UUID> uuids = new ArrayList<>();
	
	public Board(Plugin plugin, String title) {
		id = "sb" + new Random().nextInt(Integer.MAX_VALUE);
		this.plugin = plugin;
		obj = new ScoreboardObjective(id, "Board", HealthDisplay.INTEGER, (byte)0);
		
		setTitle(title);
		setTimeUpdate(1);
	}
	
	public void remove() {
		clearLines();
		task.cancel();
		uuids.clear();
		scores.clear();
		
		plugin = null;
		obj = null;
		task = null;
	}
	
	
	public void addPlayer(ProxiedPlayer p) {
		if(!uuids.contains(p.getUniqueId()))
			uuids.add(p.getUniqueId());
	}
	
	public void removePlayer(ProxiedPlayer p) {
		uuids.remove(p.getUniqueId());
	}
	
	private ArrayList<ProxiedPlayer> getOnlinePlayers() {
		ArrayList<ProxiedPlayer> players = new ArrayList<>();
		
		for(UUID uuid: uuids) {
			ProxiedPlayer p = BungeeCord.getInstance().getPlayer(uuid);
			
			if(p != null)
				players.add(p);
		}
		
		return players;
	}
	
	public void clearLines() {
		for(ScoreboardScore score: scores)
			for(ProxiedPlayer p: getOnlinePlayers())
				p.unsafe().sendPacket(new ScoreboardScore(score.getItemName(), (byte)1, id, 0));
		
		scores.clear();
	}
	
	public void setTimeUpdate(int secs) {
		if(secs < 1)
			secs = 1;
		if(task != null)
			task.cancel();
		
		task = new BungeeScheduler().schedule(plugin, () -> {
			update();
			
			for(UUID uuid: uuids) {
				ProxiedPlayer p = BungeeCord.getInstance().getPlayer(uuid);
				
				if(p != null) {
					p.unsafe().sendPacket(obj);
					
					for(ScoreboardScore score: scores)
						p.unsafe().sendPacket(score);
					
					p.unsafe().sendPacket(new ScoreboardDisplay((byte)1, id));
				}
			}
		}, 1, secs, TimeUnit.SECONDS);
	}
	
	public void setLines(String...lines) {
		for(int i = 0; i < lines.length; i++) {
			String str = lines[i];
			
			if(str.length() > 16)
				str = str.substring(0, 15);
			
			scores.add(new ScoreboardScore(ColorUtil.color(str), (byte)0, id, lines.length-i-1));
		}
	}
	
	public void setTitle(String title) {
		if(title.length() > 32)
			title = title.substring(0, 31);
		
		obj.setValue(ColorUtil.color(title));
	}
	
	public abstract void update();
	
}
