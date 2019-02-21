package cn.davidma.tinymobfarm.client.render;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.AxisAlignedBB;

public class RenderMobFarm extends TileEntityRenderer<TileEntityMobFarm> {

	@Override
	public void render(TileEntityMobFarm tileEntityMobFarm, double x, double y, double z, float partialTicks, int destroyStage) {
		EntityLiving model = tileEntityMobFarm.getModel();
		if (model != null) {
			Float modelHorizontalAngle = -tileEntityMobFarm.getModelFacing().getHorizontalAngle();
			
			AxisAlignedBB box = model.getBoundingBox();
			double length = Math.max(Math.max(box.maxX - box.minX, box.maxY - box.minY), box.maxZ - box.minZ);
			double modelScale = 0.5 / length;
			
			GlStateManager.pushMatrix();
			GlStateManager.translated(x + 0.5, y + 0.125, z + 0.5);
			GlStateManager.scaled(modelScale, modelScale, modelScale);
			GlStateManager.rotatef(modelHorizontalAngle, 0, 1, 0);
			Minecraft.getInstance().getRenderManager().renderEntity(model, 0, 0, 0, 0, partialTicks, false);
			GlStateManager.popMatrix();
		}
	}
}
