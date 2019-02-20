package cn.davidma.tinymobfarm.client;

import cn.davidma.tinymobfarm.client.render.RenderMobFarm;
import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.IProxy;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements IProxy {

	@Override
	public void setup() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMobFarm.class, new RenderMobFarm());
	}
}
