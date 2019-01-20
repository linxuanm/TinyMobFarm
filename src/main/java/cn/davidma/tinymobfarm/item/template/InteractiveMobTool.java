package cn.davidma.tinymobfarm.item.template;

import cn.davidma.tinymobfarm.reference.TinyMobFarmConfig;
import cn.davidma.tinymobfarm.util.Msg;
import cn.davidma.tinymobfarm.util.NBTTagHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public abstract class InteractiveMobTool extends StandardItemBase {

	protected abstract String[] verb(); // [0]: present tense; [1]: past tense.
	protected abstract boolean interactEntity(ItemStack stack, EntityPlayer player, EntityLivingBase mob);
	protected abstract boolean interactBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side);
	
	protected boolean shiny;
	private boolean[] damageItem;
	
	public InteractiveMobTool(String name, int maxDamage, boolean[] damageItem) {
		super(name);
		shiny = false;
		setMaxStackSize(1);
		setMaxDamage(maxDamage);
		this.damageItem = damageItem; // [0]: mob; [1]: block.
	}
	
	@Override
	public boolean hasEffect(ItemStack item) {
		NBTTagCompound nbt = NBTTagHelper.getEssentialNBT(item);
		return NBTTagHelper.keyValueEquals(nbt, "shiny", true);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase mob, EnumHand hand) {
		
		// Check.
		if (mob.world.isRemote) return true;
		
		if (!mob.isEntityAlive()) {
			
			// Mob is dying.
			Msg.tellPlayer(player, "Cannot " + verb()[0] + " a dying mob.");
			return false;
		}
		
		if (!mob.isNonBoss()) {
			
			// Mob is a boss.
			Msg.tellPlayer(player, "Only non-boss mobs can be " + verb()[1] + ".");
			return false;
		}
		
		for (String i: TinyMobFarmConfig.MOB_BLACKLIST) {
			if (mob.getName().toLowerCase().equals(i.toLowerCase())) {
				Msg.tellPlayer(player, "This mob is blacklisted.");
				return false;
			}
		}
		
		boolean result = this.interactEntity(stack, player, mob);
		if (result && damageItem[0]) {
			stack.damageItem(1, player);
		}
		return result;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float x, float y, float z) {
		
		// Check.
		if (world.isRemote) return EnumActionResult.SUCCESS;
		
		// Get ItemStack.
		ItemStack stack = player.inventory.getCurrentItem();
		
		// Check again.
		if (!player.canPlayerEdit(pos.offset(side), side, stack)) return EnumActionResult.FAIL;
		
		boolean result = this.interactBlock(stack, player, world, pos, side);
		if (result && damageItem[1]) {
			stack.damageItem(1, player);
		}
		return result ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
	}
	
}
