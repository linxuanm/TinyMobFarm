package cn.davidma.tinymobfarm.client;

import cn.davidma.tinymobfarm.client.render.RenderMobFarm;
import cn.davidma.tinymobfarm.common.CommonProxy;
import cn.davidma.tinymobfarm.common.tileentity.MobFarmTileEntity;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	public void registerTESR() {
		ClientRegistry.bindTileEntitySpecialRenderer(MobFarmTileEntity.class, new RenderMobFarm());
	}
}
