package cn.davidma.tinymobfarm.common.block;

import cn.davidma.tinymobfarm.core.EnumMobFarm;
import net.minecraft.block.Block;

public class MobFarm extends Block {

	private EnumMobFarm mobFarmData;
	
	public MobFarm(EnumMobFarm mobFarmData) {
		super(Block.Properties.from(mobFarmData.getBaseBlock()));
		this.mobFarmData = mobFarmData;
	}
}
