package cn.davidma.tinymobfarm.client.gui;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Reference.FARM_GUI) {
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileEntityMobFarm) {
				return new ContainerMobFarm(player.inventory, (TileEntityMobFarm) tileEntity);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Reference.FARM_GUI) {
			Object container = this.getServerGuiElement(ID, player, world, x, y, z);
			if (container instanceof Container) return new GuiMobFarm((Container) container);
		}
		return null;
	}
}
