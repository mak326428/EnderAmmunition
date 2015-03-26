package enderamm.block;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import enderamm.EACommonProxy;
import enderamm.TEProxy;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityHMS extends TileEntity implements ISidedInventory,
        IFluidHandler, IEnergyHandler {

    public FluidTank redstoneTank = new FluidTank(10000);
    public FluidTank glowstoneTank = new FluidTank(10000);
    public FluidTank enderTank = new FluidTank(10000);

    public static final int FLUID_COST = 1000;
    public static final int RF_PER_TICK = 10000;
    public static final int OPERATION_TICKS = 100;

    public ItemStack[] inv;
    public EnergyStorage storage;

    public TileEntityHMS() {
        super();
        inv = new ItemStack[1];
        storage = new EnergyStorage(10 * 1000 * 1000, 100000, 100000);
    }

    @Override
    public int getSizeInventory() {
        return this.inv.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inv[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.inv[slot] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return "HMS";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = this.getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= amt) {
                this.setInventorySlotContents(slot, null);
            } else {
                stack = stack.splitStack(amt);
                if (stack.stackSize == 0) {
                    this.setInventorySlotContents(slot, null);
                }
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = this.getStackInSlot(slot);
        if (stack != null) {
            this.setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void closeInventory() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < this.inv.length; i++) {
            ItemStack stack = this.inv[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        par1NBTTagCompound.setTag("Inventory", itemList);

        NBTTagCompound rT = new NBTTagCompound();
        redstoneTank.writeToNBT(rT);
        par1NBTTagCompound.setTag("redstoneTank", rT);

        NBTTagCompound gT = new NBTTagCompound();
        glowstoneTank.writeToNBT(gT);
        par1NBTTagCompound.setTag("glowstoneTank", gT);

        NBTTagCompound eT = new NBTTagCompound();
        enderTank.writeToNBT(eT);
        par1NBTTagCompound.setTag("enderTank", eT);
        this.storage.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("progressReached", progress);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList tagList = par1NBTTagCompound.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < this.inv.length) {
                this.inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        redstoneTank.readFromNBT(par1NBTTagCompound
                .getCompoundTag("redstoneTank"));
        glowstoneTank.readFromNBT(par1NBTTagCompound
                .getCompoundTag("glowstoneTank"));
        enderTank.readFromNBT(par1NBTTagCompound.getCompoundTag("enderTank"));
        this.storage.readFromNBT(par1NBTTagCompound);
        progress = par1NBTTagCompound.getInteger("progressReached");
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

    public int progress;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;
        if (redstoneTank.getFluidAmount() >= FLUID_COST
                && glowstoneTank.getFluidAmount() >= FLUID_COST
                && enderTank.getFluidAmount() >= FLUID_COST
                && getEnergyStored(ForgeDirection.UNKNOWN) >= RF_PER_TICK
                && progress < OPERATION_TICKS
                && (inv[0] == null || (inv[0].getItemDamage() == 4
                && inv[0].getItem() == EACommonProxy.itemMaterial && inv[0].stackSize < 64))) {
            this.extractEnergy(ForgeDirection.UNKNOWN, RF_PER_TICK, false);
            progress++;
        }
        if (progress >= OPERATION_TICKS) {
            redstoneTank.drain(FLUID_COST, true);
            glowstoneTank.drain(FLUID_COST, true);
            enderTank.drain(FLUID_COST, true);
            if (inv[0] == null)
                inv[0] = new ItemStack(EACommonProxy.itemMaterial, 1, 4);
            else
                inv[0].stackSize++;

            progress = 0;
        }
        progressGauged = Math.round((float) progress / OPERATION_TICKS * 100);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.getFluid().getID() == TEProxy.fluidRedstone.getID())
            return redstoneTank.fill(resource, doFill);
        if (resource.getFluid().getID() == TEProxy.fluidGlowstone.getID())
            return glowstoneTank.fill(resource, doFill);
        if (resource.getFluid().getID() == TEProxy.fluidEnder.getID())
            return enderTank.fill(resource, doFill);

        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource,
                            boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid.getID() == TEProxy.fluidRedstone.getID()
                || fluid.getID() == TEProxy.fluidGlowstone.getID()
                || fluid.getID() == TEProxy.fluidEnder.getID();
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{redstoneTank.getInfo(),
                glowstoneTank.getInfo(), enderTank.getInfo()};
    }

    public void setEnergyStored(int energy) {
        storage.setEnergyStored(energy);
    }

    @Override
    public int extractEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
        return storage.extractEnergy(arg1, arg2);
    }

    @Override
    public int getEnergyStored(ForgeDirection arg0) {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection arg0) {
        return storage.getMaxEnergyStored();
    }

    @Override
    public int receiveEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
        return storage.receiveEnergy(arg1, arg2);
    }

    /**
     * 0..22
     */
    public int progressGauged;

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return true;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }
}
