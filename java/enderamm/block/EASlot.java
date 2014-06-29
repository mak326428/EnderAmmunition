package enderamm.block;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class EASlot extends Slot {
	public EASlot(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	public boolean isItemValid(ItemStack par1ItemStack) {
		return this.inventory
		        .isItemValidForSlot(this.slotNumber, par1ItemStack);
	}
}
