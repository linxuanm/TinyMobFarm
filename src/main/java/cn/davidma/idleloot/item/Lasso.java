package cn.davidma.idleloot.item;

import java.util.List;

import cn.davidma.idleloot.item.template.InteractiveMobTool;
import cn.davidma.idleloot.reference.IdleLootConfig;
import cn.davidma.idleloot.util.Msg;
import cn.davidma.idleloot.util.NBTTagHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
		
		// Mob Info.
		double mobHealth = (double) Math.round(mob.getHealth() * 10) / 10;
		nbt.setTag("entityInfo", entityNBT);
		nbt.setDouble("mobHealth", mobHealth);
		nbt.setDouble("mobMaxHealth", mob.getMaxHealth());
		nbt.setString("mobName", mob.getName());
		
		// State change.
		nbt.setBoolean("shiny", true);
		nbt.setBoolean("containsMob", true);
		
		// Finish him!
		mob.setDead();
		
		NBTTagHelper.setEssentialNBT(stack, nbt);
		
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
		NBTTagCompound entityNBT = nbt.getCompoundTag("entityInfo");
		
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
			
			tooltip.add("Mob: " + mobName);
			tooltip.add("Health: " + mobHealth + "/" + mobMaxHealth);
		}
	}
}