package cn.davidma.tinymobfarm.common.item;

import java.util.List;

import cn.davidma.tinymobfarm.common.TinyMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import cn.davidma.tinymobfarm.core.util.Config;
import cn.davidma.tinymobfarm.core.util.EntityHelper;
import cn.davidma.tinymobfarm.core.util.Msg;
import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemLasso extends Item {

	public ItemLasso(Properties properties) {
		super(properties.group(TinyMobFarm.creativeTab));
		this.setRegistryName(Reference.getLocation("lasso"));
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		return Config.LASSO_DURABILITY;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}
	
	@Override
	public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		if (NBTHelper.hasMob(stack) || !target.isAlive() || !(target instanceof MobEntity)) return ActionResultType.FAIL;
		
		CompoundNBT nbt = NBTHelper.getBaseTag(stack);
		
		// Cannot capture boss.
		if (!target.isNonBoss()) {
			if (!player.world.isRemote) {
				Msg.tellPlayer(player, "tinymobfarm.error.cannot_capture_boss");
			}
			return ActionResultType.SUCCESS;
		}
		
		// Blacklist.
		if (EntityHelper.isMobBlacklisted((LivingEntity) target)) {
			if (!player.world.isRemote) {
				Msg.tellPlayer(player, "tinymobfarm.error.blacklisted_mob");
			}
			return ActionResultType.SUCCESS;
		}
		
		if (!player.world.isRemote()) {
			CompoundNBT mobData = target.serializeNBT();
			mobData.put("Rotation", NBTHelper.createNBTList(
					DoubleNBT.valueOf(0), DoubleNBT.valueOf(0)));
			
			nbt.put(NBTHelper.MOB_DATA, mobData);
			
			ITextComponent name = target.getName();
			if (name instanceof TranslationTextComponent) {
				nbt.putString(NBTHelper.MOB_NAME, ((TranslationTextComponent) name).getKey());
			} else {
				nbt.putString(NBTHelper.MOB_NAME, name.getUnformattedComponentText());
			}
			nbt.putString(NBTHelper.MOB_LOOTTABLE_LOCATION, EntityHelper.getLootTableLocation((LivingEntity) target));
			nbt.putDouble(NBTHelper.MOB_HEALTH, Math.round(target.getHealth() * 10) / 10.0);
			nbt.putDouble(NBTHelper.MOB_MAX_HEALTH, target.getMaxHealth());
			nbt.putBoolean(NBTHelper.MOB_HOSTILE, target instanceof MonsterEntity);
			
			if (player.isCreative()) {
				ItemStack newLasso = new ItemStack(this);
				NBTHelper.setBaseTag(newLasso, nbt);
				player.addItemStackToInventory(newLasso);
			}
			
			target.remove();
			player.inventory.markDirty();
		}
		
		return ActionResultType.SUCCESS;
	}
		
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		ItemStack stack = context.getItem();
		Direction facing = context.getFace();
		BlockPos pos = context.getPos().offset(facing);
		World world = context.getWorld();
		
		if (!NBTHelper.hasMob(stack)) return ActionResultType.FAIL;
		
		if (!player.canPlayerEdit(pos, facing, stack)) return ActionResultType.FAIL;
		
		if (!context.getWorld().isRemote()) {
			CompoundNBT nbt = NBTHelper.getBaseTag(stack);
			CompoundNBT mobData = nbt.getCompound(NBTHelper.MOB_DATA);

			EntityType.readEntityType(mobData)
				.filter(entityType -> !EntityHelper.isMobBlacklisted(entityType))
				.map(entityType -> entityType.create(world))
				.filter(entity -> entity instanceof MobEntity && canPlayerSetNbt(world, entity, player))
				.ifPresent(mob -> {
					DoubleNBT x = DoubleNBT.valueOf(pos.getX() + 0.5);
					DoubleNBT y = DoubleNBT.valueOf(pos.getY());
					DoubleNBT z = DoubleNBT.valueOf(pos.getZ() + 0.5);
					ListNBT mobPos = NBTHelper.createNBTList(x, y, z);
					mobData.put("Pos", mobPos);
					mob.read(mobData);
					world.addEntity(mob);
				});

			stack.removeChildTag(NBTHelper.MOB);
			stack.damageItem(1, player, wutTheFak -> {});
		}
		
		return ActionResultType.SUCCESS;
	}

	private boolean canPlayerSetNbt(World world, Entity entity, @Nullable PlayerEntity player) {
		MinecraftServer server = world.getServer();
		return server == null || !entity.ignoreItemEntityData() || player != null && server.getPlayerList().canSendCommands(player.getGameProfile());
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		if (NBTHelper.hasMob(stack)) {
			CompoundNBT nbt = NBTHelper.getBaseTag(stack);
			String name = nbt.getString(NBTHelper.MOB_NAME);
			double health = nbt.getDouble(NBTHelper.MOB_HEALTH);
			double maxHealth = nbt.getDouble(NBTHelper.MOB_MAX_HEALTH);

			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.release_mob.key"));
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.mob_name.key", I18n.format(name)));
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.health.key", health, maxHealth));
			if (nbt.getBoolean(NBTHelper.MOB_HOSTILE)) tooltip.add(Msg.tooltip("tinymobfarm.tooltip.hostile.key"));
		} else {
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.capture.key"));
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return NBTHelper.hasMob(stack);
	}
}
