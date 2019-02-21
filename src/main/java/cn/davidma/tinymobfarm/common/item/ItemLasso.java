package cn.davidma.tinymobfarm.common.item;

import java.util.List;

import cn.davidma.tinymobfarm.common.TinyMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import cn.davidma.tinymobfarm.core.util.Config;
import cn.davidma.tinymobfarm.core.util.Msg;
import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class ItemLasso extends Item {

	public ItemLasso(Properties properties) {
		super(properties.group(TinyMobFarm.creativeTab).defaultMaxDamage(Config.LASSO_DURABILITY));
		this.setRegistryName(Reference.getLocation("lasso"));
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
			if (!player.world.isRemote) {
				Msg.tellPlayer(player, "tinymobfarm.error.cannot_capture_boss.key");
			}
			return true;
		}
		
		if (!player.world.isRemote()) {
			NBTTagCompound mobData = target.serializeNBT();
			nbt.setTag(NBTHelper.MOB_DATA, mobData);
			nbt.setString(NBTHelper.MOB_NAME, target.getName().getUnformattedComponentText());
			nbt.setDouble(NBTHelper.MOB_HEALTH, Math.round(target.getHealth() * 10) / 10.0);
			nbt.setDouble(NBTHelper.MOB_MAX_HEALTH, target.getMaxHealth());
			nbt.setBoolean(NBTHelper.MOB_HOSTILE, target.isCreatureType(EnumCreatureType.MONSTER, false));
			
			if (player.isCreative()) {
				ItemStack newLasso = new ItemStack(this);
				NBTHelper.setBaseTag(newLasso, nbt);
				player.addItemStackToInventory(newLasso);
			}
			
			target.remove();
			player.inventory.markDirty();
		}
		
		return true;
	}
		
	@Override
	public EnumActionResult onItemUse(ItemUseContext context) {
		EntityPlayer player = context.getPlayer();
		ItemStack stack = context.getItem();
		EnumFacing facing = context.getFace();
		BlockPos pos = context.getPos().offset(facing);
		World world = context.getWorld();
		
		if (!NBTHelper.hasMob(stack)) return EnumActionResult.FAIL;
		
		if (!player.canPlayerEdit(pos, facing, stack)) return EnumActionResult.FAIL;
		
		if (!context.getWorld().isRemote()) {
			NBTTagCompound nbt = NBTHelper.getBaseTag(stack);
			NBTTagCompound mobData = nbt.getCompound(NBTHelper.MOB_DATA);
			
			NBTTagDouble x = new NBTTagDouble(pos.getX() + 0.5);
			NBTTagDouble y = new NBTTagDouble(pos.getY());
			NBTTagDouble z = new NBTTagDouble(pos.getZ() + 0.5);
			NBTTagList mobPos = NBTHelper.createNBTList(x, y, z);
			mobData.setTag("Pos", mobPos);
			
			Entity mob = EntityType.create(mobData, world);
			if (mob != null) world.spawnEntity(mob);
			
			stack.removeChildTag(NBTHelper.MOB);
			stack.damageItem(1, player);
		}
		
		return EnumActionResult.SUCCESS;
	}
	
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
