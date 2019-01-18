package cn.davidma.tinymobfarm.block.template;

import java.util.ArrayList;
import java.util.List;

import cn.davidma.tinymobfarm.Main;
import cn.davidma.tinymobfarm.handler.CollectionsManager;
import cn.davidma.tinymobfarm.reference.Info;
import cn.davidma.tinymobfarm.tileentity.MobFarmTileEntity;
import cn.davidma.tinymobfarm.util.Msg;
import net.minecraft.block.BlockContainer;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MobFarmBase extends StandardBlockBase implements ITileEntityProvider {
	
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 0.875, 0.9375);
	private static final AxisAlignedBB COLLISION_BOX = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.8125, 0.875);
	
	private int id;
	private String name;
	private NonNullList<ItemStack> drops;

	public MobFarmBase(int id, String name, Material mat, SoundType sound, float hard, String harv, int harvLvl) {
		super(name, mat, id);
		
		// Name and id.
		this.id = id;
		String tmp = name.replace("_farm", " Mob Farm").replace("wood", "wooden").replace("gold", "golden");
		tmp = tmp.substring(0, 1).toUpperCase() + tmp.substring(1);
		this.name = tmp;
		
		setSoundType(sound);
		setHardness(hard);
		setHarvestLevel(harv, harvLvl);
		
		setResistance(300.0F);
		setLightOpacity(1);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState bs, EntityPlayer player, EnumHand hand, EnumFacing side, float x, float y, float z) {
		if (world.isRemote) return true;
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof MobFarmTileEntity) {
			MobFarmTileEntity generatorTileEntity = (MobFarmTileEntity) tileEntity;
			player.openGui(Main.instance, Info.FARM_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		drops = NonNullList.<ItemStack>create();
		
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof MobFarmTileEntity) {
			((MobFarmTileEntity) te).addDropsToList(drops);
		}
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		super.getDrops(drops, world, pos, state, fortune);
		for (ItemStack i: this.drops) {
			drops.add(i);
		}
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState bs, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
	public boolean isFullBlock(IBlockState bs) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState bs) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState bs) {
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState bs, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState bs, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean isActualState) {
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_BOX);
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState bs, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		MobFarmTileEntity te = new MobFarmTileEntity();
		te.init(name, id);
		return te;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
}
