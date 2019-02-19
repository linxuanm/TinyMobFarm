package cn.davidma.tinymobfarm.common.tileentity;

import cn.davidma.tinymobfarm.common.TinyMobFarm;
import cn.davidma.tinymobfarm.core.EnumMobFarm;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityMobFarm extends TileEntity implements ITickable {

	private ItemStackHandler inventory = new ItemStackHandler(1);
	private EnumMobFarm mobFarmData;
	private int currProgress;
	
	public TileEntityMobFarm(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}
	
	public TileEntityMobFarm() {
		super(TinyMobFarm.tileEntityMobFarm);
	}

	@Override
	public void tick() {
		
	}
	
	public void setMobFarmData(EnumMobFarm mobFarmData) {
		this.mobFarmData = mobFarmData;
	}
}
