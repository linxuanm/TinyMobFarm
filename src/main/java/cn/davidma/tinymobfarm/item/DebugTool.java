package cn.davidma.tinymobfarm.item;

import java.util.List;
import java.util.Random;

import cn.davidma.tinymobfarm.item.template.InteractiveMobTool;
import cn.davidma.tinymobfarm.util.LootTableHelper;
import cn.davidma.tinymobfarm.util.Msg;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public class DebugTool extends InteractiveMobTool {

	public DebugTool(String name) {
		super(name, -1, new boolean[] {false, false});
	}

	@Override
	protected String[] verb() {
		return new String[] {"scan", "scanned"};
	}

	@Override
	protected boolean interactEntity(ItemStack stack, EntityPlayer player, EntityLivingBase mob) {
		if (player.isSneaking()) {
			mob.attackEntityFrom(DamageSource.causePlayerDamage(player), 32768); // Insta-kill.
			return true;
		}
		if (mob instanceof EntityLiving) {
			ResourceLocation location = LootTableHelper.getLootTableLocation((EntityLiving) mob);
			if (location == null) {
				Msg.tellPlayer(player, "Cannot retrieve loot table location for this mob.");
				return false;
			}
			List<ItemStack> loots = LootTableHelper.genLoots(location, mob.world);
			if (loots == null) {
				Msg.tellPlayer(player, "Cannot generate possible loots for this mob.");
				return false;
			}
			if (loots.isEmpty()) {
				Msg.tellPlayer(player, "Nothing dropped");
			} else {
				for (ItemStack lootStack: loots) {
					String info = String.format("%d x %s", lootStack.getCount(), lootStack.getDisplayName());
					int meta = lootStack.getMetadata();
					if (meta != 0) info += String.format(", Metadata: %d", meta);
					Msg.tellPlayer(player, info);
				}
			}
		}
		return true;
	}

	@Override
	protected boolean interactBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Right click on mob to see its drops.");
		tooltip.add("(Simulated probability)");
	}
}
