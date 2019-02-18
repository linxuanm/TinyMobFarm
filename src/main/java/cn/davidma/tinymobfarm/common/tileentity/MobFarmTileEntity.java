package cn.davidma.tinymobfarm.common.tileentity;

import cn.davidma.tinymobfarm.common.TinyMobFarm;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class MobFarmTileEntity extends TileEntity {

	public MobFarmTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}
	
	public MobFarmTileEntity() {
		super(TinyMobFarm.mobFarmTileEntity);
	}
}
