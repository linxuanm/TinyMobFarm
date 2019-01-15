package cn.davidma.idleloot.item;

import cn.davidma.idleloot.item.template.InteractiveMobTool;
import cn.davidma.idleloot.reference.IdleLootConfig;
import cn.davidma.idleloot.util.Msg;
import cn.davidma.idleloot.util.NBTTagHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

public class Lasso extends InteractiveMobTool {

	public Lasso(String name) {
		super(name, IdleLootConfig.LASSO_DURABILITY, new boolean[] {false, true});
	}

	@Override
	protected String[] verb() {
		return new String[] {"capture", "captured"};
	}

	@Override
	protected boolean interactEntity(ItemStack stack, EntityPlayer player, EntityLivingBase mob) {
		
		//NBT time!
		NBTTagCompound nbt = NBTTagHelper.getNBT(stack);
		
		// Already full.
		if (NBTTagHelper.containsMob(nbt)) return false;
		
		nbt.setBoolean("shiny", true);
		nbt.setBoolean("containsMob", true);
		stack.setTagCompound(nbt);
		
		return true;
	}

	@Override
	protected boolean interactBlock() {
		return false;
	}
}