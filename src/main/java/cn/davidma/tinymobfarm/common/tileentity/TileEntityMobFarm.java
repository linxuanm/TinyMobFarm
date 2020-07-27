package cn.davidma.tinymobfarm.common.tileentity;

import java.util.List;

import javax.annotation.Nullable;

import cn.davidma.tinymobfarm.client.gui.ContainerMobFarm;
import cn.davidma.tinymobfarm.common.TinyMobFarm;
import cn.davidma.tinymobfarm.core.EnumMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import cn.davidma.tinymobfarm.core.util.Config;
import cn.davidma.tinymobfarm.core.util.EntityHelper;
import cn.davidma.tinymobfarm.core.util.FakePlayerHelper;
import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityMobFarm extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
	
	private ItemStackHandler inventory = new ItemStackHandler(1);
	private EnumMobFarm mobFarmData;
	private LivingEntity model;
	private Direction modelFacing;
	private int currProgress;
	private boolean powered;
	private boolean shouldUpdate;
	
	public TileEntityMobFarm(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}
	
	public TileEntityMobFarm() {
		super(TinyMobFarm.tileEntityMobFarm);
	}

	@Override
	public void tick() {
		if (this.shouldUpdate) {
			this.updateModel();
			this.updateRedstone();
			this.shouldUpdate = false;
		}
		if (this.isWorking()) {
			this.currProgress++;
			if (!this.world.isRemote() && this.mobFarmData != null) {
				if (this.currProgress >= this.mobFarmData.getMaxProgress()) {
					this.currProgress = 0;
					
					this.generateDrops();
					
					FakePlayer daniel = FakePlayerHelper.getPlayer((ServerWorld) world);
					this.getLasso().damageItem(this.mobFarmData.getRandomDamage(this.world.rand), daniel, wut -> {});
					
					this.saveAndSync();
				}
			}
		} else {
			this.currProgress = 0;
		}
	}
	
	private void generateDrops() {
		ItemStack lasso = this.getLasso();
		String lootTableLocation = NBTHelper.getBaseTag(lasso).getString(NBTHelper.MOB_LOOTTABLE_LOCATION);
		if (lootTableLocation.isEmpty()) return;
		
		List<ItemStack> drops = EntityHelper.generateLoot(new ResourceLocation(lootTableLocation), this.world, EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, lasso));
		for (Direction facing: Direction.values()) {
			TileEntity tileEntity = this.world.getTileEntity(this.pos.offset(facing));
			if (tileEntity != null) {
				LazyOptional<IItemHandler> capability = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
				capability.ifPresent((itemHandler) -> {
					for (int i = 0; i < drops.size(); i++) {
						ItemStack remain = ItemHandlerHelper.insertItemStacked(itemHandler, drops.get(i), false);
						if (remain.isEmpty()) {
							drops.remove(i);
							i--;
						}
					}
				});
				if (drops.isEmpty()) return;
			}
		}
		
		if (Config.DISABLE_WHEN_CHEST_FULL) return;
		
		for (ItemStack stack: drops) {
			ItemEntity entityItem = new ItemEntity(this.world, this.pos.getX() + 0.5, this.pos.getY() + 1, this.pos.getZ() + 0.5, stack);
			this.world.addEntity(entityItem);
		}
		
	}
	
	private void updateModel() {
		if (this.world.isRemote()) {
			if (this.getLasso().isEmpty()) {
				this.model = null;
			} else {
				CompoundNBT nbt = NBTHelper.getBaseTag(this.getLasso());
				String mobName = nbt.getString(NBTHelper.MOB_NAME);
				if (this.model == null || !this.model.getName().getUnformattedComponentText().equals(mobName)) {
					CompoundNBT entityData = nbt.getCompound(NBTHelper.MOB_DATA);
					Entity newModel = EntityType.func_220335_a(entityData, this.world, entity -> {
						return entity;
					});
					
					if (newModel != null && newModel instanceof LivingEntity) {
						this.model = (LivingEntity) newModel;
						this.modelFacing = this.world.getBlockState(this.pos).get(HorizontalBlock.HORIZONTAL_FACING);
					}
				}
			}
		}
	}
	
	public boolean isWorking() {
		if (this.mobFarmData == null || this.getLasso().isEmpty() || this.isPowered()) return false;
		return this.mobFarmData.isLassoValid(this.getLasso());
	}
	
	public void updateRedstone() {
		this.powered = this.world.getRedstonePowerFromNeighbors(this.pos) != 0;
	}
	
	public ItemStack getLasso() {
		return this.inventory.getStackInSlot(0);
	}
	
	public void setMobFarmData(EnumMobFarm mobFarmData) {
		this.mobFarmData = mobFarmData;
	}
	
	public boolean isPowered() {
		return this.powered;
	}
	
	/*
	 * Not using capability here.
	 * I am trying to prevent the inserting/extracting of the lasso via automation.
	 * The player is encouraged to build multiple farms instead of automating 
	 * multiple mobs with the same farm.
	 */
	public ItemStackHandler getInventory() {
		return this.inventory;
	}
	
	public double getScaledProgress() {
		if (this.mobFarmData == null) return 0;
		return this.currProgress / (double) this.mobFarmData.getMaxProgress();
	}
	
	public LivingEntity getModel() {
		return this.model;
	}
	
	public Direction getModelFacing() {
		return this.modelFacing;
	}
	
	public String getUnlocalizedName() {
		if (this.mobFarmData == null) return "block." + Reference.MOD_ID + ".default_mob_farm";
		return this.mobFarmData.getUnlocalizedName();
	}
	
	public void saveAndSync() {
		BlockState state = this.world.getBlockState(this.pos);
		this.world.notifyBlockUpdate(pos, state, state, 3);
		this.markDirty();
	}
	
	@Override
	public void read(CompoundNBT nbt) {
		super.read(nbt);
		this.mobFarmData = EnumMobFarm.values()[nbt.getInt(NBTHelper.MOB_FARM_DATA)];
		this.currProgress = nbt.getInt(NBTHelper.CURR_PROGRESS);
		this.inventory.deserializeNBT(nbt.getCompound(NBTHelper.INVENTORY));
		this.shouldUpdate = true;
		
	}
	
	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		if (this.mobFarmData == null) return nbt;
		nbt.putInt(NBTHelper.MOB_FARM_DATA, this.mobFarmData.ordinal());
		nbt.putInt(NBTHelper.CURR_PROGRESS, this.currProgress);
		nbt.put(NBTHelper.INVENTORY, this.inventory.serializeNBT());
		return super.write(nbt);
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getPos(), 0, this.getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.read(packet.getNbtCompound());
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}
	
	@Override
	public void handleUpdateTag(CompoundNBT nbt) {
		this.read(nbt);
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new ContainerMobFarm(windowId, inv, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent(mobFarmData.getUnlocalizedName());
	}
}
