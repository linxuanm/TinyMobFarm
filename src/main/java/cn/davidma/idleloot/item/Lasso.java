package cn.davidma.idleloot.item;

import cn.davidma.idleloot.item.template.InteractiveMobTool;
import cn.davidma.idleloot.reference.IdleLootConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class Lasso extends InteractiveMobTool {

	public Lasso(String name) {
		super(name);
		setMaxDamage(IdleLootConfig.LASSO_DURABILITY);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack item, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		
		// Check.
		if (!target.isEntityAlive()) return false;
		if (target.world.isRemote) return true;
		
		target.setDead();
		System.out.println(target.getName());
		// item.damageItem(1, player);
		return true;
	}
}