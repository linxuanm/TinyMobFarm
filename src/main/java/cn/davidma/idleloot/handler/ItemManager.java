package cn.davidma.idleloot.handler;

import java.util.ArrayList;
import java.util.List;

import cn.davidma.idleloot.item.template.InteractiveMobTool;
import cn.davidma.idleloot.item.template.StandardItemBase;

public class ItemManager {
	
	public static List<StandardItemBase> items;
	
	public static void instantiateAllItems() {
		items = new ArrayList<StandardItemBase>();
		items.add(new Lasso("lasso"));
	}
}
