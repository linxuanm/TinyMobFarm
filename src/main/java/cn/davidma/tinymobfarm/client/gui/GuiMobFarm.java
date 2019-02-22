package cn.davidma.tinymobfarm.client.gui;

import java.util.ArrayList;
import java.util.List;

import cn.davidma.tinymobfarm.common.tileentity.MobFarmTileEntity;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiMobFarm extends GuiContainer{

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID+":textures/gui/farm_gui.png");
	private InventoryPlayer player;
	private MobFarmTileEntity tileEntity;
	
	public GuiMobFarm(InventoryPlayer player, MobFarmTileEntity tileEntity) {
		super(new ContainerMobFarm(player, tileEntity));
		this.player = player;
		this.tileEntity = tileEntity;
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = this.tileEntity.getName();
		this.fontRenderer.drawString(name, xSize/2 - this.fontRenderer.getStringWidth(name)/2, 8, 4210752);
		if (this.tileEntity.working()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(TEXTURE);
			this.drawTexturedModalRect(48, 60, 176, 5, 80, 5);
			this.drawTexturedModalRect(48, 60, 176, 0, progressScale(80), 5);
		} else {
			String text;
			if (this.tileEntity.hasLasso()) {
				if (this.tileEntity.hasHostileMob() && this.tileEntity.getId() < Reference.LOWEST_ID_FOR_HOSTILE_SPAWNING) {
					text = I18n.format("gui.higher_tier.key");
				} else {
					text = I18n.format("gui.redstone_disable.key");
				}
			} else {
				text = I18n.format("gui.no_lasso.key");
			}
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
		this.drawTip(mouseX, mouseY);
	}
	
	private void drawTip(int mouseX, int mouseY) {
		int btnX = this.guiLeft + 2, btnY = this.guiTop + 2, width = 8, height = 8;
		boolean active = mouseX > btnX && mouseY > btnY && mouseX <= btnX+width && mouseY <= btnY+height;
		if (active) {
			// Nope, no list.addAll()
			List<String> info = new ArrayList<String>();
			
			String mobName = this.tileEntity.getMobName();
			if (mobName != null && !mobName.isEmpty()) {
				info.add(I18n.format("tip.mob_name.key", mobName));
				info.add("");
			}
			info.add(I18n.format("tip.redstone.key"));
			info.add("");
			info.add(I18n.format("tip.eject_par1.key"));
			info.add(I18n.format("tip.eject_par2.key"));
			int predictX, maxLen = 0;
			for (String i: info) {
				int textWidth = this.fontRenderer.getStringWidth(i);
				if (textWidth > maxLen) maxLen = textWidth;
			}
			predictX = this.guiLeft - (maxLen + 17);
			this.drawHoveringText(info, predictX, this.getGuiTop(), this.fontRenderer);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	private int progressScale(int pixels) {
		int total = this.tileEntity.getTotalProgress();
		if (total == 0) return 0;
		return this.tileEntity.getCurrProgress() * pixels / total;
	}

}
