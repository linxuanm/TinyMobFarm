package cn.davidma.idleloot.tileentity;

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
import scala.collection.mutable.Stack;

public class GeneratorTileEntity extends TileEntity implements IInventory, ITickable {

	private NBTTagCompound entityNBT;
	private int currProgress, totalProgress;
	
	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
	private String name;
	
	public void pushLoot() {
		
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
		return !name.isEmpty() && name != null;
	}
	
	@Override
	public ITextComponent getDisplayName() {
		if (hasCustomName()) return new TextComponentString(getName());
		return new TextComponentTranslation(getName());
	}

	@Override
	public void update() {
		if (world.isRemote) return;
		
		currProgress++;
		if (currProgress >= totalProgress) {
			currProgress = 0;
			pushLoot();
			markDirty();
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
		currProgress = nbt.getInteger(NBTTagHelper.CURR_PROGRESS_TAG);
		totalProgress = nbt.getInteger(NBTTagHelper.TOTAL_PROGRESS_TAG);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (hasCustomName()) nbt.setString(NBTTagHelper.CUSTOM_NAME_TAG, name);
		nbt.setInteger(NBTTagHelper.CURR_PROGRESS_TAG, currProgress);
		nbt.setInteger(NBTTagHelper.TOTAL_PROGRESS_TAG, totalProgress);
		ItemStackHelper.saveAllItems(nbt, inventory);
		
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
		return NBTTagHelper.containsMob(NBTTagHelper.getEssentialNBT(stack));
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
