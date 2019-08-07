package cn.davidma.tinymobfarm.client;

import cn.davidma.tinymobfarm.client.gui.GuiMobFarm;
import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Reference.MOD_ID, value = {Dist.CLIENT})
public class ClientEventHandler {
	
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> {
			return (openContainer) -> {
				ResourceLocation location = openContainer.getId();
				if (location.toString().equals(Reference.MOD_ID + ":mob_farm_gui")) {
					EntityPlayerSP player = Minecraft.getInstance().player;
					BlockPos pos = openContainer.getAdditionalData().readBlockPos();
					TileEntity tileEntity = player.world.getTileEntity(pos);
					if (tileEntity instanceof TileEntityMobFarm) {
						return new GuiMobFarm(player.inventory, (TileEntityMobFarm) tileEntity);
					}
				}
				return null;
			};
		});
	}
}
