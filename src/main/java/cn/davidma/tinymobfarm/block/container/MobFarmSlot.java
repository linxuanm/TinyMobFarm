package cn.davidma.tinymobfarm.block.container;

import cn.davidma.tinymobfarm.util.NBTTagHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class MobFarmSlot extends Slot {

	public MobFarmSlot(IInventory inventory, int index, int x, int y) {
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