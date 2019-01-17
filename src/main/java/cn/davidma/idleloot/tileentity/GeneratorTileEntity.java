package cn.davidma.idleloot.tileentity;

import java.util.Random;

import cn.davidma.idleloot.reference.IdleLootConfig;
import cn.davidma.idleloot.reference.Info;
import cn.davidma.idleloot.util.NBTTagHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import scala.collection.mutable.Stack;

public class GeneratorTileEntity extends TileEntity implements IInventory, ITickable {

	private NBTTagCompound entityNBT;
	private int currProgress, totalProgress;
	
	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
	private String name = "Generator";
	private int id = 0;
	
	public GeneratorTileEntity() {
		super();
	}
	
	public void init(String name, int id) {
		setName(name);
		this.id = id;
		this.currProgress = 0;
		this.totalProgress = IdleLootConfig.GENERATOR_SPEED[id] * 20;
	}
	
	public void pushLoot() {
		
	}
	
	public void addDropsToList(NonNullList<ItemStack> drops) {
		for(ItemStack i: inventory) drops.add(i);
	}
	
	public boolean working() {
		ItemStack stack = this.getStackInSlot(0);
		boolean work = !stack.isEmpty() && NBTTagHelper.containsMob(stack);
		if (!work) this.currProgress = 0;
		return work;
	}
	
	@Override
	public String getName() {
		if (hasCustomName()) return name;
		return "container.generator";
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
				pushLoot();
				
				// Damage the lasso.
				Random rand = new Random();
				ItemStack stack = this.getStackInSlot(0);
				stack.attemptDamageItem(Info.DURABILITY_COST_FROM_GENERATOR_ID(this.id, rand), rand, null);
				
				this.markDirty();
			}
		}
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
}
