package cn.davidma.tinymobfarm.item;

import java.util.List;
import java.util.Random;

import cn.davidma.tinymobfarm.item.template.InteractiveMobTool;
import cn.davidma.tinymobfarm.reference.TinyMobFarmConfig;
import cn.davidma.tinymobfarm.util.LootTableHelper;
import cn.davidma.tinymobfarm.util.Msg;
import cn.davidma.tinymobfarm.util.NBTTagHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public class Lasso extends InteractiveMobTool {

	public Lasso(String name) {
		super(name, TinyMobFarmConfig.LASSO_DURABILITY, new boolean[] {false, true});
	}

	@Override
	protected String[] verb() {
		return new String[] {I18n.format("join.capture.key"), I18n.format("join.captured.key")};
	}

	@Override
	protected boolean interactEntity(ItemStack stack, EntityPlayer player, EntityLivingBase mob) {
		
		if (!(mob instanceof EntityLiving)) {
			Msg.tellPlayer(player, I18n.format("error.cannot.key"));
			return false;
		}
		
		//NBT time!
		NBTTagCompound nbt = NBTTagHelper.getEssentialNBT(stack);
		
		// Already full.
		if (NBTTagHelper.containsMob(nbt)) return false;
		
		// Instantiates entity NBT tag.
		NBTTagCompound entityNBT = new NBTTagCompound();
		if (!mob.writeToNBTAtomically(entityNBT)) {
			Msg.tellPlayer(player, I18n.format("error.cannot.key"));
			return false;
		}
		
		// Mob loot.
		ResourceLocation location = LootTableHelper.getLootTableLocation((EntityLiving) mob);
		if (location == null) {
			Msg.tellPlayer(player, I18n.format("error.cannot.key"));
			return false;
		}
		nbt.setString(NBTTagHelper.LOOT_TABLE_LOCATION, location.toString());
		
		// Mob info.
		double mobHealth = (double) Math.round(mob.getHealth() * 10) / 10;
		nbt.setTag(NBTTagHelper.ENTITY_INFO, entityNBT);
		nbt.setDouble(NBTTagHelper.MOB_HEALTH, mobHealth);
		nbt.setDouble(NBTTagHelper.MOB_MAX_HEALTH, mob.getMaxHealth());
		nbt.setString(NBTTagHelper.MOB_NAME, mob.getName());
		nbt.setBoolean(NBTTagHelper.IS_HOSTILE, mob.isCreatureType(EnumCreatureType.MONSTER, false));
		
		// State change.
		nbt.setBoolean(NBTTagHelper.SHINY, true);
		nbt.setBoolean(NBTTagHelper.CONTAINS_MOB, true);
		
		// Finish him!
		mob.setDead();
		
		NBTTagHelper.setEssentialNBT(stack, nbt);
		
		if (player.isCreative()) {
			ItemStack newLasso = new ItemStack(this);
			NBTTagHelper.setEssentialNBT(newLasso, nbt);
			player.addItemStackToInventory(newLasso);
		}
		
		player.inventory.markDirty();
		return true;
	}

	@Override
	protected boolean interactBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
		
		// More NBT Tags!
		NBTTagCompound nbt = NBTTagHelper.getEssentialNBT(stack);
		
		// Check yet AGAIN!
		if (!NBTTagHelper.containsMob(nbt)) return false;
		
		// Don't question my naming convention.
		IBlockState bs = world.getBlockState(pos);
		NBTTagCompound entityNBT = nbt.getCompoundTag(NBTTagHelper.ENTITY_INFO);
		
		// General entity info.
		NBTTagList tags = new NBTTagList();
		BlockPos newPos = pos.offset(side);
		
		// Add 0.5 so that the mob is centered.
		tags.appendTag(new NBTTagDouble(newPos.getX() + 0.5)); // X
		tags.appendTag(new NBTTagDouble(newPos.getY())); // Z
		tags.appendTag(new NBTTagDouble(newPos.getZ() + 0.5)); // Y
		
		entityNBT.setInteger("Dimension", world.provider.getDimension());
		entityNBT.setTag("Pos", tags);
		
		// Spawns the mob.
		Entity mob = EntityList.createEntityFromNBT(entityNBT, world);
		if (mob != null) world.spawnEntity(mob);
		
		// Final changes.
		NBTTagCompound base = stack.getTagCompound();
		base.removeTag(NBTTagHelper.BASE);
		stack.setTagCompound(base);
		
		player.inventory.markDirty();
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound nbt = NBTTagHelper.getEssentialNBT(stack);
		if (NBTTagHelper.containsMob(nbt)) {
			String mobName = nbt.getString("mobName");
			double mobHealth = nbt.getDouble("mobHealth");
			double mobMaxHealth = nbt.getDouble("mobMaxHealth");
			
			tooltip.add(I18n.format("tooltip.release_mob.key"));
			tooltip.add(I18n.format("tooltip.mob_name.key", mobName));
			tooltip.add(I18n.format("tooltip.health.key", mobHealth, mobMaxHealth));
			if (NBTTagHelper.isHostile(nbt)) tooltip.add(I18n.format("tooltip.hostile.key"));
		} else {
			tooltip.add(I18n.format("tooltip.capture.key"));
		}
	}
}