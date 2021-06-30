package ru.dseymo.libutils;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.debug.LogMessage.LogType;

public class ThreadService {
	private static HashMap<String, ThreadPoolExecutor> syncPools = new HashMap<>();
	private static ThreadPoolExecutor asyncPool = new ThreadPoolExecutor(0, 10000, Long.MAX_VALUE, TimeUnit.NANOSECONDS, new SynchronousQueue<>());
	
	static {
		asyncPool.setKeepAliveTime(20, TimeUnit.SECONDS);
	}
	
	public static void startSync(String id, Runnable run) {
		ThreadPoolExecutor service = syncPools.get(id);
		if(service == null) {
			service = new ThreadPoolExecutor(0, 1, Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingDeque<>());
			service.setKeepAliveTime(10, TimeUnit.SECONDS);
			syncPools.put(id, service);
			
			Debug.message(LogMessage.create("THREAD-SERVICE", LogType.INFO, "Pool:Sync:" + id + ": Created"), true);
		}
		
		service.execute(run);
	}
	
	public static void startAsync(Runnable run) {
		asyncPool.execute(run);
		
		Debug.message(LogMessage.create("THREAD-SERVICE", LogType.INFO, "Thread:Async: Runned"), false);
	}
	
	
	public static void joinSync() {
		for(ThreadPoolExecutor pool: syncPools.values())
			try {
				while(!pool.awaitTermination(200, TimeUnit.MILLISECONDS)) {}
			} catch (InterruptedException e) {e.printStackTrace();}
		
		Debug.message(LogMessage.create("THREAD-SERVICE", LogType.INFO, "Sync: Joined"), true);
	}
	
	public static void joinAsync() {
		try {
			while(!asyncPool.awaitTermination(200, TimeUnit.MILLISECONDS)) {}
		} catch (InterruptedException e) {e.printStackTrace();}
		
		Debug.message(LogMessage.create("THREAD-SERVICE", LogType.INFO, "Async: Joined"), true);
	}
	
	public static void joinAll() {
		joinSync();
		joinAsync();
	}
	
	public static void shutdown() {
		for(ThreadPoolExecutor pool: syncPools.values())
			pool.shutdown();
		
		syncPools.clear();
		
		asyncPool.shutdown();
		
		Debug.message(LogMessage.create("THREAD-SERVICE", LogType.INFO, "Shutdown"), true);
	}
	
}
