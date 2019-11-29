package cn.davidma.tinymobfarm.common.tileentity;

import java.util.List;

import javax.annotation.Nullable;

import cn.davidma.tinymobfarm.common.TinyMobFarm;
import cn.davidma.tinymobfarm.core.EnumMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import cn.davidma.tinymobfarm.core.util.EntityHelper;
import cn.davidma.tinymobfarm.core.util.FakePlayerHelper;
import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityMobFarm extends TileEntity implements ITickable {
	
	private ItemStackHandler inventory = new ItemStackHandler(1);
	private EnumMobFarm mobFarmData;
	private EntityLiving model;
	private EnumFacing modelFacing;
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
					
					FakePlayer daniel = FakePlayerHelper.getPlayer((WorldServer) world);
					this.getLasso().damageItem(this.mobFarmData.getRandomDamage(this.world.rand), daniel);
					
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
		
		List<ItemStack> drops = EntityHelper.generateLoot(new ResourceLocation(lootTableLocation), this.world);
		for (EnumFacing facing: EnumFacing.values()) {
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
		
		for (ItemStack stack: drops) {
			EntityItem entityItem = new EntityItem(this.world, this.pos.getX() + 0.5, this.pos.getY() + 1, this.pos.getZ() + 0.5, stack);
			this.world.spawnEntity(entityItem);
		}
		
	}
	
	private void updateModel() {
		if (this.world.isRemote()) {
			if (this.getLasso().isEmpty()) {
				this.model = null;
			} else {
				NBTTagCompound nbt = NBTHelper.getBaseTag(this.getLasso());
				String mobName = nbt.getString(NBTHelper.MOB_NAME);
				if (this.model == null || !this.model.getName().getUnformattedComponentText().equals(mobName)) {
					NBTTagCompound entityData = nbt.getCompound(NBTHelper.MOB_DATA);
					Entity newModel = EntityType.create(entityData, this.world);
					
					if (newModel != null && newModel instanceof EntityLiving) {
						this.model = (EntityLiving) newModel;
						this.modelFacing = this.world.getBlockState(this.pos).get(BlockHorizontal.HORIZONTAL_FACING);
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
	
	@Deprecated
	public ItemStackHandler getInventory() {
		return this.inventory;
	}
	
	public double getScaledProgress() {
		if (this.mobFarmData == null) return 0;
		return this.currProgress / (double) this.mobFarmData.getMaxProgress();
	}
	
	public EntityLiving getModel() {
		return this.model;
	}
	
	public EnumFacing getModelFacing() {
		return this.modelFacing;
	}
	
	public String getUnlocalizedName() {
		if (this.mobFarmData == null) return "block." + Reference.MOD_ID + ".default_mob_farm";
		return this.mobFarmData.getUnlocalizedName();
	}
	
	public void saveAndSync() {
		IBlockState state = this.world.getBlockState(this.pos);
		this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		this.world.notifyBlockUpdate(pos, state, state, 3);
		this.markDirty();
	}
	
	@Override
	public void read(NBTTagCompound nbt) {
		super.read(nbt);
		this.mobFarmData = EnumMobFarm.values()[nbt.getInt(NBTHelper.MOB_FARM_DATA)];
		this.currProgress = nbt.getInt(NBTHelper.CURR_PROGRESS);
		this.inventory.deserializeNBT(nbt.getCompound(NBTHelper.INVENTORY));
		this.shouldUpdate = true;
		
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound nbt) {
		if (this.mobFarmData == null) return nbt;
		nbt.setInt(NBTHelper.MOB_FARM_DATA, this.mobFarmData.ordinal());
		nbt.setInt(NBTHelper.CURR_PROGRESS, this.currProgress);
		nbt.setTag(NBTHelper.INVENTORY, this.inventory.serializeNBT());
		return super.write(nbt);
	}
	
	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), 0, this.getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		this.read(packet.getNbtCompound());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.write(new NBTTagCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound nbt) {
		this.read(nbt);
	}
}
