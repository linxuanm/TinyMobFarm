package cn.davidma.idleloot.handler;

import cn.davidma.idleloot.block.container.GeneratorContainer;
import cn.davidma.idleloot.gui.GeneratorGUI;
import cn.davidma.idleloot.reference.Info;
import cn.davidma.idleloot.tileentity.GeneratorTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Info.GENERATOR_GUI) return new GeneratorContainer(player.inventory, (GeneratorTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Info.GENERATOR_GUI) return new GeneratorGUI(player.inventory, (GeneratorTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}
}
