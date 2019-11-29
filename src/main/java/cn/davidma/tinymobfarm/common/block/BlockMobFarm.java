package cn.davidma.tinymobfarm.common.block;

import java.util.List;

import cn.davidma.tinymobfarm.client.gui.InteractionObjectMobFarm;
import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.EnumMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockMobFarm extends Block {
	
	public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
	
	private static final VoxelShape BOUNDING_BOX = Block.makeCuboidShape(1, 0, 1, 15, 14, 15);

	private EnumMobFarm mobFarmData;
	
	public BlockMobFarm(EnumMobFarm mobFarmData) {
		super(Block.Properties.from(mobFarmData.getBaseBlock()));
		this.setRegistryName(Reference.getLocation(mobFarmData.getRegistryName()));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
		this.mobFarmData = mobFarmData;
	}
	
	public Consumer<List<ITextComponent>> getTooltipBuilder() {
		return this.mobFarmData::addTooltip;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, IBlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context) {
		EnumFacing facing = context.getPlacementHorizontalFacing().getOpposite();
		return this.getDefaultState().with(FACING, facing);
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
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote() && player instanceof EntityPlayerMP) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntityMobFarm) {
				NetworkHooks.openGui((EntityPlayerMP) player, new InteractionObjectMobFarm((TileEntityMobFarm) tileEntity), (buffer) -> {
					buffer.writeBlockPos(pos);
				});
			}
		}
		return true;
	}
	
	@Override
	public IBlockState updatePostPlacement(IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		TileEntity tileEntity = world.getTileEntity(currentPos);
		if (tileEntity instanceof TileEntityMobFarm) {
			TileEntityMobFarm tileEntityMobFarm = (TileEntityMobFarm) tileEntity;
			tileEntityMobFarm.updateRedstone();
			tileEntityMobFarm.saveAndSync();
		}
		return state;
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (!world.isRemote() && tileEntity instanceof TileEntityMobFarm) {
			ItemStack lasso = ((TileEntityMobFarm) tileEntity).getLasso();
			if (!lasso.isEmpty()) {
				EntityItem drop = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, lasso);
				world.spawnEntity(drop);
			}
		}
		super.onBlockHarvested(world, pos, state, player);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
		return new TileEntityMobFarm();
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return BOUNDING_BOX;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return 0;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
