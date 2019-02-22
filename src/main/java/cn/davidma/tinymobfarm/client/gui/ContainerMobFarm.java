package cn.davidma.tinymobfarm.client.gui;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ContainerMobFarm extends Container {

	public ContainerMobFarm(InventoryPlayer playerInv, TileEntityMobFarm tileEntityMobFarm) {
		IItemHandler itemHandler = tileEntityMobFarm.getInventory();
		this.addSlotToContainer(new SlotLassoOnly(itemHandler, 0, 80, 25) {
			@Override
			public void onSlotChanged() {
				tileEntityMobFarm.saveAndSync();
			}
		});
		
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(playerInv, i, i * 18 + 8, 142));
		}
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 3; j++) {
				this.addSlotToContainer(new Slot(playerInv, i + j * 9 + 9, i * 18 + 8, j * 18 + 84));
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
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
}
