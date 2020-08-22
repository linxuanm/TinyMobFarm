package cn.davidma.tinymobfarm.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.davidma.tinymobfarm.core.util.Msg;
import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;;

public enum EnumMobFarm {

	WOOD("wood_farm", Blocks.OAK_WOOD, false, new int[] {2, 3, 3}),
	STONE("stone_farm", Blocks.STONE, false, new int[] {1, 2, 3}),
	IRON("iron_farm", Blocks.IRON_BLOCK, true, new int[] {1, 2}),
	GOLD("gold_farm", Blocks.GOLD_BLOCK, true, new int[] {1, 1, 2}),
	DIAMOND("diamond_farm", Blocks.DIAMOND_BLOCK, true, new int[] {1}),
	EMERALD("emerald_farm", Blocks.EMERALD_BLOCK, true, new int[] {0, 1, 1}),
	INFERNAL("inferno_farm", Blocks.OBSIDIAN, true, new int[] {0, 0, 1}),
	ULTIMATE("ultimate_farm", Blocks.OBSIDIAN, true, new int[] {0});
	
	private static final double[] MOB_FARM_SPEED = {50.0, 40.0, 30.0, 20.0, 10.0, 5.0, 2.5, 0.5};
	
	private String registryName;
	private Block baseBlock;
	private boolean canFarmHostile;
	private int[] damageChance;
	private Map<Integer, Integer> normalizedChance;
	
	private EnumMobFarm(String registryName, Block baseBlock, boolean canFarmHostile, int[] damageChance) {
		this.registryName = registryName;
		this.baseBlock = baseBlock;
		this.canFarmHostile = canFarmHostile;
		this.damageChance = damageChance;
		
		this.normalizedChance = new HashMap<Integer, Integer>();
		for (int i: this.damageChance) {
			if (!this.normalizedChance.containsKey(i)) this.normalizedChance.put(i, 0);
			this.normalizedChance.put(i, this.normalizedChance.get(i) + 1);
		}
		int denominator = this.damageChance.length;
		for (int i: this.normalizedChance.keySet()) {
			this.normalizedChance.put(i, (int) (this.normalizedChance.get(i) * 100.0 / denominator));
		}
	}
	
	public String getRegistryName() {
		return this.registryName;
	}
	
	public String getUnlocalizedName() {
		return String.format("block.%s.%s", Reference.MOD_ID, this.registryName);
	}
	
	public Block getBaseBlock() {
		return this.baseBlock;
	}
	
	public boolean isLassoValid(ItemStack lasso) {
		return NBTHelper.hasMob(lasso) && (this.canFarmHostile || !NBTHelper.hasHostileMob(lasso));
	}
	
	public int getMaxProgress() {
		return (int) (MOB_FARM_SPEED[this.ordinal()] * 20);
	}
	
	public int getRandomDamage(Random rand) {
		return this.damageChance[rand.nextInt(this.damageChance.length)];
	}
	
	public void addTooltip(List<ITextComponent> tooltip) {
		if (!this.canFarmHostile) {
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.no_hostile").func_230530_a_(Style.field_240709_b_.func_240712_a_(TextFormatting.RED)));
		}
		tooltip.add(Msg.tooltip("tinymobfarm.tooltip.farm_rate", MOB_FARM_SPEED[this.ordinal()]));
		tooltip.add(Msg.tooltip("tinymobfarm.tooltip.durability_info"));
		for (int i: this.normalizedChance.keySet()) {
			if (i == 0) tooltip.add(Msg.tooltip("tinymobfarm.tooltip.no_durability", this.normalizedChance.get(i)));
			else tooltip.add(Msg.tooltip("tinymobfarm.tooltip.default_durability", this.normalizedChance.get(i), i));
		}
	}
}
