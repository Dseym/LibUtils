package ru.dseymo.libutils.mc.bungee.localize;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import ru.dseymo.libutils.mc.bungee.Config;

public class Localize extends Config {
	private static final HashMap<String, Localize> CONFIGS = new HashMap<>();
	
	public static void initDefaultLang(String langId, File file, String pathFileJar) {
		CONFIGS.put(langId, new Localize(file, Localize.class.getResourceAsStream(pathFileJar)));
	}
	
	public static void initLang(String langId, File file) {
		CONFIGS.put(langId, new Localize(file));
	}
	
	public static void reloadLang(String langId) {
		if(CONFIGS.containsKey(langId))
			CONFIGS.get(langId).load();
	}
	
	public static void reloadAll() {
		for(String key: CONFIGS.keySet())
			reloadLang(key);
	}
	
	public static void unloadLang(String langId) {
		if(CONFIGS.containsKey(langId))
			CONFIGS.remove(langId);
	}
	
	public static void unloadAll() {
		CONFIGS.clear();
	}
	
	
	public static String message(String langId, String path) {
		if(!CONFIGS.containsKey(langId))
			return langId + ":" + path;
		
		String mess = CONFIGS.get(langId).getMessage(path);
		
		return mess == null ? langId + ":" + path : mess;
	}
	
	
	
	protected Localize(File file) {
		super(file, false);
	}
	
	protected Localize(File file, InputStream fileJar) {
		super(file, fileJar);
	}
	
	public String getMessage(String path) {
		if(!contains(path))
			return null;
		
		return getString(path);
	}

}
