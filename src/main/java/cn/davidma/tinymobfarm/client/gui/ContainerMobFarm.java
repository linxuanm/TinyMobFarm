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
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;

public class ContainerMobFarm extends Container {
	
	public ContainerMobFarm(int windowId, PlayerInventory inv) {
		super(TinyMobFarm.containerTypeMobFarm, windowId);
	}
	
	public ContainerMobFarm(int windowId, PlayerInventory inv, PacketBuffer buffer) {
		this(windowId, inv);
		BlockPos pos = buffer.readBlockPos();
		TileEntity tileEntity = inv.player.getEntityWorld().getTileEntity(pos);
		if (tileEntity instanceof TileEntityMobFarm) {
			this.setup(inv, (TileEntityMobFarm) tileEntity);
		}
	}
	
	public ContainerMobFarm(int windowId, PlayerInventory inv, TileEntityMobFarm farm) {
		this(windowId, inv);
		
		this.setup(inv, farm);
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity player) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
	
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
		
			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();
	
			if (index < containerSlots) {
				if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}
	
			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
	
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}
	
	private void setup(PlayerInventory inv, TileEntityMobFarm farm) {
		IItemHandler itemHandler = farm.getInventory();
		this.addSlot(new SlotLassoOnly(itemHandler, 0, 80, 25) {
			@Override
			public void onSlotChanged() {
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
}
