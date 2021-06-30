package ru.dseymo.libutils.mc.bukkit.board;

import java.util.ArrayList;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TitleAnimateWrapper extends BukkitRunnable {
	
	private ArrayList<Board> boards = new ArrayList<>();
	private String[] titles;
	private int current = 0;
	
	public TitleAnimateWrapper(Plugin plugin, int ticks, String...titles) {
		this.titles = titles;
		
		runTaskTimer(plugin, 1, ticks);
	}
	
	public void remove() {
		for(Board board: new ArrayList<>(boards))
			removeBoard(board);
		
		cancel();
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
