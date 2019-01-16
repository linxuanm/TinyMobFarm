package cn.davidma.idleloot.block.template;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GeneratorBase extends StandardBlockBase {

	public GeneratorBase(String name, Material mat, SoundType sound, float hard, String harv, int harvLvl) {
		super(name, mat);
		setSoundType(sound);
		setHardness(hard);
		setHarvestLevel(harv, harvLvl);
		
		setResistance(300.0F);
		setLightOpacity(1);
	}

	@Override
	public boolean isFullBlock(IBlockState bs) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState bs) {
		return false;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState bs, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
	}	
}
