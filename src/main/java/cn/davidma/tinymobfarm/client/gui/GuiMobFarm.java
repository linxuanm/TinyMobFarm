package cn.davidma.tinymobfarm.client.gui;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiMobFarm extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/gui/farm_gui.png");
	
	private TileEntityMobFarm tileEntityMobFarm;
	
	public GuiMobFarm(InventoryPlayer playerInv, TileEntityMobFarm tileEntityMobFarm) {
		super(new ContainerMobFarm(playerInv, tileEntityMobFarm));
		this.tileEntityMobFarm = tileEntityMobFarm;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = I18n.format(this.tileEntityMobFarm.getUnlocalizedName());
		this.fontRenderer.drawString(name, (this.xSize - this.fontRenderer.getStringWidth(name)) / 2, 8, 4210752);
		
		if (this.tileEntityMobFarm.isWorking()) {
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(TEXTURE);
			this.drawTexturedModalRect(48, 60, 176, 5, 80, 5);
		} else {
			String error;
			if (this.tileEntityMobFarm.getLasso().isEmpty()) error = "tinymobfarm.gui.no_lasso";
			else if (this.tileEntityMobFarm.isPowered()) error = "tinymobfarm.gui.redstone_disable";
			else error = "tinymobfarm.gui.higher_tier";
			error = I18n.format(error);
			this.fontRenderer.drawString(error, (this.xSize - this.fontRenderer.getStringWidth(error)) / 2, 59, 16733525);
		}
	}
}
