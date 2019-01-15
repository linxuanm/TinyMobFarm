package cn.davidma.idleloot.item;

import java.util.List;

import cn.davidma.idleloot.item.template.InteractiveMobTool;
import cn.davidma.idleloot.reference.IdleLootConfig;
import cn.davidma.idleloot.util.Msg;
import cn.davidma.idleloot.util.NBTTagHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

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
		NBTTagCompound nbt = NBTTagHelper.getEssentialNBT(stack);
		
		// Already full.
		if (NBTTagHelper.containsMob(nbt)) return false;
		
		// Instantiates entity NBT tag.
		NBTTagCompound entityNBT = new NBTTagCompound();
		if (!mob.writeToNBTAtomically(entityNBT)) {
			Msg.tellPlayer(player, "Cannot capture mob (invalid NBT).");
			return false;
		}
		
		nbt.setTag("entityInfo", entityNBT);
		nbt.setBoolean("shiny", true);
		nbt.setBoolean("containsMob", true);
		nbt.setString("mobName", mob.getName());
		
		// Finish him!
		mob.setDead();
		
		NBTTagHelper.setEssentialNBT(stack, nbt);
		
		return true;
	}

	@Override
	protected boolean interactBlock() {
		return false;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound nbt = NBTTagHelper.getEssentialNBT(stack);
		if (NBTTagHelper.containsMob(nbt)) {
			String mobName = nbt.getString("mobName");
			tooltip.add("Type: " + mobName);
		}
	}
}