package cn.davidma.tinymobfarm.block.container;

import cn.davidma.tinymobfarm.tileentity.MobFarmTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MobFarmContainer extends Container {

	private MobFarmTileEntity tileEntity;
	private int currProgress, totalProgress;
	
	public MobFarmContainer(InventoryPlayer player, MobFarmTileEntity tileEntity) {
		this.tileEntity = tileEntity;
		addSlotToContainer(new MoobFarmSlot(tileEntity, 0, 80, 25));
		
		// Player hotbar slots.
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player, i, i*18 + 8, 142));
		}
		
		// PLayer inventory slots.
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 3; j++) {
				addSlotToContainer(new Slot(player, i + j*9 + 9, i*18 + 8, j*18 + 84));
			}
		}
	}
	
	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, tileEntity);
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (IContainerListener i: listeners) {
			if (currProgress != tileEntity.getField(0)) i.sendWindowProperty(this, 0, tileEntity.getField(0));
			if (totalProgress != tileEntity.getField(1)) i.sendWindowProperty(this, 1, tileEntity.getField(1));
		}
		currProgress = tileEntity.getField(0);
		totalProgress = tileEntity.getField(1);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(slotId);
		if (slot == null || !slot.getHasStack()) return ItemStack.EMPTY;
		
		ItemStack slotStack = slot.getStack();
		stack = slotStack.copy();
		
		if (slotId == 0) {
			
			// Send item from slot to inventory.
			if (!this.mergeItemStack(slotStack, 1, 37, true)) return ItemStack.EMPTY;
			slot.onSlotChange(slotStack, stack);	
			
		} else {
			
			// Send item from inventory to slot.
			if (!this.mergeItemStack(slotStack, 0, 1, false)) return ItemStack.EMPTY;
			
		}
		
		// Cleanup check.
		if (slotStack.isEmpty() || slotStack.getCount() == 0) {
			slot.putStack(ItemStack.EMPTY);
		} else {
			slot.onSlotChanged();
		}
		
		if (slotStack.getCount() == stack.getCount()) return ItemStack.EMPTY;
		slot.onTake(player, slotStack);
		
		return stack;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		tileEntity.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUsableByPlayer(player);
	}
}
