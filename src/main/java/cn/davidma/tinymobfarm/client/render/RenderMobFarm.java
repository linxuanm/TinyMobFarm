package cn.davidma.tinymobfarm.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3f;

public class RenderMobFarm extends TileEntityRenderer<TileEntityMobFarm> {

	public RenderMobFarm(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}
	
	@Override
	public void render(TileEntityMobFarm tileEntityMobFarm, float partialTicks, MatrixStack matrix,
			IRenderTypeBuffer typeBuffer, int combinedLight, int combinedOverlay) {
		
		LivingEntity model = tileEntityMobFarm.getModel();
		if (model != null) {
			Float modelHorizontalAngle = -tileEntityMobFarm.getModelFacing().getHorizontalAngle();
			
			AxisAlignedBB box = model.getBoundingBox();
			double length = Math.max(
					Math.max(box.maxX - box.minX, box.maxY - box.minY), box.maxZ - box.minZ);
			float modelScale = (float) (0.5 / length);
			
			matrix.push();
			matrix.translate(0.5, 0.125, 0.5);
			matrix.scale(modelScale, modelScale, modelScale);
			
			//GlStateManager.rotatef(modelHorizontalAngle, 0, 1, 0);
			matrix.rotate(Vector3f.YP.rotation(modelHorizontalAngle));
			
			Minecraft.getInstance().getRenderManager().renderEntityStatic(model, 0, 0, 0, 0,
					partialTicks, matrix, typeBuffer, combinedLight);
			matrix.pop();
		}
	}
	
	/*
	@Override
	public void render(TileEntityMobFarm tileEntityMobFarm, double x, double y, double z, float partialTicks, int destroyStage) {
		LivingEntity model = tileEntityMobFarm.getModel();
		if (model != null) {
			Float modelHorizontalAngle = -tileEntityMobFarm.getModelFacing().getHorizontalAngle();
			
			AxisAlignedBB box = model.getBoundingBox();
			double length = Math.max(Math.max(box.maxX - box.minX, box.maxY - box.minY), box.maxZ - box.minZ);
			double modelScale = 0.5 / length;
			
			GlStateManager.pushMatrix();
			GlStateManager.translated(x + 0.5, y + 0.125, z + 0.5);
			GlStateManager.scaled(modelScale, modelScale, modelScale);
			GlStateManager.rotatef(0, 0, 0, 0);
			GlStateManager.rotatef(modelHorizontalAngle, 0, 1, 0);
			Minecraft.getInstance().getRenderManager().renderEntity(model, 0, 0, 0, 0, partialTicks, false);
			GlStateManager.popMatrix();
		}
	}
	*/
}
