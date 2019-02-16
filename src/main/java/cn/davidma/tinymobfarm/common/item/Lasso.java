package cn.davidma.tinymobfarm.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Lasso extends Item {

	public Lasso(Properties properties) {
		super(properties);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}
}
