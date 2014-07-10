package enderamm;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 10.07.14
 * Time: 10:14
 * To change this template use File | Settings | File Templates.
 */

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class LogicSlot {

    private final IInventory boundInventory;
    private final int index;

    public LogicSlot(IInventory inv, int index) {
        this.boundInventory = inv;
        this.index = index;
    }

    public boolean isValid(ItemStack stack) {
        return this.boundInventory.isItemValidForSlot(index, stack);
    }

    public boolean isItemTheSame(ItemStack stack) {
        return stack == null ? false : stack.getItem() == get().getItem()
                && stack.getItemDamage() == get().getItemDamage();
    }

    public boolean canConsume(int amount) {
        if (get() == null)
            return false;
        ItemStack st = get();
        return st.stackSize >= amount;
    }

    public boolean canAdd(int amount) {
        if (get() == null && amount <= boundInventory.getInventoryStackLimit())
            return true;
        ItemStack alreadyThere = get();
        if (boundInventory.getInventoryStackLimit() - alreadyThere.stackSize >= amount)
            return true;
        return false;
    }

    public IInventory getInventory() {
        return boundInventory;
    }

    public void add(int amount) {
        if (get() == null)
            throw new NullPointerException(
                    "HelperLogicSlot: add(): get() returns null");
        ItemStack alreadyInThere = get().copy();
        alreadyInThere.stackSize += amount;
        if (alreadyInThere.stackSize <= 0) {
            this.boundInventory.setInventorySlotContents(index, null);
            return;
        }
        this.boundInventory.setInventorySlotContents(index, alreadyInThere);
    }

    public void consume(int amount) {
        add(-amount);
    }

    public ItemStack get() {
        return boundInventory.getStackInSlot(index);
    }

    /**
     * Null if successful
     * TODO: make NBT-sensitive
     * @param what
     * @param simulate
     * @return leftover
     */
    public ItemStack add(ItemStack what, boolean simulate) {
        if (get() != null) {
            if (get().getItemDamage() != what.getItemDamage()
                    || get().getItem() != what.getItem()) {
                return what;
            } else {
                int freeSpace = what.getItem().getItemStackLimit(what)
                        - get().stackSize;
                if (freeSpace >= what.stackSize) {
                    if (!simulate) {
                        ItemStack newIS = get().copy();
                        newIS.stackSize += what.stackSize;
                        boundInventory.setInventorySlotContents(index,
                                newIS.copy());
                        return null;
                    }
                    return null;
                } else {
                    ItemStack leftover = what.copy();
                    leftover.stackSize -= (what.getMaxStackSize() - get().stackSize);
                    if (!simulate) {
                        ItemStack toSet = get().copy();
                        toSet.stackSize = what.getItem().getItemStackLimit(what);
                        set(toSet);
                    }
                    return leftover;
                }
            }
        } else {
            if (!simulate)
                boundInventory.setInventorySlotContents(index, what.copy());
            return null;
        }
    }

    public void set(ItemStack is) {
        boundInventory.setInventorySlotContents(index, is);
    }

}