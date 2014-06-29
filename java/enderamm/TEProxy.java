package enderamm;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class TEProxy {
    public static CreativeTabs tabTETools = CreativeTabs.tabTools;
    public static CreativeTabs tabTEBlocks = CreativeTabs.tabBlock;
    public static Fluid fluidRedstone = FluidRegistry.WATER;
    public static Fluid fluidGlowstone = FluidRegistry.LAVA;
    public static Fluid fluidEnder = FluidRegistry.getFluid("oil");

    public static final String NBT_ENERGY_TAG = "Energy";

    public static final ItemStack bucketPyrotheum = new ItemStack(Items.bucket); // TODO: TEFluids.bucketPyrotheum

    public static void setDefaultEnergyTag(ItemStack stack, int energy) {
        if (stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger(NBT_ENERGY_TAG, energy);
    }

    public static boolean isEnergyContainerItem(ItemStack stack) {
        return stack.getItem() instanceof IEnergyContainerItem;
    }
}
