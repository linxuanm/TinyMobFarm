package cn.davidma.tinymobfarm.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class RenderMobFarm extends TileEntityRenderer<TileEntityMobFarm> {

	public RenderMobFarm(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void func_225616_a_(TileEntityMobFarm tileEntityMobFarm, float partialTicks,
			MatrixStack matrix, IRenderTypeBuffer typeBuffer, int p_225616_5_, int p_225616_6_) {
		LivingEntity model = tileEntityMobFarm.getModel();
		if (model != null) {
			Float modelHorizontalAngle = -tileEntityMobFarm.getModelFacing().getHorizontalAngle();
			
			AxisAlignedBB box = model.getBoundingBox();
			double length = Math.max(
					Math.max(box.maxX - box.minX, box.maxY - box.minY), box.maxZ - box.minZ);
			float modelScale = (float) (0.5 / length);
			
			matrix.func_227860_a_();
			matrix.func_227861_a_(0.5, 0.125, 0.5);
			matrix.func_227862_a_(modelScale, modelScale, modelScale);
			
			//GlStateManager.rotatef(modelHorizontalAngle, 0, 1, 0);
			matrix.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(modelHorizontalAngle));
			
			Minecraft.getInstance().getRenderManager().func_229084_a_(model, 0, 0, 0, 0,
					partialTicks, matrix, typeBuffer, p_225616_5_);
			matrix.func_227865_b_();
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
