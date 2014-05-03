package enderamm.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import thermalexpansion.ThermalExpansion;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EAItemMaterial extends Item {
	@SideOnly(Side.CLIENT)
	public static List<Icon> itemTextures;

	public static List<String> itemNames;
	public static List<Integer> rareItems;

	public EAItemMaterial(int par1) {
		super(par1);
		this.setHasSubtypes(true);
		if (FMLCommonHandler.instance().getSide().isClient())
			this.setCreativeTab(ThermalExpansion.tabTools);
		itemNames = Lists.newArrayList();
		rareItems = Lists.newArrayList();
		addItem("nuggetEndopherum", "Endopherum Nugget"); // 0
		rareItems.add(0);
		addItem("ingotEndopherum", "Endopherum Ingot"); // 1
		rareItems.add(1);
		addItem("lensPyrotheum", "Pyrotheum Lens"); // 2
		rareItems.add(2);
		addItem("rfKineticPreprocessor", "Flux-Kinesis Preprocessor"); // 3
		rareItems.add(3);
		addItem("energeticallyEnrichedMatterFragment",
				"Energetically Enriched Matter Fragment"); // 4
		rareItems.add(4);

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
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return rareItems.contains(par1ItemStack.getItemDamage()) ? EnumRarity.epic
				: EnumRarity.common;
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

	/*public static ItemStack generateRandomItem() {
		Item b = null;
		while (b == null)
			b = Item.itemsList[itemRand.nextInt(255) + 1];
		List<ItemStack> lst = Lists.newArrayList();
		b.getSubItems(b.itemID, CreativeTabs.tabAllSearch, lst);
		if (lst.size() > 0) {
			return lst.get(itemRand.nextInt(lst.size()));
		} else {
			return null;
		}
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		World w = entityItem.worldObj;
		if (!w.isRemote && entityItem.getEntityItem().getItemDamage() == 4) {
			// w.createExplosion(entityItem, entityItem.posX, entityItem.posY,
			// entityItem.posZ,
			// (float) itemRand.nextInt(entityItem.getEntityItem().stackSize *
			// 200) / 1000.0F, false);
			for (int i = 0; i < entityItem.getEntityItem().stackSize; i++) {
				NBTTagCompound nbt = new NBTTagCompound();
				entityItem.writeToNBT(nbt);
				EntityItem entity = new EntityItem(w);
				entity.readFromNBT(nbt);
				entity.setEntityItemStack(generateRandomItem());
				entity.delayBeforeCanPickup = 60;
				w.spawnEntityInWorld(entity);
			}
			entityItem.setDead();

		}
		return false;
	}*/

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		itemTextures = Lists.newArrayList();
		for (String name : itemNames)
			itemTextures.add(par1IconRegister.registerIcon(String.format(
					"enderamm:%s", name)));
	}

}
