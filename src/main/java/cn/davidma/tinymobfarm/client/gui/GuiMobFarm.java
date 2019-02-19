package cn.davidma.tinymobfarm.client.gui;

import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiMobFarm extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/gui/farm_gui.png");
	
	public GuiMobFarm(Container inventorySlots) {
		super(inventorySlots);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}
