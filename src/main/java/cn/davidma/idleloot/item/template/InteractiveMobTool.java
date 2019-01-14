package cn.davidma.idleloot.item.template;

import net.minecraft.item.ItemStack;

public class InteractiveMobTool extends StandardItemBase {

	protected boolean shiny;
	
	public InteractiveMobTool(String name) {
		super(name);
		setMaxStackSize(1);
		shiny = false;
	}
	
	@Override
	public boolean hasEffect(ItemStack item) {
		return shiny;
	}
}
