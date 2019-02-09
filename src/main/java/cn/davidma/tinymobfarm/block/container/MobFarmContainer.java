package cn.davidma.tinymobfarm.block.container;

import cn.davidma.tinymobfarm.tileentity.MobFarmTileEntity;
import cn.davidma.tinymobfarm.util.Msg;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class MobFarmContainer extends Container {

	public MobFarmContainer(InventoryPlayer playerInv, MobFarmTileEntity tileEntity) {
		IItemHandler inventory = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		addSlotToContainer(new MobFarmSlot(inventory, 0, 80, 25) {
			@Override
			public void onSlotChanged() {
				tileEntity.sendUpdate();
			}
		});
		
		// Player hotbar slots.
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(playerInv, i, i*18 + 8, 142));
		}
		
		// PLayer inventory slots.
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 3; j++) {
				addSlotToContainer(new Slot(playerInv, i + j*9 + 9, i*18 + 8, j*18 + 84));
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