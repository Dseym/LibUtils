package ru.dseymo.libutils.mc.bukkit;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import ru.dseymo.libutils.mc.bukkit.packet.ProtocolVer;

public class Menu implements Listener {
	public static ItemStack FILL_ALL_ITEM;
	protected final static int MAX_SIZE = 54;
	
	static {
		if(ProtocolVer.v1_13.isThatOrNewest())
			FILL_ALL_ITEM = ItemUtil.generateItem(Material.valueOf("GRAY_STAINED_GLASS_PANE"), " ");
		else
			FILL_ALL_ITEM = ItemUtil.generateItem(Material.valueOf("STAINED_GLASS_PANE"), (byte)15, " ");
	}
	
	
	protected Inventory inv;
	private boolean autoRemove;
	private Plugin plugin;
	
	public Menu(Plugin plugin, String title, int size, boolean autoRemove, boolean fillAll) {
		inv = Bukkit.createInventory(null, MAX_SIZE < size ? MAX_SIZE : size, ColorUtil.color(title));
		this.autoRemove = autoRemove;
		this.plugin = plugin;
		
		if(fillAll)
			fillAll(FILL_ALL_ITEM);
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public void remove() {
		ArrayList<HumanEntity> viewers = new ArrayList<>(inv.getViewers());
		for(HumanEntity h: viewers)
			h.closeInventory();
		
		InventoryClickEvent.getHandlerList().unregister(this);
		InventoryDragEvent.getHandlerList().unregister(this);
		InventoryOpenEvent.getHandlerList().unregister(this);
		InventoryCloseEvent.getHandlerList().unregister(this);
	}
	
	
	public Inventory getInv() {
		return inv;
	}
	
	protected void fillAll(ItemStack stack) {
		for(int i = 0; i < inv.getSize(); i++)
			inv.setItem(i, stack);
	}
	
	public void open(Player p) {
		p.closeInventory(); p.openInventory(inv);
	}
	
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if(inv.equals(e.getWhoClicked().getOpenInventory().getTopInventory()))
			e.setCancelled(onClick((Player)e.getWhoClicked(), e.getCurrentItem(), e.getSlot(), e.getClick(), !inv.equals(e.getClickedInventory())));
	}
	
	@EventHandler
	public void drag(InventoryDragEvent e) {
		if(inv.equals(e.getInventory()))
			e.setCancelled(onDrag((Player)e.getWhoClicked(), e.getNewItems(), e.getInventorySlots(), e.getType()));
	}
	
	@EventHandler
	public void open(InventoryOpenEvent e) {
		if(inv.equals(e.getInventory()))
			e.setCancelled(onOpen((Player)e.getPlayer()));
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if(inv.equals(e.getInventory())) {
			onClose((Player)e.getPlayer());
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					if(autoRemove && inv.getViewers().size() == 0)
						remove();
				}
				
			}.runTaskLater(plugin, 1);
		}
	}
	
	protected boolean onClick(Player p, ItemStack item, int slot, ClickType click, boolean isInvPlayer) {return true;}
	protected boolean onDrag(Player p, Map<Integer, ItemStack> items, Set<Integer> slots, DragType drag) {return true;}
	protected boolean onOpen(Player p) {return false;}
	protected void onClose(Player p) {}
	
}
