package cn.davidma.tinymobfarm.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import cn.davidma.tinymobfarm.reference.TinyMobFarmConfig;
import cn.davidma.tinymobfarm.reference.Info;
import cn.davidma.tinymobfarm.util.FakePlayerHelper;
import cn.davidma.tinymobfarm.util.LootTableHelper;
import cn.davidma.tinymobfarm.util.Msg;
import cn.davidma.tinymobfarm.util.NBTTagHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import scala.collection.mutable.Stack;

public class MobFarmTileEntity extends TileEntity implements ITickable {

	private NBTTagCompound entityNBT;
	private int currProgress, totalProgress;
	
	private ItemStackHandler inventory = new ItemStackHandler(1);
	private String name = "Generator";
	private EntityLiving mob;
	private int dir = 0;
	private int id = 0;
	
	public MobFarmTileEntity() {
		super();
	}
	
	public void init(String name, int id) {
		setName(name);
		this.id = id;
		this.currProgress = 0;
		this.totalProgress = (int) (TinyMobFarmConfig.GENERATOR_SPEED[id] * 20);
	}
	
	public void pushItemsToInv(List<ItemStack> items) {
		
		// Double check (XD).
		if (this.world.isRemote) return;
		
		EnumFacing[] facing = {
			EnumFacing.DOWN,
			EnumFacing.UP,
			EnumFacing.WEST,
			EnumFacing.EAST,
			EnumFacing.NORTH,
			EnumFacing.SOUTH
		};
		
		// [xOffset, yOffset, xOffset, facingIndex]
		int[][] pos = {
			{0, 1, 0, 0},
			{0, -1, 0, 1},
			{0, 0, 1, 2},
			{0, 0, -1, 3},
			{1, 0, 0, 4},
			{-1, 0, 0, 5}
		};
		
		// Iterate through adjacent inventories.
		for (int[] i: pos) {
			TileEntity adjacent = this.world.getTileEntity(this.getPos().add(new BlockPos(i[0], i[1], i[2])));
			if (adjacent == null) continue;
				
			// Grab the IItemHandler and do stuff with it.
			IItemHandler inv = adjacent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing[i[3]]);
			if (inv == null) continue;
			
			for (int j = 0; j < items.size(); j++) {
				items.set(j, ItemHandlerHelper.insertItemStacked(inv, items.get(j), false));
			}
			
			adjacent.markDirty();
			
			for (int j = 0; j < items.size(); j++) {
				if (items.get(j).isEmpty()) {
					items.remove(j);
					j--;
				}
			}
		}
		
		// Chuck 'em into da world!
		BlockPos here = this.getPos();
		for (ItemStack i: items) {
			EntityItem biu = new EntityItem(this.world, here.getX() + 0.5D, here.getY() + 1, here.getZ() + 0.5D, i);
			this.world.spawnEntity(biu);
		}
	}
	
	public boolean working() {
		// if (this.world.isRemote) System.out.println("Client: " + this.hasLasso());
		// else System.out.println("Server: " + this.hasLasso());
		boolean work = this.hasLasso() &&
			(!this.hasHostileMob() || this.id >= Info.LOWEST_ID_FOR_HOSTILE_SPAWNING) &&
			!this.getPower();
		if (!work) this.currProgress = 0;
		return work;
	}
	
	public EntityLiving getMob() {
		return this.mob;
	}
	
	private void updateModel() {
		if (!world.isRemote) return;
		if (this.hasLasso()) {
			NBTTagCompound nbt = NBTTagHelper.getEssentialNBT(this.getLasso());
			String lassoMobName = nbt.getString(NBTTagHelper.MOB_NAME);
			
			// Should never happen.
			if (lassoMobName == null || lassoMobName.isEmpty()) return;
			
			if (this.mob == null || !lassoMobName.equals(this.mob.getName())) {
				NBTTagCompound entityNBT = nbt.getCompoundTag(NBTTagHelper.ENTITY_INFO);
				entityNBT.setInteger("Dimension", world.provider.getDimension());
				entityNBT.removeTag("Pos");
				Entity newMob = EntityList.createEntityFromNBT(entityNBT, world);
				if (newMob == null || !(newMob instanceof EntityLiving)) return;
				this.mob = (EntityLiving) newMob;
			}
		} else {
			this.mob = null;
		}
	}
	
	public boolean getPower() {
		return this.world.isBlockPowered(this.pos);
	}
	
	public boolean hasHostileMob() {
		return this.hasLasso() && NBTTagHelper.isHostile(this.getLasso());
	}
	
	public String getMobName() {
		NBTTagCompound nbt = NBTTagHelper.getEssentialNBT(this.getLasso());
		String mobName = nbt.getString(NBTTagHelper.MOB_NAME);
		return mobName;
	}
	
	public boolean hasLasso() {
		ItemStack stack = this.getLasso();
		return !stack.isEmpty() && NBTTagHelper.containsMob(stack);
	}
	
	public ItemStack getLasso() {
		return this.inventory.getStackInSlot(0);
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void update() {
		updateModel();
		this.currProgress++;
		if (world.isRemote) {
			if (currProgress >= totalProgress) {
				this.currProgress = 0;
			}
			return;
		}
		
		if (this.working()) {
			((WorldServer) this.world).spawnParticle(EnumParticleTypes.PORTAL, this.pos.getX() + 0.5D, this.pos.getY(), this.pos.getZ() + 0.5D, 3, 0.15D, 0, 0.15D, 0, null);
			// System.out.println(String.format("%d / %d", this.currProgress, this.totalProgress));
			if (this.currProgress >= this.totalProgress) {
				this.currProgress = 0;
				
				// Push items to inventory.
				ItemStack stack = this.getLasso();
				NBTTagCompound nbt = NBTTagHelper.getEssentialNBT(stack);
				String locationStr = nbt.getString(NBTTagHelper.LOOT_TABLE_LOCATION);
				if (locationStr == null || locationStr.isEmpty()) return;
				ResourceLocation location = new ResourceLocation(locationStr);
				List<ItemStack> loots = LootTableHelper.genLoots(location, this.world);
				pushItemsToInv(loots);
				
				// Damage the lasso.
				Random rand = new Random();
				int amount = Info.DURABILITY_COST_FROM_FARM_ID(this.id, rand);
				stack.damageItem(amount, FakePlayerHelper.getPlayer((WorldServer) this.world));
				
				this.sendUpdate();
			}
		}
	}
	
	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), this.writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound nbt) {
		this.readFromNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.inventory.deserializeNBT(nbt.getCompoundTag(NBTTagHelper.INVENTORY));
		this.id = nbt.getInteger(NBTTagHelper.ID_TAG);
		this.currProgress = nbt.getInteger(NBTTagHelper.CURR_PROGRESS_TAG);
		this.dir = nbt.getInteger(NBTTagHelper.FACING);
		this.totalProgress = (int) (TinyMobFarmConfig.GENERATOR_SPEED[this.id] * 20);
		this.name = nbt.getString(NBTTagHelper.NAME);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger(NBTTagHelper.ID_TAG, this.id);
		nbt.setInteger(NBTTagHelper.CURR_PROGRESS_TAG, this.currProgress);
		nbt.setInteger(NBTTagHelper.FACING, this.dir);
		nbt.setString(NBTTagHelper.NAME, this.name);
		nbt.setTag(NBTTagHelper.INVENTORY, this.inventory.serializeNBT());
		return nbt;
	}
	
	public void sendUpdate() {
		this.world.markBlockRangeForRenderUpdate(pos, pos);
		IBlockState tmp = this.world.getBlockState(this.pos);
		this.world.notifyBlockUpdate(pos, tmp, tmp, 3);
		this.world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
		this.markDirty();
	}
	
	public int getTotalProgress() {
		return this.totalProgress;
	}
	
	public int getCurrProgress() {
		return this.currProgress;
	}
	
	public int getId() {
		return id;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}
	
	public int getDir() {
		return this.dir;
	}
	
	public ITextComponent getDisplayName() {
		return new TextComponentString(this.name);
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if (facing != null) return false;
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (facing != null) return null;
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
	}
}
