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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemLasso extends Item {

	public ItemLasso(Properties properties) {
		super(properties.tab(TinyMobFarm.creativeTab).defaultDurability(Config.LASSO_DURABILITY));
		this.setRegistryName(Reference.getLocation("lasso"));
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}
	
	@Override
	public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		if (NBTHelper.hasMob(stack) || !target.isAlive() || !(target instanceof MobEntity)) return ActionResultType.FAIL;
		
		CompoundNBT nbt = NBTHelper.getBaseTag(stack);
		
		// Cannot capture boss.
		if (!target.canChangeDimensions()) {
			if (!player.level.isClientSide()) {
				Msg.tellPlayer(player, "tinymobfarm.error.cannot_capture_boss");
			}
			return ActionResultType.SUCCESS;
		}
		
		// Blacklist.
		if (EntityHelper.isMobBlacklisted((LivingEntity) target)) {
			if (!player.level.isClientSide()) {
				Msg.tellPlayer(player, "tinymobfarm.error.blacklisted_mob");
			}
			return ActionResultType.SUCCESS;
		}
		
		if (!player.level.isClientSide()) {
			CompoundNBT mobData = target.serializeNBT();
			mobData.put("Rotation", NBTHelper.createNBTList(
					DoubleNBT.valueOf(0), DoubleNBT.valueOf(0)));
			
			nbt.put(NBTHelper.MOB_DATA, mobData);
			
			ITextComponent name = target.getName();
			if (name instanceof TranslationTextComponent) {
				nbt.putString(NBTHelper.MOB_NAME, ((TranslationTextComponent) name).getKey());
			} else {
				nbt.putString(NBTHelper.MOB_NAME, name.getContents());
			}
			nbt.putString(NBTHelper.MOB_LOOTTABLE_LOCATION, EntityHelper.getLootTableLocation((LivingEntity) target));
			nbt.putDouble(NBTHelper.MOB_HEALTH, Math.round(target.getHealth() * 10) / 10.0);
			nbt.putDouble(NBTHelper.MOB_MAX_HEALTH, target.getMaxHealth());
			nbt.putBoolean(NBTHelper.MOB_HOSTILE, target instanceof MonsterEntity);
			
			if (player.isCreative()) {
				ItemStack newLasso = new ItemStack(this);
				NBTHelper.setBaseTag(newLasso, nbt);
				player.addItem(newLasso);
			}
			
			target.remove();
			player.inventory.setChanged();
		}
		
		return ActionResultType.SUCCESS;
	}
		
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		Direction facing = context.getClickedFace();
		BlockPos pos = context.getClickedPos().offset(facing.getStepX(), facing.getStepY(), facing.getStepZ());
		World world = context.getLevel();
		
		if (!NBTHelper.hasMob(stack)) return ActionResultType.FAIL;
		
		if (!player.mayUseItemAt(pos, facing, stack)) return ActionResultType.FAIL;
		
		if (!context.getLevel().isClientSide()) {
			CompoundNBT nbt = NBTHelper.getBaseTag(stack);
			CompoundNBT mobData = nbt.getCompound(NBTHelper.MOB_DATA);
			
			DoubleNBT x = DoubleNBT.valueOf(pos.getX() + 0.5);
			DoubleNBT y = DoubleNBT.valueOf(pos.getY());
			DoubleNBT z = DoubleNBT.valueOf(pos.getZ() + 0.5);
			ListNBT mobPos = NBTHelper.createNBTList(x, y, z);
			mobData.put("Pos", mobPos);
			
			Entity mob = EntityType.loadEntityRecursive(mobData, world, entity -> {
				return entity;
			});
			if (mob != null) world.addFreshEntity(mob);
			
			stack.removeTagKey(NBTHelper.MOB);
			stack.hurtAndBreak(1, player, wutTheFak -> {});
		}
		
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		if (NBTHelper.hasMob(stack)) {
			CompoundNBT nbt = NBTHelper.getBaseTag(stack);
			String name = nbt.getString(NBTHelper.MOB_NAME);
			double health = nbt.getDouble(NBTHelper.MOB_HEALTH);
			double maxHealth = nbt.getDouble(NBTHelper.MOB_MAX_HEALTH);
			
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.release_mob.key"));
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.mob_name.key", I18n.get(name)));
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.health.key", health, maxHealth));
			if (nbt.getBoolean(NBTHelper.MOB_HOSTILE)) tooltip.add(Msg.tooltip("tinymobfarm.tooltip.hostile.key"));
		} else {
			tooltip.add(Msg.tooltip("tinymobfarm.tooltip.capture.key"));
		}
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return NBTHelper.hasMob(stack);
	}
}
