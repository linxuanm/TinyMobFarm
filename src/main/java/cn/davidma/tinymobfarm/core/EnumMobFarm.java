package cn.davidma.tinymobfarm.core;

public enum EnumMobFarm {

	WOOD("wood_farm", false, new int[] {2, 2, 3}),
	STONE("stone_farm", false),
	IRON("iron_farm", true),
	GOLD("gold_farm", true),
	DIAMOND("diamond_farm", true),
	EMERALD("emerald_farm", true),
	INFERNAL("infernal_farm", true),
	ULTIMATE("ultimate_farm", true);
	
	private String registryName;
	private boolean canFarmHostile;
	
	private EnumMobFarm(String registryName, boolean canFarmHostile, int[] damageChance) {
		this.registryName = registryName;
		this.canFarmHostile = canFarmHostile;
	}
	
	public String getRegistryName() {
		return this.registryName;
	}
	
	public String getUnlocalizedName() {
		return String.format("tile.%s.name", this.registryName);
	}
	
	public boolean canFarmHostile() {
		return this.canFarmHostile;
	}
}
