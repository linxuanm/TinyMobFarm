package cn.davidma.tinymobfarm.tesr;

import org.lwjgl.opengl.GL11;

import cn.davidma.tinymobfarm.tileentity.MobFarmTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MobFarmTESR extends TileEntitySpecialRenderer<MobFarmTileEntity> {
	
	EntityItem stackEntity = null;
	int currTime = 0;
	
	@Override
	public void render(MobFarmTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack stack = new ItemStack(Items.DIAMOND);
		
		if (stack == null) return;
		World world = Minecraft.getMinecraft().world;
		stackEntity = new EntityItem(world, 0D, 0D, 0D, stack);
		
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y + 0.25, z + 0.5);
		GL11.glRotated(90, 0, 1, 0);
		
		Minecraft.getMinecraft().getRenderManager().renderEntity(new EntityZombie(world), 0F, 0F, 0F, 0F, 0, false);
		// Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
		
		GL11.glPopMatrix();
	}
}
