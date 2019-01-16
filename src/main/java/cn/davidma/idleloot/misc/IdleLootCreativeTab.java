package cn.davidma.idleloot.misc;

import cn.davidma.idleloot.handler.CollectionsManager;
import cn.davidma.idleloot.item.template.StandardItemBase;
import cn.davidma.idleloot.reference.Info;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IdleLootCreativeTab extends CreativeTabs {

	public IdleLootCreativeTab() {
		super(Info.MOD_ID);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getTabIconItem() {
		for (Item i: CollectionsManager.items) {
			System.out.println(i.getUnlocalizedName());
			if (i.getUnlocalizedName().equals("tile.iron_generator")) return new ItemStack(i);
		}
		return new ItemStack(Items.ROTTEN_FLESH);
	}
}
