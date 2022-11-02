package cn.davidma.tinymobfarm.client.gui;

import cn.davidma.tinymobfarm.common.TinyMobFarm;
import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

public class ContainerMobFarm extends Container {
	
	private TileEntityMobFarm tileEntityMobFarm;
	
	public ContainerMobFarm(int windowId, PlayerInventory inv, PacketBuffer buffer) {
		this(windowId, inv, getMobFarm(inv, buffer));
	}
	
	public ContainerMobFarm(int windowId, PlayerInventory inv, TileEntityMobFarm farm) {
		super(TinyMobFarm.containerTypeMobFarm, windowId);
		
		this.tileEntityMobFarm = farm;
		
		IItemHandler itemHandler = farm.getInventory();
		this.addSlot(new SlotLassoOnly(itemHandler, 0, 80, 25) {
			@Override
			public void setChanged() {
				farm.saveAndSync();
			}
		});
		
		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(inv, i, i * 18 + 8, 142));
		}
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 3; j++) {
				this.addSlot(new Slot(inv, i + j * 9 + 9, i * 18 + 8, j * 18 + 84));
			}
		}
	}
	
	@Override
	public boolean stillValid(PlayerEntity player) {
		return true;
	}
	
	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(index);
	
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
		
			int containerSlots = slots.size() - player.inventory.items.size();
	
			if (index < containerSlots) {
				if (!this.moveItemStackTo(itemstack1, containerSlots, slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}
	
			if (itemstack1.getCount() == 0) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
	
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}
	
	public TileEntityMobFarm getCachedFarm() {
		return this.tileEntityMobFarm;
	}
	
	public static TileEntityMobFarm getMobFarm(PlayerInventory inv, PacketBuffer buffer) {
		TileEntity tileEntity = inv.player.level.getBlockEntity(buffer.readBlockPos());
		if (tileEntity instanceof TileEntityMobFarm) return (TileEntityMobFarm) tileEntity;
		throw new IllegalStateException("Tile entity is not in valid state!");
	}
}
