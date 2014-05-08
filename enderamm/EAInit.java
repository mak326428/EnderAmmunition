package enderamm;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import thermalexpansion.block.TEBlocks;
import thermalexpansion.fluid.TEFluids;
import thermalexpansion.item.TEItems;
import thermalexpansion.util.crafting.TransposerManager;
import cpw.mods.fml.common.registry.GameRegistry;
import enderamm.item.ItemAnnihilationManipulator;

public class EAInit {

	public static void addMatterRecipes() {
		ItemStack matter = new ItemStack(EACommonProxy.itemMaterial.itemID, 1,
				4);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Item.diamond,
				2), "MMM", "M M", "MMM", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Item.emerald,
				1), "MMM", "MMM", "MMM", 'M', matter));
		{
			ItemStack ore = getIfAny("oreCopper", 32);
			if (ore != null)
				GameRegistry.addRecipe(new ShapedOreRecipe(ore, "M  ", "M  ",
						"  M", 'M', matter));
		}
		{
			ItemStack ore = getIfAny("oreTin", 24);
			if (ore != null)
				GameRegistry.addRecipe(new ShapedOreRecipe(ore, "M  ", "M  ",
						" M ", 'M', matter));
		}
		{
			ItemStack ore = getIfAny("oreLead", 10);
			if (ore != null)
				GameRegistry.addRecipe(new ShapedOreRecipe(ore, "M  ", " M ",
						" M ", 'M', matter));
		}
		{
			ItemStack ore = getIfAny("gemRuby", 3);
			if (ore != null)
				GameRegistry.addRecipe(new ShapedOreRecipe(ore, "MM ", " M ",
						" MM", 'M', matter));
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				Item.glowstone, 28), " MM", "M  ", "   ", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				Item.enderPearl, 12), " M ", "M  ", "   ", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Item.redstone,
				56), " M ", "  M", "   ", 'M', matter));
		GameRegistry
				.addRecipe(new ShapedOreRecipe(new ItemStack(Block.wood, 12),
						"   ", " M ", "   ", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Block.oreIron,
				24), "M M", " M ", "M M", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Block.oreGold,
				8), " M ", "M M", " M ", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				EACommonProxy.itemMaterial.itemID, 1, 0), "MPM", 'M', matter,
				'P', new ItemStack(TEItems.itemMaterial, 1, 165)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				Block.obsidian, 24, 0), " M ", "M M", "   ", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				Block.dragonEgg), "MMM", "MNM", "MMM", 'M', matter, 'N',
				new ItemStack(Block.beacon)));
	}

	public static ItemStack getIfAny(String oreDictName, int stackSize) {
		List<ItemStack> oreDictRetr = OreDictionary.getOres(oreDictName);
		if (oreDictRetr.size() > 0) {
			ItemStack ret = oreDictRetr.get(0).copy();
			if (ret != null) {
				ret.stackSize = stackSize;
				return ret;
			}
		}

		return null;
	}

	public static void addRecipes() {
		// Glass -> Pyrotheum Lens
		TransposerManager.addFillRecipe(40000, new ItemStack(Block.glass),
				new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 2),
				new FluidStack(TEFluids.fluidPyrotheum, 8000), false);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				EACommonProxy.itemMaterial, 1, 1), "NNN", "NNN", "NNN", 'N',
				new ItemStack(EACommonProxy.itemMaterial, 1, 0)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(
				EACommonProxy.itemMaterial, 9, 0), new ItemStack(
				EACommonProxy.itemMaterial, 1, 1)));
		// HMS
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				EACommonProxy.blockHMS, 1, 0), "ENE", "BRB", "EDE", 'E',
				"ingotEnderium", 'N', new ItemStack(Item.netherStar), 'D',
				new ItemStack(Block.dragonEgg), 'R', new ItemStack(
						TEBlocks.blockEnergyCell, 1, 4), 'B', new ItemStack(
						Block.blockEmerald)));
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
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemHealingGem), "HJR", "EPE", "ECE", 'E',
				new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 1), 'C',
				energyCapacitor, 'P', new ItemStack(
						EACommonProxy.itemMaterial.itemID, 1, 2), 'J',
				new ItemStack(EACommonProxy.itemMaterial.itemID, 1, 3), 'H',
				new ItemStack(Item.potion.itemID, 1, 8229), 'R', new ItemStack(
						Item.potion.itemID, 1, 8225)));
		addMatterRecipes();
	}
}
