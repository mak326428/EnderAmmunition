package enderamm;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cofh.api.energy.IEnergyContainerItem;

/**
 * @author mak326428
 * 
 */
public class ShapedRFRecipe extends ShapedOreRecipe {

	public ShapedRFRecipe(ItemStack result, Object... recipe) {
		super(result, recipe);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		return getResult(super.getCraftingResult(var1), var1);
	}

	public static ItemStack getResult(ItemStack target, InventoryCrafting var1) {
		// ItemStack target = super.getCraftingResult(var1);
		if (!(target.getItem() instanceof IEnergyContainerItem))
			return target;
		// IEnergyContainerItem item = (IEnergyContainerItem)target.getItem();
		int energyStored = 0;
		for (int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack is = var1.getStackInSlot(i);
			if (is != null && is.getItem() instanceof IEnergyContainerItem)
				energyStored += ((IEnergyContainerItem) is.getItem())
						.getEnergyStored(is);
		}
		int maxStorage = ((IEnergyContainerItem) target.getItem())
				.getMaxEnergyStored(target);
		if (energyStored > maxStorage)
			energyStored = maxStorage;
		if (target.getTagCompound() == null)
			target.setTagCompound(new NBTTagCompound());
		target.getTagCompound().setInteger("Energy", energyStored);
		return target;
	}
}
