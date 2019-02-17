package cn.davidma.tinymobfarm.common.item;

import java.util.List;

import javax.annotation.Nullable;

import cn.davidma.tinymobfarm.core.util.Msg;
import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

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
		if (NBTHelper.hasMob(stack) || !target.isAlive() || !(target instanceof EntityLiving)) return false;
		
		NBTTagCompound nbt = NBTHelper.getBaseTag(stack);
		
		// Cannot capture boss.
		if (!target.isNonBoss()) {
			Msg.tellPlayer(player, "tinymobfarm.error.cannot_capture_boss.key");
			return true;
		}
		
		// Hand animation.
		if (player.world.isRemote()) {
			return true;
		} else {
			NBTTagCompound mobData = target.serializeNBT();
			nbt.setTag(NBTHelper.MOB_DATA, mobData);
			nbt.setString(NBTHelper.MOB_NAME, target.getName().toString());
			nbt.setDouble(NBTHelper.MOB_HEALTH, Math.round(target.getHealth() * 10) / 10.0);
			nbt.setDouble(NBTHelper.MOB_MAX_HEALTH, target.getMaxHealth());
			nbt.setBoolean(NBTHelper.MOB_HOSTILE, target.isCreatureType(EnumCreatureType.MONSTER, false));
		}
		
		player.inventory.markDirty();
		return true;
	}
		
	/*@Override
	public EnumActionResult onItemUse(ItemUseContext context) {
		ItemStack stack = context.getItem();
		
	}*/
	
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		if (NBTHelper.hasMob(stack)) {
			NBTTagCompound nbt = NBTHelper.getBaseTag(stack);
			String name = nbt.getString(NBTHelper.MOB_NAME);
			double health = nbt.getDouble(NBTHelper.MOB_HEALTH);
			double maxHealth = nbt.getDouble(NBTHelper.MOB_MAX_HEALTH);
			
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.release_mob.key"));
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.mob_name.key", name));
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.health.key", health, maxHealth));
			if (nbt.getBoolean(NBTHelper.MOB_HOSTILE)) tooltip.add(Msg.tooltip("tinymobfarm.tooltip.hostile.key"));
		} else {
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.capture.key"));
		}
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return NBTHelper.hasMob(stack);
	}
}
