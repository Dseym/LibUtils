package ru.dseymo.libutils.storage;

import java.util.List;
import java.util.Map.Entry;

public interface Storage<K, V> extends Iterable<Entry<K, V>> {
	
	public void set(K key, V value, boolean useCache);
	
	public V get(K key, boolean useCache);
	
	public void remove(K key);
	
	public boolean contains(K key, boolean useOnlyCache);
	
	public List<K> keys();
	
	public List<V> values();
	
	public void close();
	
	public void clear();
	
}
