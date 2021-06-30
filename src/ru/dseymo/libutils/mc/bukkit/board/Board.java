package ru.dseymo.libutils.mc.bukkit.board;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import lombok.Getter;
import ru.dseymo.libutils.mc.bukkit.ColorUtil;

public abstract class Board {
	
	@Getter
	private Plugin plugin;
	private Scoreboard board;
	private BukkitTask task;
	
	public Board(Plugin plugin, String title) {
		this.plugin = plugin;
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		setTitle(title);
		setTimeUpdate(20);
	}
	
	public void remove() {
		task.cancel();
		clearLines();
		
		plugin = null;
		board = null;
		task = null;
	}
	
	
	public void addPlayer(Player p) {
		p.setScoreboard(board);
	}
	
	public void removePlayer(Player p) {
		p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}
	
	public void clearLines() {
		for(String str: board.getEntries())
			board.resetScores(str);
	}
	
	public Objective getObjective() {
		Objective obj = board.getObjective("object");
		if(obj == null) {
			obj = board.registerNewObjective("object", "dummy");
			obj.setDisplayName("Board");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		}
		
		return obj;
	}
	
	public void setTimeUpdate(int ticks) {
		if(ticks < 1)
			ticks = 1;
		if(task != null)
			task.cancel();
		
		task = new BukkitRunnable() {
			
			@Override
			public void run() {update();}
			
		}.runTaskTimer(plugin, ticks, ticks);
	}
	
	public void setLines(String...lines) {
		Objective obj = getObjective();
		for(int i = 0; i < lines.length; i++) {
			String str = lines[i];
			
			if(str.length() > 16)
				str = str.substring(0, 15);
			
			obj.getScore(ColorUtil.color(str)).setScore(lines.length-i-1);
		}
	}
	
	public void setTitle(String title) {
		if(title.length() > 32)
			title = title.substring(0, 31);
		
		getObjective().setDisplayName(ColorUtil.color(title));
	}
	
	public abstract void update();
	
}
