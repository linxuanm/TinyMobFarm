package cn.davidma.idleloot.item.template;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

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
