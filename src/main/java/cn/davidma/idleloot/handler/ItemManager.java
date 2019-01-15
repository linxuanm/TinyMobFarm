package cn.davidma.idleloot.handler;

import java.util.ArrayList;
import java.util.List;

import cn.davidma.idleloot.item.Lasso;
import cn.davidma.idleloot.item.template.InteractiveMobTool;
import cn.davidma.idleloot.item.template.StandardItemBase;
import net.minecraft.item.Item;

public class ItemManager {
	
	public static List<Item> items = new ArrayList<Item>();
	
	public static void instantiateAllItems() {
		new Lasso("lasso");
	}
}
