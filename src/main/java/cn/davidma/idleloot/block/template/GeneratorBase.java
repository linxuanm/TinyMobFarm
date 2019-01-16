package cn.davidma.idleloot.block.template;

import java.util.List;

import cn.davidma.idleloot.tileentity.GeneratorTileEntity;
import cn.davidma.idleloot.util.Msg;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GeneratorBase extends StandardBlockBase implements ITileEntityProvider {
	
	private static final AxisAlignedBB TOP_BOX = new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 0.875, 0.9375);
	private static final AxisAlignedBB BASE_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.125, 1);

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
	public AxisAlignedBB getBoundingBox(IBlockState bs, IBlockAccess source, BlockPos pos) {
		return TOP_BOX.union(BASE_BOX);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState bs, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean isActualState) {
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, TOP_BOX.union(BASE_BOX));
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState bs, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new GeneratorTileEntity();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState bs, EntityPlayer player, EnumHand hand, EnumFacing side, float x, float y, float z) {
		if (world.isRemote) return true;
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof GeneratorTileEntity) {
			GeneratorTileEntity tile = (GeneratorTileEntity) tileEntity;
			Msg.tellPlayer(player, "Click!");
		}
		return true;
	}
}
