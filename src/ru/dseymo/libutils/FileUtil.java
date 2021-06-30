package ru.dseymo.libutils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;

public class FileUtil {
	
	public static void copyFromJar(InputStream fileJar, File to) {
	    try (OutputStream out = new BufferedOutputStream(new FileOutputStream(to))) {
	        byte[] buffer = new byte[1024];
	        int lengthRead;
	        while ((lengthRead = fileJar.read(buffer)) > 0) {
	            out.write(buffer, 0, lengthRead);
	            out.flush();
	        }
	    } catch (Exception e) {e.printStackTrace();}
	}
	
	public static void copyFolder(File folder, File to) {
	    try {
	    	if(!to.exists())
		    	to.mkdirs();
	    	
	    	File[] contents = folder.listFiles();
		    if (contents != null)
		        for (File f: contents) {
		            File newFile = new File(to.getAbsolutePath() + File.separator + f.getName());
		            if (f.isDirectory())
		                copyFolder(f, newFile);
		            else
		            	Files.copy(folder.toPath(), to.toPath());
		        }
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void removeFolder(File folder) {
	    try {
	    	File[] contents = folder.listFiles();
		    if (contents != null)
		        for (File f: contents)
		            if (f.isDirectory())
		            	removeFolder(f);
		            else
		            	folder.delete();
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void clearFile(File file) {
		try {
			new PrintWriter(file).close();
		} catch (FileNotFoundException e) {e.printStackTrace();}
	}
	
}
