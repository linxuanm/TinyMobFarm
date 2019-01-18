package cn.davidma.tinymobfarm.gui;

import cn.davidma.tinymobfarm.block.container.MobFarmContainer;
import cn.davidma.tinymobfarm.reference.Info;
import cn.davidma.tinymobfarm.tileentity.MobFarmTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class MobFarmGUI extends GuiContainer{

	private static final ResourceLocation TEXTURE = new ResourceLocation(Info.MOD_ID+":textures/gui/generator_gui.png");
	private InventoryPlayer player;
	private MobFarmTileEntity tileEntity;
	
	public MobFarmGUI(InventoryPlayer player, MobFarmTileEntity tileEntity) {
		super(new MobFarmContainer(player, tileEntity));
		this.player = player;
		this.tileEntity = tileEntity;
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = this.tileEntity.getDisplayName().getFormattedText();
		this.fontRenderer.drawString(name, xSize/2 - this.fontRenderer.getStringWidth(name)/2, 8, 4210752);
		if (this.tileEntity.working()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(TEXTURE);
			this.drawTexturedModalRect(48, 60, 176, 5, 80, 5);
			this.drawTexturedModalRect(48, 60, 176, 0, progressScale(80), 5);
		} else {
			String text = "Insert a lasso to activate";
			int x = xSize / 2 - this.fontRenderer.getStringWidth(text) / 2;
			int y = 60;
			this.fontRenderer.drawString(text, x, 59, 16733525);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	private int progressScale(int pixels) {
		int total = this.tileEntity.getField(1);
		if (total == 0) return 0;
		return this.tileEntity.getField(0) * pixels / total;
	}

}
