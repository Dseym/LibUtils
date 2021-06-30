package ru.dseymo.libutils.mc.bungee.board;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.scheduler.BungeeScheduler;

public class TitleAnimateWrapper implements Runnable {
	
	private ArrayList<Board> boards = new ArrayList<>();
	private String[] titles;
	private int current = 0;
	private ScheduledTask task;
	
	public TitleAnimateWrapper(Plugin plugin, int secs, String...titles) {
		this.titles = titles;
		
		task = new BungeeScheduler().schedule(plugin, this, 1, secs, TimeUnit.SECONDS);
	}
	
	public void remove() {
		for(Board board: new ArrayList<>(boards))
			removeBoard(board);
		
		task.cancel();
	}
	
	
	public void addBoard(Board board) {
		if(!boards.contains(board))
			boards.add(board);
	}
	
	public void removeBoard(Board board) {
		boards.remove(board);
	}

	@Override
	public void run() {
		for(Board board: new ArrayList<>(boards)) {
			if(board.getPlugin() == null) {
				boards.remove(board);
				continue;
			}
			
			board.setTitle(titles[current]);
		}
		
		if(++current+1 > titles.length)
			current = 0;
	}
	
}
