package cn.davidma.tinymobfarm.core;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;;

public enum EnumMobFarm {

	WOOD("wood_farm", Blocks.OAK_WOOD, false),
	STONE("stone_farm", Blocks.STONE, false),
	IRON("iron_farm", Blocks.IRON_BLOCK, true),
	GOLD("gold_farm", Blocks.GOLD_BLOCK, true),
	DIAMOND("diamond_farm", Blocks.DIAMOND_BLOCK, true),
	EMERALD("emerald_farm", Blocks.EMERALD_BLOCK, true),
	INFERNAL("inferno_farm", Blocks.OBSIDIAN, true),
	ULTIMATE("ultimate_farm", Blocks.OBSIDIAN, true);
	
	private String registryName;
	private Block baseBlock;
	private boolean canFarmHostile;
	
	private EnumMobFarm(String registryName, Block baseBlock, boolean canFarmHostile) {
		this.registryName = registryName;
		this.baseBlock = baseBlock;
		this.canFarmHostile = canFarmHostile;
	}
	
	public String getRegistryName() {
		return this.registryName;
	}
	
	public String getUnlocalizedName() {
		return String.format("tile.%s.name", this.registryName);
	}
	
	public Block getBaseBlock() {
		return this.baseBlock;
	}
	
	public boolean canFarmHostile() {
		return this.canFarmHostile;
	}
	
	public int getMaxProgress() {
		return 50 * 20;
	}
}
