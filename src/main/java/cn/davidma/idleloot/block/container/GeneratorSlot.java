package cn.davidma.idleloot.block.container;

import cn.davidma.idleloot.util.NBTTagHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GeneratorSlot extends Slot {

	public GeneratorSlot(IInventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return NBTTagHelper.containsMob(NBTTagHelper.getEssentialNBT(stack));
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}
}