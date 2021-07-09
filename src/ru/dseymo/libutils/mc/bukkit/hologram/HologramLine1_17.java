package ru.dseymo.libutils.mc.bukkit.hologram;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import ru.dseymo.libutils.ReflUtil;
import ru.dseymo.libutils.mc.bukkit.ColorUtil;

public class HologramLine1_17 implements IHologramLine {
	
	EntityArmorStand stand;
	Location loc;
	
	public HologramLine1_17(String text, Location loc) {
		stand = new EntityArmorStand((World)ReflUtil.invoke(Bukkit.getWorlds().get(0), "getHandle"), 0, 0, 0);
		
		stand.setInvisible(true);
		stand.setNoGravity(true);
		stand.setCustomNameVisible(true);
		
		setText(text);
		setLocation(loc);
	}

	@Override
	public Location getLocation() {
		return loc;
	}

	@Override
	public Object getStand() {
		return stand;
	}

	@Override
	public void setLocation(Location loc) {
		this.loc = loc;
		
		stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
	}

	@Override
	public void setText(String text) {
		text = ColorUtil.color(text);
		
		stand.setCustomName(new ChatComponentText(ColorUtil.color(text)));
	}
	
}
