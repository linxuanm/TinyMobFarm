package cn.davidma.tinymobfarm.client.gui;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class GuiHandler {

	public static GuiScreen openGui(FMLPlayMessages.OpenContainer openContainer) {
		BlockPos pos = openContainer.getAdditionalData().readBlockPos();
		EntityPlayer player = Minecraft.getInstance().player;
		TileEntity tileEntity = player.world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityMobFarm) {
			return new GuiMobFarm(new ContainerMobFarm(player.inventory, (TileEntityMobFarm) tileEntity));
		}
		return null;
	}
}
