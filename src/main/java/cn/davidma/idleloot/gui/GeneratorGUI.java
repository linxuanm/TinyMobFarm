package cn.davidma.idleloot.gui;

import cn.davidma.idleloot.reference.Info;
import cn.davidma.idleloot.tileentity.GeneratorTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GeneratorGUI extends GuiContainer{

	private static final ResourceLocation TEXTURE = new ResourceLocation(Info.MOD_ID + ":textures/gui/generatorGUI.png");
	private InventoryPlayer player;
	private GeneratorTileEntity tileEntity;
	
	public GeneratorGUI(InventoryPlayer player, GeneratorTileEntity tileEntity) {
		super(new );
		this.player = player;
		this.tileEntity = tileEntity;
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		
	}
}
