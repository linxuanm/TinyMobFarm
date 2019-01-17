package cn.davidma.idleloot.gui;

import cn.davidma.idleloot.block.container.GeneratorContainer;
import cn.davidma.idleloot.reference.Info;
import cn.davidma.idleloot.tileentity.GeneratorTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GeneratorGUI extends GuiContainer{

	private static final ResourceLocation TEXTURE = new ResourceLocation(Info.MOD_ID+":textures/gui/generator_gui.png");
	private InventoryPlayer player;
	private GeneratorTileEntity tileEntity;
	
	public GeneratorGUI(InventoryPlayer player, GeneratorTileEntity tileEntity) {
		super(new GeneratorContainer(player, tileEntity));
		this.player = player;
		this.tileEntity = tileEntity;
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = this.tileEntity.getDisplayName().getFormattedText();
		this.fontRenderer.drawString(name, xSize/2 - this.fontRenderer.getStringWidth(name)/2, 8, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		if (this.tileEntity.working()) {
			this.drawTexturedModalRect(this.guiLeft + 48, this.guiTop + 60, 176, 0, progressScale(80), 5);
		}
	}
	
	private int progressScale(int pixels) {
		int total = this.tileEntity.getField(1);
		if (total == 0) return 0;
		return this.tileEntity.getField(0) * pixels / total;
	}

}
