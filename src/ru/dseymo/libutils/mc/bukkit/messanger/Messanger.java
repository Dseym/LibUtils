package ru.dseymo.libutils.mc.bukkit.messanger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedTransferQueue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import lombok.Getter;

public class Messanger extends BukkitRunnable implements PluginMessageListener {
	private LinkedTransferQueue<ByteArrayDataOutput> queueMessages = new LinkedTransferQueue<>();
	private ArrayList<Message<?>> messages = new ArrayList<>();
	
	@Getter
	private Plugin plugin;
	@Getter
	private String channel;
	
	public Messanger(Plugin plugin, String channel) {
		this.plugin = plugin;
		this.channel = channel;
		
		runTaskTimer(plugin, 20, 5);
		
		Messenger pm = Bukkit.getMessenger();
		
		pm.registerIncomingPluginChannel(plugin, channel, this);
		pm.registerOutgoingPluginChannel(plugin, channel);
	}
	
	public void unregister() {
		Messenger pm = Bukkit.getMessenger();
		
		pm.unregisterIncomingPluginChannel(plugin, channel);
		pm.unregisterOutgoingPluginChannel(plugin, channel);
	}
	
	
	@Override
	public void onPluginMessageReceived(String channel, Player p, byte[] _mess) {
		if(channel.equals(this.channel))
			for(Message<?> mess: new ArrayList<>(messages))
				if(mess.handleNewMessage(p, ByteStreams.newDataInput(_mess.clone())))
					messages.remove(mess);
	}
	
	public void sendMessage(Message<?> message) {
		if(!messages.contains(message)) {
			messages.add(message);
			
			queueMessages.add(message.out);
		}
	}
	
	@Override
	public void run() {
		if(queueMessages.isEmpty())
			return;
		
		Iterator<? extends Player> iter = Bukkit.getOnlinePlayers().iterator();
		
		if(iter.hasNext()) {
			Player p = iter.next();
			ByteArrayDataOutput out;
			
			while((out = queueMessages.poll()) != null)
				p.sendPluginMessage(plugin, channel, out.toByteArray());
		}
	}

}
