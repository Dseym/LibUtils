package ru.dseymo.libutils.mc.bukkit.localize;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import ru.dseymo.libutils.mc.bukkit.Config;

public class Localize {
	
	private HashMap<String, Config> configs = new HashMap<>();
	
	public void addLang(String langId, File file) {
		configs.put(langId, new Config(file, false));
	}
	
	public void addDefaultLang(String langId, File file, InputStream streamFileFromJar) {
		configs.put(langId, new Config(file, streamFileFromJar));
	}
	
	public void reload(String langId) {
		if(configs.containsKey(langId))
			configs.get(langId).load();
	}
	
	public void reloadAll() {
		for(String key: configs.keySet())
			reload(key);
	}
	
	public void unload(String langId) {
		if(configs.containsKey(langId))
			configs.remove(langId);
	}
	
	public void unloadAll() {
		configs.clear();
	}
	
	
	public String message(String langId, String path) {
		if(!configs.containsKey(langId) || !configs.get(langId).contains(path))
			return langId + ":" + path;
		
		return configs.get(langId).getString(path);
	}

}
