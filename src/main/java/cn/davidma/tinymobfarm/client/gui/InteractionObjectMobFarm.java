package cn.davidma.tinymobfarm.client.gui;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;

public class InteractionObjectMobFarm implements IInteractionObject {

	private TileEntityMobFarm tileEntityMobFarm;
	
	public InteractionObjectMobFarm(TileEntityMobFarm tileEntityMobFarm) {
		this.tileEntityMobFarm = tileEntityMobFarm;
	}
	
	@Override
	public ITextComponent getCustomName() {
		return null;
	}

	@Override
	public ITextComponent getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInv, EntityPlayer arg1) {
		return new ContainerMobFarm(playerInv, this.tileEntityMobFarm);
	}

	@Override
	public String getGuiID() {
		return Reference.MOD_ID + ":mob_farm_gui";
	}

}
