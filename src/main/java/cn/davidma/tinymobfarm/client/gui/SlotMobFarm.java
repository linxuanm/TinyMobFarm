package cn.davidma.tinymobfarm.client.gui;

import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotMobFarm extends SlotItemHandler {

	public SlotMobFarm(IItemHandler inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return NBTHelper.containsMob(NBTHelper.getEssentialNBT(stack));
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}
}