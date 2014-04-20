package enderamm;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thermalexpansion.block.TEBlocks;
import thermalexpansion.fluid.TEFluids;
import thermalexpansion.item.ItemTEBase;
import thermalexpansion.item.TEItems;
import thermalexpansion.util.crafting.SmelterManager;
import thermalexpansion.util.crafting.TransposerManager;
import cpw.mods.fml.common.registry.GameRegistry;
import enderamm.item.ItemAnnihilationManipulator;

public class EAInit {
	public static void addRecipes() {
		// Enderium Blend -> Endopherum Dust
		TransposerManager.addFillRecipe(20000, TEItems.dustEnderium.copy(),
				new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 0),
				new FluidStack(TEFluids.fluidPyrotheum, 2000), false);
		// Glass -> Pyrotheum Lens
		TransposerManager.addFillRecipe(40000, new ItemStack(Block.glass),
				new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 2),
				new FluidStack(TEFluids.fluidPyrotheum, 8000), false);
		// 16 Endopherum Dust -> Endopherum Ingot
		ItemStack endopherumDusts = new ItemStack(
				EACommonProxy.itemMaterial.itemID, 16, 0);
		SmelterManager.addRecipe(40000, TEItems.dustPyrotheum.copy(),
				endopherumDusts, new ItemStack(
						EACommonProxy.itemMaterial.itemID, 1, 1));
		// Armor Recipes
		ItemStack energyCapacitor = new ItemStack(TEItems.itemCapacitor, 1, 5);
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemArmorEnderChestplate), "#C#", "###", "###",
				'#', new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 1),
				'C', energyCapacitor));
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemArmorEnderLeggings), "###", "#C#", "# #",
				'#', new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 1),
				'C', energyCapacitor));
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemArmorEnderHelmet), "#P#", "#C#", '#',
				new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 1), 'C',
				energyCapacitor, 'P', new ItemStack(
						EACommonProxy.itemMaterial.itemID, 1, 2)));
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemArmorEnderBoots), "#C#", "# #", '#',
				new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 1), 'C',
				energyCapacitor));
		// GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
		// EACommonProxy.itemMaterial.itemID, 1, 3), " # ", " # ", '#',
		// new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 1)));
		// Warp Gem recipe
		GameRegistry.addRecipe(new ShapelessRFRecipe(new ItemStack(
				EACommonProxy.itemWarpGem), new ItemStack(
				EACommonProxy.itemWarpGem, 1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemWarpGem), "III", "ERE", "III", 'I',
				new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 1), 'E',
				new ItemStack(Item.enderPearl), 'R', new ItemStack(
						TEBlocks.blockEnergyCell, 1, 4)));
		// Annihilation Manipulator Recipe
		GameRegistry.addRecipe(new ShapedRFRecipe(ItemAnnihilationManipulator
				.getAnnihilationManipulator(0), "LJL", "IWI", "IRI", 'L',
				new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 2), 'W',
				new ItemStack(EACommonProxy.itemWarpGem), 'I', new ItemStack(
						EACommonProxy.itemMaterial.itemID, 1, 1), 'R',
				energyCapacitor, 'J', new ItemStack(
						EACommonProxy.itemMaterial.itemID, 1, 3)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				EACommonProxy.itemMaterial.itemID, 1, 3), "ETE", "COC", "ERE",
				'O', TEBlocks.blockTesseract, 'R', TEItems.itemComponent, 'T',
				TEItems.itemComponent, 'E', new ItemStack(
						EACommonProxy.itemMaterial.itemID, 1, 1), 'C',
				TEItems.itemComponent));
		// HealingIIPotion FluxKinesisThingy RegenIIPotion
		// EndopherumIngot PyrotheumLens EndopherumIngot
		// EndopherumIngot FluxCapacitor EndopherumIngot
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemHealingGem), "HJR", "EPE", "ECE", 'E',
				new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 1), 'C',
				energyCapacitor, 'P', new ItemStack(
						EACommonProxy.itemMaterial.itemID, 1, 2), 'J',
				new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 3), 'H',
				new ItemStack(Item.potion.itemID, 1, 8229), 'R', new ItemStack(
						Item.potion.itemID, 1, 8225)));
	}
}
