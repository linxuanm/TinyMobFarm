package cn.davidma.idleloot.block.container;

import cn.davidma.idleloot.tileentity.GeneratorTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class GeneratorContainer extends Container {

	private GeneratorTileEntity tileEntity;
	private int currProgress, totalProgress;
	
	public GeneratorContainer(InventoryPlayer player, GeneratorTileEntity tileEntity) {
		this.tileEntity = tileEntity;
		addSlotToContainer(new GeneratorSlot(tileEntity, 0, 80, 25));
		
		// Player hotbar slots.
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player, i, i*18 + 8, 142));
		}
		
		// PLayer inventory slots.
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 3; j++) {
				addSlotToContainer(new Slot(player, i + j*9 + 9, i*18 + 8, j*18 + 84));
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return false;
	}
}
