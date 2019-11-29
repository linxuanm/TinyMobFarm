package cn.davidma.tinymobfarm.client.gui;

import cn.davidma.tinymobfarm.common.item.ItemLasso;
import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotLassoOnly extends SlotItemHandler {

	public SlotLassoOnly(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ItemLasso && NBTHelper.hasMob(stack);
	}
	
	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
