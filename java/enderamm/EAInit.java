package enderamm;

import cpw.mods.fml.common.registry.GameRegistry;
import enderamm.item.ItemAnnihilationManipulator;
import enderamm.item.ItemEnderArrow;
import enderamm.item.ItemEnderArrow.IEnderEffect;
import enderamm.item.RecipeCombineArrows;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.lang.reflect.Method;
import java.util.List;

public class EAInit {
    // TODO: uncomment and port when TE is released
    public static void addMatterRecipes() {
        ItemStack matter = new ItemStack(EACommonProxy.itemMaterial, 1,
				4);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.diamond,
                2), "MMM", "M M", "MMM", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.emerald,
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
                Items.glowstone_dust, 28), " MM", "M  ", "   ", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				Items.ender_pearl, 12), " M ", "M  ", "   ", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.redstone,
				56), " M ", "  M", "   ", 'M', matter));
        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.log, 12),
                        "   ", " M ", "   ", 'M', matter));
        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(Items.coal, 56),
                        "M  ", " M ", "M M", 'M', matter));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.iron_ore,
				24), "M M", " M ", "M M", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.gold_ore,
				8), " M ", "M M", " M ", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				EACommonProxy.itemMaterial, 1, 0), "MPM", 'M', matter,
				'P', TEProxy.dustPyrotheum));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				Blocks.obsidian, 24, 0), " M ", "M M", "   ", 'M', matter));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				Blocks.dragon_egg), "MMM", "MNM", "MMM", 'M', matter, 'N',
				new ItemStack(Blocks.beacon)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.skull, 1,
				1), "MMM", "MSM", "MMM", 'M', matter, 'S', new ItemStack(
				Items.skull, 1, 0)));
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

    public static void addTransposerFillRecipe(int paramInt, ItemStack paramItemStack1, ItemStack paramItemStack2, FluidStack paramFluidStack, boolean paramBoolean) {
        try {
            Class<?> cls = Class.forName("thermalexpansion.util.crafting.TransposerManager");
            Method m = cls.getMethod("addFillRecipe", int.class, ItemStack.class, ItemStack.class, FluidStack.class, boolean.class);
            m.invoke(null, paramInt, paramItemStack1, paramItemStack2, paramFluidStack, paramBoolean);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static void addRecipes() {
		// Glass -> Pyrotheum Lens
		addTransposerFillRecipe(40000, new ItemStack(Blocks.glass),
				new ItemStack(EACommonProxy.itemMaterial, 1, 2),
				new FluidStack(TEProxy.fluidPyrotheum, 8000), false);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				EACommonProxy.itemMaterial, 1, 1), "NNN", "NNN", "NNN", 'N',
				new ItemStack(EACommonProxy.itemMaterial, 1, 0)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(
				EACommonProxy.itemMaterial, 9, 0), new ItemStack(
				EACommonProxy.itemMaterial, 1, 1)));
		// HMS
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				EACommonProxy.blockHMS, 1, 0), "ENE", "BRB", "EDE", 'E',
				"ingotEnderium", 'N', new ItemStack(Items.nether_star), 'D',
				new ItemStack(Blocks.dragon_egg), 'R', TEProxy.resonantEnergyCell, 'B', new ItemStack(
						Blocks.emerald_block)));
		// Armor Recipes
		ItemStack energyCapacitor = TEProxy.resonantCapacitor;
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemArmorEnderChestplate), "#C#", "###", "###",
				'#', new ItemStack(EACommonProxy.itemMaterial, 1, 1),
				'C', energyCapacitor));
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemArmorEnderLeggings), "###", "#C#", "# #",
				'#', new ItemStack(EACommonProxy.itemMaterial, 1, 1),
				'C', energyCapacitor));
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemArmorEnderHelmet), "#P#", "#C#", '#',
				new ItemStack(EACommonProxy.itemMaterial, 1, 1), 'C',
				energyCapacitor, 'P', new ItemStack(
						EACommonProxy.itemMaterial, 1, 2)));
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemArmorEnderBoots), "#C#", "# #", '#',
				new ItemStack(EACommonProxy.itemMaterial, 1, 1), 'C',
				energyCapacitor));
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemEnderMagnet), "EPE", "PRP", "EPE", 'E',
				new ItemStack(EACommonProxy.itemMaterial, 1, 1), 'R',
				energyCapacitor, 'P', new ItemStack(Items.ender_pearl)));
		//GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
		// EACommonProxy.itemMaterial, 1, 3), " # ", " # ", '#',
		// new ItemStack(EACommonProxy.itemMaterial, 1, 1)));
		GameRegistry.addRecipe(new ShapelessRFRecipe(new ItemStack(
				EACommonProxy.itemWarpGem), new ItemStack(
				EACommonProxy.itemWarpGem, 1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemWarpGem), "III", "ERE", "III", 'I',
				new ItemStack(EACommonProxy.itemMaterial, 1, 1), 'E',
				new ItemStack(Items.ender_pearl), 'R', TEProxy.resonantEnergyCell));
		// Annihilation Manipulator Recipe
		GameRegistry.addRecipe(new ShapedRFRecipe(ItemAnnihilationManipulator
				.getAnnihilationManipulator(0), "LJL", "IWI", "IRI", 'L',
				new ItemStack(EACommonProxy.itemMaterial, 1, 2), 'W',
				new ItemStack(EACommonProxy.itemWarpGem), 'I', new ItemStack(
						EACommonProxy.itemMaterial, 1, 1), 'R',
				energyCapacitor, 'J', new ItemStack(
						EACommonProxy.itemMaterial, 1, 3)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				EACommonProxy.itemMaterial, 1, 3), "ETE", "COC", "ERE",
				'O', TEProxy.tesseract, 'R', TEProxy.tesseract, 'T',
                TEProxy.tesseract, 'E', new ItemStack(
						EACommonProxy.itemMaterial, 1, 1), 'C',
                TEProxy.tesseract));
		GameRegistry.addRecipe(new ShapedRFRecipe(new ItemStack(
				EACommonProxy.itemHealingGem), "HJR", "EPE", "ECE", 'E',
				new ItemStack(EACommonProxy.itemMaterial, 1, 1), 'C',
				energyCapacitor, 'P', new ItemStack(
						EACommonProxy.itemMaterial, 1, 2), 'J',
				new ItemStack(EACommonProxy.itemMaterial, 1, 3), 'H',
				new ItemStack(Items.potionitem, 1, 8229), 'R', new ItemStack(
						Items.potionitem, 1, 8225)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				EACommonProxy.itemEnderBow), "DE ", "DBE", "DE ", 'D',
				new ItemStack(Blocks.dragon_egg), 'B',
				new ItemStack(Items.bow, 1), 'E', new ItemStack(
						EACommonProxy.itemMaterial, 1, 1)));
        EnderEffects.init();
        for (IEnderEffect eff : ItemEnderArrow.registeredEffects) {
            ItemStack target = new ItemStack(EACommonProxy.itemEnderArrow);
            target.setTagCompound(new NBTTagCompound());
            NBTTagList tl = new NBTTagList();
            NBTTagCompound t = new NBTTagCompound();
            t.setInteger(ItemEnderArrow.NBT_TYPE,
                    ItemEnderArrow.registeredEffects.indexOf(eff));
            tl.appendTag(t);
            target.getTagCompound().setTag(ItemEnderArrow.NBT_TYPES, tl);
            eff.addRecipe(target);
        }
        addMatterRecipes();
        CraftingManager.getInstance().getRecipeList()
                .add(new RecipeCombineArrows());
    }
}
