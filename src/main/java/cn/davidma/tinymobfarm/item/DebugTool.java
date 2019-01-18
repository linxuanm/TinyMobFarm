package cn.davidma.tinymobfarm.item;

import cn.davidma.tinymobfarm.item.template.InteractiveMobTool;
import cn.davidma.tinymobfarm.util.LootTableHelper;
import cn.davidma.tinymobfarm.util.Msg;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
		if (mob instanceof EntityLiving) {
			ResourceLocation loc = LootTableHelper.getLootTable((EntityLiving) mob);
			Msg.tellPlayer(player, loc.toString());
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
}
