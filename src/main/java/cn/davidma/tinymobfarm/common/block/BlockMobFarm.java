package cn.davidma.tinymobfarm.common.block;

import java.util.List;

import cn.davidma.tinymobfarm.common.TinyMobFarm;
import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.EnumMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockMobFarm extends Block {
	
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	
	private static final VoxelShape BOUNDING_BOX = Block.makeCuboidShape(1, 0, 1, 15, 14, 15);

	private EnumMobFarm mobFarmData;
	
	public BlockMobFarm(EnumMobFarm mobFarmData) {
		super(Block.Properties.from(mobFarmData.getBaseBlock()));
		this.setRegistryName(Reference.getLocation(mobFarmData.getRegistryName()));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
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
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction facing = context.getPlacementHorizontalFacing().getOpposite();
		return this.getDefaultState().with(FACING, facing);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityMobFarm) {
			TileEntityMobFarm tileEntityMobFarm = (TileEntityMobFarm) tileEntity;
			tileEntityMobFarm.setMobFarmData(mobFarmData);
			tileEntityMobFarm.updateRedstone();
		}
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote) return true;
		
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityMobFarm) {
			TileEntityMobFarm tileEntityMobFarm = (TileEntityMobFarm) tileEntity;
			NetworkHooks.openGui((ServerPlayerEntity) player, tileEntityMobFarm, pos);
		}
		
		return true;
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		TileEntity tileEntity = world.getTileEntity(currentPos);
		if (tileEntity instanceof TileEntityMobFarm) {
			TileEntityMobFarm tileEntityMobFarm = (TileEntityMobFarm) tileEntity;
			tileEntityMobFarm.updateRedstone();
			tileEntityMobFarm.saveAndSync();
		}
		return state;
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (!world.isRemote() && tileEntity instanceof TileEntityMobFarm) {
			ItemStack lasso = ((TileEntityMobFarm) tileEntity).getLasso();
			if (!lasso.isEmpty()) {
				ItemEntity drop = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, lasso);
				world.addEntity(drop);
			}
		}
		super.onBlockHarvested(world, pos, state, player);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TinyMobFarm.tileEntityMobFarm.create();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return BOUNDING_BOX;
	}
	
	@Override
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return false;
	}
	
	@Override
	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 0;
	}
}
