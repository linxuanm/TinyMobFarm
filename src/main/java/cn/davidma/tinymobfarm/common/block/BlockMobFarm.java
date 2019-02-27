package cn.davidma.tinymobfarm.common.block;

import java.util.List;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.EnumMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMobFarm extends Block {
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(1 / 16.0, 0 / 16.0, 1 / 16.0, 15 / 16.0, 14 / 16.0, 15 / 16.0);

	private EnumMobFarm mobFarmData;
	
	@SuppressWarnings("deprecation")
	public BlockMobFarm(EnumMobFarm mobFarmData) {
		super(mobFarmData.getBaseBlock().getMaterial(null));
		Block baseBlock = mobFarmData.getBaseBlock();
		this.setHardness(baseBlock.getBlockHardness(null, null, null));
		this.setSoundType(mobFarmData.getBaseBlock().getSoundType());
		
		this.setRegistryName(mobFarmData.getRegistryName());
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.NORTH));
		this.mobFarmData = mobFarmData;
	}
	
	public Consumer<List<String>> getTooltipBuilder() {
		return this.mobFarmData::addTooltip;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityMobFarm) {
			TileEntityMobFarm tileEntityMobFarm = (TileEntityMobFarm) tileEntity;
			tileEntityMobFarm.setMobFarmData(mobFarmData);
			tileEntityMobFarm.updateRedstone();
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && player instanceof EntityPlayerMP) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntityMobFarm) {
				player.openGui(Reference.MOD_ID, Reference.FARM_GUI, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (!world.isRemote && tileEntity instanceof TileEntityMobFarm) {
			ItemStack lasso = ((TileEntityMobFarm) tileEntity).getLasso();
			if (!lasso.isEmpty()) {
				EntityItem drop = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, lasso);
				world.spawnEntity(drop);
			}
		}
		super.onBlockHarvested(world, pos, state, player);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);
		if (facing.getAxis() == EnumFacing.Axis.Y) {
			facing = EnumFacing.NORTH;
		}
		return this.getDefaultState().withProperty(FACING, facing);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityMobFarm();
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return BOUNDING_BOX;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public int getLightOpacity(IBlockState state) {
		return 0;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
