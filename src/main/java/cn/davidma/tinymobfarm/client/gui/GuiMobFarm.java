package cn.davidma.tinymobfarm.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiMobFarm extends ContainerScreen<ContainerMobFarm> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/gui/farm_gui.png");
	
	private TileEntityMobFarm tileEntityMobFarm;
	
	public GuiMobFarm(ContainerMobFarm container, PlayerInventory inv, ITextComponent text) {
		super(container, inv, text);
		this.tileEntityMobFarm = container.getCachedFarm();
	}
	
	// render
	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		// renderBackground
		this.renderBackground(matrix);
		
		super.render(matrix, mouseX, mouseY, partialTicks);
		
		// renderHoveredToolTip
		this.renderTooltip(matrix, mouseX, mouseY);
	}
	
	/*@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = I18n.format(this.tileEntityMobFarm.getUnlocalizedName());
		this.font.drawString(name, (this.xSize - this.font.getStringWidth(name)) / 2, 8, 4210752);
		
		if (this.tileEntityMobFarm.isWorking()) {
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.minecraft.getTextureManager().bindTexture(TEXTURE);
			this.blit(48, 60, 176, 5, 80, 5);
			this.blit(48, 60, 176, 0, (int) (this.tileEntityMobFarm.getScaledProgress() * 80), 5);
		} else {
			String error;
			if (this.tileEntityMobFarm.getLasso().isEmpty()) error = "tinymobfarm.gui.no_lasso";
			else if (this.tileEntityMobFarm.isPowered()) error = "tinymobfarm.gui.redstone_disable";
			else error = "tinymobfarm.gui.higher_tier";
			error = I18n.format(error);
			this.font.drawString(error, (this.xSize - this.font.getStringWidth(error)) / 2, 59, 16733525);
		}
	}*/
	
	// drawGuiContainerForegroundLayer
	@Override
	protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY) {
		String name = I18n.get(this.tileEntityMobFarm.getUnlocalizedName());
		
		// field_230712_o_ == font
		this.font.draw(matrix, name, (this.imageWidth - this.font.width(name)) / 2, 8, 4210752);
		
		if (this.tileEntityMobFarm.isWorking()) {
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.minecraft.getTextureManager().bind(TEXTURE);
			this.blit(matrix, 48, 60, 176, 5, 80, 5);
			this.blit(matrix, 48, 60, 176, 0, (int) (this.tileEntityMobFarm.getScaledProgress() * 80), 5);
		} else {
			String error;
			if (this.tileEntityMobFarm.getLasso().isEmpty()) error = "tinymobfarm.gui.no_lasso";
			else if (this.tileEntityMobFarm.isPowered()) error = "tinymobfarm.gui.redstone_disable";
			else error = "tinymobfarm.gui.higher_tier";
			error = I18n.get(error);
			this.font.draw(matrix, error, (this.imageWidth - this.font.width(error)) / 2, 59, 16733525);
		}
		
		//super.func_230451_b_(matrix, mouseX, mouseY);
	}
	
	// drawGuiContainerBackgroundLayer
	@Override
	protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {	
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		// Minecraft#getTextureManager
		this.minecraft.getTextureManager().bind(TEXTURE);
		
		// AbstractGui#blit
		this.blit(matrix, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}
}
