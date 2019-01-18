package cn.davidma.tinymobfarm.handler;

import cn.davidma.tinymobfarm.block.container.MobFarmContainer;
import cn.davidma.tinymobfarm.gui.MobFarmGUI;
import cn.davidma.tinymobfarm.reference.Info;
import cn.davidma.tinymobfarm.tileentity.MobFarmTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Info.FARM_GUI) return new MobFarmContainer(player.inventory, (MobFarmTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Info.FARM_GUI) return new MobFarmGUI(player.inventory, (MobFarmTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}
}
