package ru.dseymo.libutils;

import java.util.ArrayList;
import java.util.HashMap;

import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.debug.LogMessage.LogType;

public class Cooldown {
	private static HashMap<String, HashMap<String, Long>> cooldowns = new HashMap<>();
	
	public static void cooldown(String id, String key, long time) {
		HashMap<String, Long> map = cooldowns.get(id);
		if(map == null)
			map = new HashMap<>();
		
		map.put(key, System.currentTimeMillis()+time);
		
		cooldowns.put(id, map);
		
		Debug.message(LogMessage.create("COOLDOWN", LogType.INFO, id + ":" + key + ": " + time), true);
	}

	public static long getCooldown(String id, String key) {
		if(!isCooldown(id, key))
			return 0;
		
		return cooldowns.get(id).get(key)-System.currentTimeMillis();
	}
	
	public static boolean isCooldown(String id, String key) {
		if(cooldowns.get(id) == null || cooldowns.get(id).get(key) == null)
			return false;
		
		return cooldowns.get(id).get(key) > System.currentTimeMillis();
	}
	
	public static void clear(String id) {
		cooldowns.get(id).clear();
		cooldowns.remove(id);
		
		Debug.message(LogMessage.create("COOLDOWN", LogType.INFO, id + ": Cleared"), true);
	}
	
	public static void clear() {
		for(String id: new ArrayList<>(cooldowns.keySet()))
			clear(id);
	}
	
}
