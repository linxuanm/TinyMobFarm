package cn.davidma.tinymobfarm.tesr;

import org.lwjgl.opengl.GL11;

import cn.davidma.tinymobfarm.reference.TinyMobFarmConfig;
import cn.davidma.tinymobfarm.tileentity.MobFarmTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class MobFarmTESR extends TileEntitySpecialRenderer<MobFarmTileEntity> {
	
	@Override
	public void render(MobFarmTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		if (TinyMobFarmConfig.DISABLE_MOB_MODEL) return;
		
		World world = te.getWorld();
		EntityLiving mob = te.getMob();
		if (mob == null) return;
		AxisAlignedBB box = mob.getEntityBoundingBox();
		double maxLen = Math.max(Math.max(box.maxX - box.minX, box.maxY - box.minY), box.maxZ - box.minZ);
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y + 0.125, z + 0.5);
		double scale = TinyMobFarmConfig.MOB_MODEL_SCALE;
		GL11.glScaled(scale / maxLen, scale / maxLen, scale / maxLen);
		GL11.glRotated(90, 0, 1, 0);
		
		Minecraft.getMinecraft().getRenderManager().renderEntity(mob, 0F, 0F, 0F, 0F, 0, false);
		
		GL11.glPopMatrix();
	}
}
