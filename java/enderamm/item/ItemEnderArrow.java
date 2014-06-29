package enderamm.item;

import java.util.List;

import enderamm.TEProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.EACommonProxy;
import enderamm.EnderEffects;
import net.minecraftforge.common.util.Constants;

public class ItemEnderArrow extends Item {

	public static String NBT_TYPE = "type";
	public static String NBT_TYPES = "types";

	public static interface IEnderEffect {
		void addRecipe(ItemStack target);

		void apply(Entity e, EntityPlayer shooter, ItemStack arrowStack, World w);

		String getName();
	}

	public static List<IEnderEffect> registeredEffects = Lists.newArrayList();

	public ItemEnderArrow() {
		super();
		setNoRepair();
		setMaxStackSize(64);
		if (FMLCommonHandler.instance().getSide().isClient()) {
			setCreativeTab(TEProxy.tabTETools);
			setTextureName("enderamm:enderArrow");
		}
		setUnlocalizedName("itemEnderArrow");
		LanguageRegistry.instance().addStringLocalization(
				"item.itemEnderArrow.name", "Ender-Infused Arrow");
	}

	public void apply(Entity e, EntityPlayer ep, ItemStack arrowStack, World w) {
		e.attackEntityFrom(DamageSource.causePlayerDamage(ep), 10);
		if (arrowStack.getTagCompound().hasKey(NBT_TYPES)) {
			NBTTagList lst = arrowStack.getTagCompound().getTagList(NBT_TYPES, Constants.NBT.TAG_COMPOUND);
			for (int tag = 0; tag < lst.tagCount(); tag++) {
				int id = ((NBTTagCompound) lst.getCompoundTagAt(tag)).getInteger(NBT_TYPE);
				e.hurtResistantTime = 0;
				registeredEffects.get(id).apply(e, ep, arrowStack, w);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		boolean noEff = true;
		if (par1ItemStack.stackTagCompound != null) {
			if (par1ItemStack.getTagCompound().hasKey(NBT_TYPES)) {
				NBTTagList lst = par1ItemStack.getTagCompound().getTagList(
						NBT_TYPES, Constants.NBT.TAG_COMPOUND);
				for (int tag = 0; tag < lst.tagCount(); tag++) {
					int id = ((NBTTagCompound) lst.getCompoundTagAt(tag))
							.getInteger(NBT_TYPE);
					par3List.add(registeredEffects.get(id).getName());
				}
				noEff = false;
			}
		}
		if (noEff)
			par3List.add(EnumChatFormatting.DARK_RED + "No effect");
	}

    @Override
    @SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		for (IEnderEffect eff : registeredEffects) {
			ItemStack target = new ItemStack(EACommonProxy.itemEnderArrow);
			target.setTagCompound(new NBTTagCompound());
			NBTTagList tl = new NBTTagList();
			NBTTagCompound t = new NBTTagCompound();
			t.setInteger(ItemEnderArrow.NBT_TYPE,
					ItemEnderArrow.registeredEffects.indexOf(eff));
			tl.appendTag(t);
			target.getTagCompound().setTag(ItemEnderArrow.NBT_TYPES, tl);
			par3List.add(target);
		}
	}
}
