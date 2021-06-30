package ru.dseymo.libutils.mc.bukkit;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;
import ru.dseymo.libutils.FileUtil;

public class Config extends YamlConfiguration {
	public static String JAR_DEFAULT_FOLDER = "config";
	
	
	@Getter
	private File file;
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
			save(file);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void load() {
		try {
			if(!file.exists())
				if (fileJar != null)
					FileUtil.copyFromJar(fileJar, file);
				else
					file.createNewFile();
			
			load(file);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	
	public Location getLocation(String path) {
		try {
			return LocUtil.parseLoc(getString(path));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setLocation(String path, Location loc, boolean exactLoc , boolean rotate) {
		set(path, LocUtil.toString(loc, exactLoc, rotate));
	}
	
	public void setUUID(String path, UUID uuid) {
		set(path, uuid.toString());
	}
	
	public UUID getUUID(String path) {
		return UUID.fromString(getString(path));
	}
	
}
