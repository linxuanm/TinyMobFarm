package cn.davidma.tinymobfarm.client;

import cn.davidma.tinymobfarm.client.gui.GuiMobFarm;
import cn.davidma.tinymobfarm.client.render.RenderMobFarm;
import cn.davidma.tinymobfarm.common.TinyMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Reference.MOD_ID, value = {Dist.CLIENT}, bus = Bus.MOD)
public class ClientEventHandler {
	
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(TinyMobFarm.containerTypeMobFarm, GuiMobFarm::new);
		ClientRegistry.bindTileEntityRenderer(TinyMobFarm.tileEntityMobFarm, RenderMobFarm::new);
		
		TinyMobFarm.mobFarms.forEach(
				farm -> RenderTypeLookup.setRenderLayer(farm, RenderType.getCutout()));
	}
}
