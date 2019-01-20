package cn.davidma.tinymobfarm.item.template;

import java.util.List;

import cn.davidma.tinymobfarm.reference.Info;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class TooltipItemBlock extends ItemBlock {

	private int id;
	
	public TooltipItemBlock(Block block, int id) {
		super(block);
		this.id = id;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		
		// Dynamic tooltip.
		if (GuiScreen.isShiftKeyDown()) {
			for (String i: Info.TOOLTIP_FROM_FARM_ID(this.id)) {
				tooltip.add(i);
			}
		} else {
			tooltip.add(I18n.format("tooltip.shift.key", TextFormatting.ITALIC, TextFormatting.RESET));
		}
	}
}
