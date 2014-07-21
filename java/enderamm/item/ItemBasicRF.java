package enderamm.item;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.EACommonProxy;
import enderamm.TEProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/**
 * Contains the routine code trivial to RF-powered tools/items.
 */
public class ItemBasicRF extends Item implements IEnergyContainerItem {
    private int transferRate, storage;

    public ItemBasicRF(int storage, int transferRate, String localizedName, String texture) {
        this.transferRate = transferRate;
        this.storage = storage;
        this.setNoRepair();
        this.setMaxStackSize(1);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.setCreativeTab(TEProxy.tabTETools);
            if (texture != null)
                this.setTextureName(texture);
        }
        String name = getClass().getSimpleName().toLowerCase();
        setUnlocalizedName(name);
        LanguageRegistry.instance().addStringLocalization(
                "item." + name + ".name", localizedName);
    }

    public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    public ItemStack getStack(int energy) {
        ItemStack result = new ItemStack(this, 1,
                0);
        result.stackTagCompound = new NBTTagCompound();
        result.stackTagCompound.setInteger("Energy", energy);
        return result;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack par1ItemStack,
                               EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(String.format("Charge: %d / %d RF",
                getEnergyStored(par1ItemStack),
                        Integer.valueOf(storage)));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs,
                            List par3List) {
        par3List.add(getStack(0));
        par3List.add(getStack(storage));
    }

    public double getDurabilityForDisplay(ItemStack stack)
    {
        return 1.0D - (double)getEnergyStored(stack) / ((double)storage + 1.0D);
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract,
                             boolean simulate) {
        if (container.stackTagCompound == null) {
            TEProxy.setDefaultEnergyTag(container, 0);
        }
        int stored = container.stackTagCompound.getInteger("Energy");
        int extract = Math.min(maxExtract, stored);

        if (!simulate) {
            stored -= extract;
            container.stackTagCompound.setInteger("Energy", stored);
        }
        return extract;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        if (container.stackTagCompound == null) {
            TEProxy.setDefaultEnergyTag(container, 0);
        }
        return container.stackTagCompound.getInteger("Energy");
    }

    @Override
    public int getMaxEnergyStored(ItemStack arg0) {
        return storage;
    }

    public int receiveEnergy(ItemStack container, int maxReceive,
                             boolean simulate) {
        if (container.stackTagCompound == null) {
            TEProxy.setDefaultEnergyTag(container, 0);
        }
        int stored = container.stackTagCompound.getInteger("Energy");
        int receive = Math.min(maxReceive,
                Math.min(storage - stored, storage));

        if (!simulate) {
            stored += receive;
            container.stackTagCompound.setInteger("Energy", stored);
        }
        return receive;
    }

    public boolean draw(ItemStack stack, int energy) {
        if (getEnergyStored(stack) >= energy) {
            extractEnergy(stack, energy, false);
            return true;
        }
        return false;
    }
}
