package cn.davidma.tinymobfarm.client.gui;

import java.util.ArrayList;
import java.util.List;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import cn.davidma.tinymobfarm.core.util.NBTHelper;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiMobFarm extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/gui/farm_gui.png");
	
	private TileEntityMobFarm tileEntityMobFarm;
	
	public GuiMobFarm(InventoryPlayer playerInv, TileEntityMobFarm tileEntityMobFarm) {
		super(new ContainerMobFarm(playerInv, tileEntityMobFarm));
		this.tileEntityMobFarm = tileEntityMobFarm;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		this.drawTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = I18n.format(this.tileEntityMobFarm.getUnlocalizedName());
		this.fontRenderer.drawString(name, (this.xSize - this.fontRenderer.getStringWidth(name)) / 2, 8, 4210752);
		
		if (this.tileEntityMobFarm.isWorking()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(TEXTURE);
			this.drawTexturedModalRect(48, 60, 176, 5, 80, 5);
			this.drawTexturedModalRect(48, 60, 176, 0, (int) (this.tileEntityMobFarm.getScaledProgress() * 80), 5);
		} else {
			String error;
			if (this.tileEntityMobFarm.getLasso().isEmpty()) error = "tinymobfarm.gui.no_lasso";
			else if (this.tileEntityMobFarm.isPowered()) error = "tinymobfarm.gui.redstone_disable";
			else error = "tinymobfarm.gui.higher_tier";
			error = I18n.format(error);
			this.fontRenderer.drawString(error, (this.xSize - this.fontRenderer.getStringWidth(error)) / 2, 59, 16733525);
		}
	}
	
	private void drawTip(int mouseX, int mouseY) {
		int btnX = this.guiLeft + 2, btnY = this.guiTop + 2;
		int btnWidth = 8, btnHeight = 8;
		
		if (mouseX > btnX && mouseY > btnY && mouseX < btnX + btnWidth && mouseY < btnY + btnHeight) {
			List<String> info = new ArrayList<String>();
			
			info.add(I18n.format("tinymobfarm.tip.redstone"));
			info.add("");
			info.add(I18n.format("tinymobfarm.tip.output_part_1"));
			info.add(I18n.format("tinymobfarm.tip.output_part_2"));
			
			
			this.drawHoveringText(info, mouseX, mouseY);
		}
	}
}
