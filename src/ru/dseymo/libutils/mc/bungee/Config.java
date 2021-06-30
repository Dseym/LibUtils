package ru.dseymo.libutils.mc.bungee;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import ru.dseymo.libutils.FileUtil;

@SuppressWarnings("unchecked")
public class Config {
	public static String JAR_DEFAULT_FOLDER = "config";
	
	
	@Getter
	private File file;
	@Getter
	protected Configuration config;
	private InputStream fileJar;
	
	public Config(File file, InputStream fileJar) {
		file.getParentFile().mkdirs();
		
		this.file = file;
		this.fileJar = fileJar;
		
		load();
	}
	
	public Config(File file, boolean createFromJarDefaultFolder) {
		file.getParentFile().mkdirs();
		
		this.file = file;
		
		if(createFromJarDefaultFolder)
			this.fileJar = getClass().getResourceAsStream("/" + JAR_DEFAULT_FOLDER + "/" + file.getName());
		
		load();
	}
	
	public void save() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void load() {
		try {
			if(!file.exists())
				if (fileJar != null)
					FileUtil.copyFromJar(fileJar, file);
				else
					file.createNewFile();
			
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	
	public boolean contains(String path) {return config.contains(path);}
	public void set(String path, Object obj) {config.set(path, obj);}
	public Collection<String> keys() {return config.getKeys();}
	
	public Object get(String path) {return config.get(path);}
	public String getString(String path) {return (String)get(path);}
	public int getInt(String path) {return (int)get(path);}
	public boolean getBoolean(String path) {return (boolean)get(path);}
	public byte getByte(String path) {return (byte)get(path);}
	public char getChar(String path) {return (char)get(path);}
	public double getDouble(String path) {return (double)get(path);}
	public float getFloat(String path) {return (float)get(path);}
	public long getLong(String path) {return (long)get(path);}
	public short getShort(String path) {return (short)get(path);}
	
	public List<?> getList(String path) {return config.getList(path);}
	public List<String> getStringList(String path) {return (List<String>)getList(path);}
	public List<Byte> getByteList(String path) {return (List<Byte>)getList(path);}
	public List<Character> getCharList(String path) {return (List<Character>)getList(path);}
	public List<Double> getDoubleList(String path) {return (List<Double>)getList(path);}
	public List<Integer> getIntList(String path) {return (List<Integer>)getList(path);}
	public List<Float> getFloatList(String path) {return (List<Float>)getList(path);}
	public List<Long> getLongList(String path) {return (List<Long>)getList(path);}
	public List<Short> getShortList(String path) {return (List<Short>)getList(path);}
	
	public void setUUID(String path, UUID uuid) {
		set(path, uuid.toString());
	}
	
	public UUID getUUID(String path) {
		return UUID.fromString(getString(path));
	}
	
}
