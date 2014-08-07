package enderamm;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.lang.reflect.Field;

public class TEProxy {
    public static CreativeTabs tabTETools;
    public static CreativeTabs tabTEBlocks;
    public static Fluid fluidRedstone;
    public static Fluid fluidGlowstone;
    public static Fluid fluidEnder;
    public static Fluid fluidPyrotheum;
    public static ItemStack resonantEnergyCell;
    public static ItemStack tesseract;
    public static ItemStack resonantCapacitor;
    public static final String NBT_ENERGY_TAG = "Energy";
    public static ItemStack bucketPyrotheum;
    public static ItemStack dustPyrotheum;

    public static void setDefaultEnergyTag(ItemStack stack, int energy) {
        if (stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger(NBT_ENERGY_TAG, energy);
    }

    public static Object grabStaticField(String className, String fieldName) {
        try {
            Class<?> cls = Class.forName(className);
            Field f = cls.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(null);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Grab items & other stuff from TE
     */
    public static void reflect() {
        fluidRedstone = (Fluid)grabStaticField("thermalfoundation.fluid.TFFluids", "fluidRedstone");
        fluidEnder = (Fluid)grabStaticField("thermalfoundation.fluid.TFFluids", "fluidEnder");
        fluidGlowstone = (Fluid)grabStaticField("thermalfoundation.fluid.TFFluids", "fluidGlowstone");
        fluidPyrotheum = (Fluid)grabStaticField("thermalfoundation.fluid.TFFluids", "fluidPyrotheum");
        bucketPyrotheum = (ItemStack)grabStaticField("thermalfoundation.item.TFItems", "bucketPyrotheum");
        tabTETools = (CreativeTabs)grabStaticField("thermalexpansion.ThermalExpansion", "tabTools");
        tabTEBlocks = (CreativeTabs)grabStaticField("thermalexpansion.ThermalExpansion", "tabBlocks");
        tesseract = new ItemStack((Block)grabStaticField("thermalexpansion.block.TEBlocks", "blockTesseract"));
        resonantEnergyCell = new ItemStack((Block)grabStaticField("thermalexpansion.block.TEBlocks", "blockCell"), 1, 4);
        resonantCapacitor = new ItemStack((Item)grabStaticField("thermalexpansion.item.TEItems", "itemCapacitor"), 1, 5);
        dustPyrotheum = (ItemStack)grabStaticField("thermalfoundation.item.TFItems", "dustPyrotheum");
    }

    public static boolean isEnergyContainerItem(ItemStack stack) {
        return stack.getItem() instanceof IEnergyContainerItem;
    }
}
