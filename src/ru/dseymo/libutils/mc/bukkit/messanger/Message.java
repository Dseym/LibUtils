package ru.dseymo.libutils.mc.bukkit.messanger;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import lombok.Getter;

public abstract class Message<A> {
	ByteArrayDataOutput out;
	@Getter
	protected boolean done = false;
	protected A answer = null;
	
	public Message(ByteArrayDataOutput out) {
		this.out = out;
	}
	
	public abstract boolean handleNewMessage(Player p, ByteArrayDataInput mess);
	
	public void setDone(A answer) {
		done = true;
		this.answer = answer;
	}
	
	public A getAnswer() {
		return answer;
	}
	
	public void await(long time) throws Exception {
		Thread.sleep(time);
	}
	
	public boolean awaitAndCheckDone(long time) throws Exception {
		if(done)
			return true;
		
		await(time);
		
		return done;
	}
	
	@Override
	public int hashCode() {
		return (out.hashCode() + 283)*3;
	}
	
}
