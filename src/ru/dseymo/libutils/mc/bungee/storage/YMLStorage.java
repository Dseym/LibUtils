package ru.dseymo.libutils.mc.bungee.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import ru.dseymo.libutils.FileUtil;
import ru.dseymo.libutils.mc.bungee.Config;
import ru.dseymo.libutils.storage.Storage;

public abstract class YMLStorage<K, V> implements Storage<K, V> {
	
	protected HashMap<K, V> data = new HashMap<>();
	protected Config yml;
	
	public YMLStorage(Config config) {
		yml = config;
	}
	
	@Override
	public Iterator<Entry<K, V>> iterator() {
		return data.entrySet().iterator();
	}
	
	public void unload(K key) {
		if(contains(key, true))
			data.remove(key);
	}
	
	public List<K> keys() {
		return new ArrayList<>(data.keySet());
	}
	
	public List<V> values() {
		return new ArrayList<>(data.values());
	}
	
	@Override
	public void close() {
		for(K key: new ArrayList<>(data.keySet()))
			unload(key);
	}

	@Override
	public void clear() {
		data.clear();
		
		FileUtil.clearFile(yml.getFile());
	}

}
