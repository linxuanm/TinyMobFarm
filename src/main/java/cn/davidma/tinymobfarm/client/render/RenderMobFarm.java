package cn.davidma.tinymobfarm.client.render;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.EntityLiving;

public class RenderMobFarm extends TileEntityRenderer<TileEntityMobFarm> {

	@Override
	public void render(TileEntityMobFarm tileEntityMobFarm, double x, double y, double z, float partialTicks, int destroyStage) {
		EntityLiving model = tileEntityMobFarm.getModel();
		if (model != null) {
			GlStateManager.pushMatrix();
			Minecraft.getInstance().getRenderManager().renderEntity(model, x, y, z, 0, partialTicks, false);
			GlStateManager.popMatrix();
			System.out.println(123);
		}
	}
}
