package ru.dseymo.libutils.debug;

import java.util.ArrayList;
import java.util.HashMap;

public class Debug {
	public static boolean ENABLE = false,
						  LOG_IN_FILE = false;
	
	public static HashMap<Object, LogHandler> HANDLERS = new HashMap<>();
	
	private static ArrayList<String> KEYS = new ArrayList<>();
	
	public static void registerHandler(Object key, LogHandler handler) {
		if(!HANDLERS.containsValue(handler))
			HANDLERS.put(key, handler);
	}
	
	public static void unregisterHandler(Object key) {
		HANDLERS.remove(key);
	}
	
	public static void remove() {
		for(Object key: new ArrayList<>(HANDLERS.keySet()))
			unregisterHandler(key);
		
		KEYS.clear();
	}
	
	public static ArrayList<String> getKeys() {
		return new ArrayList<>(KEYS);
	}
	
	
	public static void message(LogMessage message, boolean inFile) {
		if(ENABLE) {
			if(!KEYS.contains(message.getKey()))
				KEYS.add(message.getKey());
			
			for(LogHandler handler: HANDLERS.values()) {
				handler.newMessage(message);
				
				if(inFile && LOG_IN_FILE)
					handler.newLogInFile(message);
			}
		}
	}
	
}
