package enderamm;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * @author mak326428
 * 
 */
public class ShapelessRFRecipe extends ShapelessOreRecipe {

	public ShapelessRFRecipe(ItemStack result, Object... recipe) {
		super(result, recipe);
	}
	
	@Override
    public ItemStack getCraftingResult(InventoryCrafting var1){
		return ShapedRFRecipe.getResult(super.getCraftingResult(var1), var1);
	}

}
