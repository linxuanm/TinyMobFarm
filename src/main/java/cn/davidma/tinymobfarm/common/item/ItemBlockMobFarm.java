package cn.davidma.tinymobfarm.common.item;

import java.util.List;

import cn.davidma.tinymobfarm.common.block.BlockMobFarm;
import cn.davidma.tinymobfarm.core.util.Msg;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemBlockMobFarm extends ItemBlock {

	private Consumer<List<ITextComponent>> tooltipBuilder;
	
	public ItemBlockMobFarm(BlockMobFarm block, Properties builder) {
		super(block, builder);
		this.tooltipBuilder = block.getTooltipBuilder();
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (GuiScreen.isShiftKeyDown()) this.tooltipBuilder.accept(tooltip);
		else tooltip.add(Msg.tooltip("tinymobfarm.tooltip.hold_shift", TextFormatting.ITALIC, TextFormatting.GRAY));
		
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
