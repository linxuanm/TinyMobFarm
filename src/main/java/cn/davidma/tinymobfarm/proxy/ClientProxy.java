package cn.davidma.tinymobfarm.proxy;

import cn.davidma.tinymobfarm.tesr.MobFarmTESR;
import cn.davidma.tinymobfarm.tileentity.MobFarmTileEntity;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	public void registerTESR() {
		ClientRegistry.bindTileEntitySpecialRenderer(MobFarmTileEntity.class, new MobFarmTESR());
	}
}
