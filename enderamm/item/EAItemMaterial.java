package enderamm.item;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thermalexpansion.ThermalExpansion;
import thermalexpansion.block.TEBlocks;
import thermalexpansion.fluid.TEFluids;
import thermalexpansion.item.TEItems;
import thermalexpansion.util.crafting.SmelterManager;
import thermalexpansion.util.crafting.TransposerManager;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.EACommonProxy;

public class EAItemMaterial extends Item {
	@SideOnly(Side.CLIENT)
	public static List<Icon> itemTextures;

	public static List<String> itemNames;

	public EAItemMaterial(int par1) {
		super(par1);
		this.setHasSubtypes(true);
		if (FMLCommonHandler.instance().getSide().isClient())
			this.setCreativeTab(ThermalExpansion.tabTools);
		itemNames = Lists.newArrayList();
		addItem("dustEndopherum", "Endopherum Dust");
		addItem("ingotEndopherum", "Endopherum Ingot");
		addItem("lensPyrotheum", "Pyrotheum Lens");
	}

	public static void addItem(String name, String localizedName) {
		itemNames.add(name);
		LanguageRegistry.instance().addStringLocalization(
				String.format("item.%s.name", name), localizedName);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIconFromDamage(int par1) {
		return itemTextures.get(par1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		for (int i = 0; i < itemNames.size(); i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return "item." + itemNames.get(par1ItemStack.getItemDamage());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		itemTextures = Lists.newArrayList();
		for (String name : itemNames)
			itemTextures.add(par1IconRegister.registerIcon(String.format(
					"enderamm:%s", name)));
	}


}
