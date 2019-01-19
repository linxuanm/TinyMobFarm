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
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import scala.collection.mutable.Stack;

public class MobFarmTileEntity extends TileEntity implements IInventory, ITickable {

	private NBTTagCompound entityNBT;
	private int currProgress, totalProgress;
	
	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
	private String name = "Generator";
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
			
			// markDirt flag.
			boolean dirty = false;
			
			// Prioritize slots with the same item.
			for (int insert = 0; insert < 2; insert++) {
				for (int j = 0; j < inv.getSlots(); j++) {
				
					while (items.contains(ItemStack.EMPTY)) items.remove(ItemStack.EMPTY);
					// Just checking.
					if (items.isEmpty()) return;
				
					for (int k = 0; k < items.size(); k++) {
					
						// Check for same item.
						ItemStack tmp = inv.insertItem(j, items.get(k), true);
						// Can insert.
						if (tmp.getCount() != items.get(k).getCount() && (!(inv.getStackInSlot(j).getCount() == 0) || insert == 1)) {
							ItemStack stack = inv.insertItem(j, items.get(k), false);
							items.set(k, ItemStack.EMPTY);
						
							// Add remainder to list.
							if (stack != null && !stack.isEmpty()) items.set(k, stack);
							
							dirty = true;
						}
					}
				}
			}
			if (dirty) adjacent.markDirty();
		}
		
		// Chuck 'em into da world!
		BlockPos here = this.getPos();
		for (ItemStack i: items) {
			EntityItem biu = new EntityItem(this.world, here.getX() + 0.5D, here.getY() + 1, here.getZ() + 0.5D, i);
			this.world.spawnEntity(biu);
		}
	}
	
	public void addDropsToList(NonNullList<ItemStack> drops) {
		for(ItemStack i: inventory) drops.add(i);
	}
	
	public boolean working() {
		boolean work = this.hasLasso() &&
			!this.redstoneOn() &&
			(!this.hasHostileMob() || this.id >= Info.LOWEST_ID_FOR_HOSTILE_SPAWNING);
		if (!work) this.currProgress = 0;
		return work;
	}
	
	public boolean hasHostileMob() {
		return this.hasLasso() && NBTTagHelper.isHostile(this.getLasso());
	}
	
	public boolean hasLasso() {
		ItemStack stack = this.getLasso();
		return !stack.isEmpty() && NBTTagHelper.containsMob(stack);
	}
	
	public ItemStack getLasso() {
		return this.getStackInSlot(0);
	}
	
	@Override
	public String getName() {
		if (hasCustomName()) return name;
		return "container.mobfarm";
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean hasCustomName() {
		return name != null && !name.isEmpty();
	}
	
	@Override
	public ITextComponent getDisplayName() {
		if (hasCustomName()) return new TextComponentString(getName());
		return new TextComponentTranslation(getName());
	}

	@Override
	public void update() {
		if (world.isRemote) return;
		
		if (this.working()) {
			currProgress++;
			if (currProgress >= totalProgress) {
				currProgress = 0;
				
				// Push items to inventory.
				ItemStack stack = this.getStackInSlot(0);
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
				
				world.notifyBlockUpdate(this.pos, world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
				this.markDirty();
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
	public int getSizeInventory() {
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack i: inventory) {
			if (!i.isEmpty()) return true;
		}
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(inventory, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(inventory, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack prev = getStackInSlot(index);
		if (!stack.isEmpty() && prev.isEmpty()) inventory.set(index, stack);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		inventory = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, inventory);
		if (nbt.hasKey(NBTTagHelper.CUSTOM_NAME_TAG)) setName(nbt.getString(NBTTagHelper.CUSTOM_NAME_TAG));
		int inId = nbt.getInteger(NBTTagHelper.ID_TAG);
		currProgress = nbt.getInteger(NBTTagHelper.CURR_PROGRESS_TAG);
		
		this.init(this.getName(), inId);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (hasCustomName()) nbt.setString(NBTTagHelper.CUSTOM_NAME_TAG, this.name);
		nbt.setInteger(NBTTagHelper.ID_TAG, this.id);
		nbt.setInteger(NBTTagHelper.CURR_PROGRESS_TAG, this.currProgress);
		ItemStackHelper.saveAllItems(nbt, this.inventory);
		return nbt;
	}
	
	public boolean redstoneOn() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return NBTTagHelper.containsMob(stack);
	}

	@Override
	public int getField(int id) {
		if (id == 0) return currProgress;
		return totalProgress;
	}

	@Override
	public void setField(int id, int value) {
		if (id == 0) currProgress = value;
		else totalProgress = value;
	}

	@Override
	public int getFieldCount() {
		return 2;
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}
	
	public int getId() {
		return id;
	}
}
