package cn.davidma.idleloot.item;

import cn.davidma.idleloot.item.template.InteractiveMobTool;
import cn.davidma.idleloot.reference.IdleLootConfig;
import cn.davidma.idleloot.util.Msg;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
	protected boolean interactEntity(ItemStack item, EntityPlayer player, EntityLivingBase mob) {
		
		//NBT time!
	}

	@Override
	protected boolean interactBlock() {
		return false;
	}
}