package cn.davidma.tinymobfarm.common.item;

import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

public class Lasso extends Item {

	public Lasso(Properties properties) {
		super(properties);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		if (NBTHelper.hasMob(stack)) return false;
		
		NBTTagCompound nbt = NBTHelper.getBaseTag(stack);
		
		// Cannot capture boss.
		if (!target.isNonBoss()) {
			Msg.tellPlayer(player, "");
		}
	}
		
	@Override
	public EnumActionResult onItemUse(ItemUseContext context) {
		ItemStack stack = context.getItem();
		
	}
	
}
