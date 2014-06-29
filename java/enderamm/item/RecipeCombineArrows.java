package enderamm.item;

import com.google.common.collect.Lists;
import enderamm.EACommonProxy;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class RecipeCombineArrows implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inventorycrafting, World world) {
        return getRes(inventorycrafting) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        return getRes(inventorycrafting);
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    public ItemStack getRes(InventoryCrafting ic) {
        ItemStack output = new ItemStack(EACommonProxy.itemEnderArrow);
        List<ItemStack> nonNullItems = Lists.newArrayList();
        for (int i = 0; i < ic.getSizeInventory(); i++) {
            ItemStack is = ic.getStackInSlot(i);
            if (is != null) {
                if (!(is.getItem() instanceof ItemEnderArrow)) {
                    return null;
                }
                nonNullItems.add(is);
            }
        }
        List<Integer> contained = Lists.newArrayList();
        for (ItemStack is : nonNullItems) {
            if (is.getTagCompound() == null)
                continue;
            if (is.getTagCompound().hasKey(ItemEnderArrow.NBT_TYPES)) {
                NBTTagList lst = is.getTagCompound().getTagList(
                        ItemEnderArrow.NBT_TYPES, Constants.NBT.TAG_COMPOUND);
                for (int tag = 0; tag < lst.tagCount(); tag++) {
                    int id = ((NBTTagCompound) lst.getCompoundTagAt(tag))
                            .getInteger(ItemEnderArrow.NBT_TYPE);
                    if (contained.contains(id))
                        return null;
                    contained.add(id);
                }
            }
        }
        if (contained.size() == 0)
            return null;
        output.setTagCompound(new NBTTagCompound());
        NBTTagList tl = new NBTTagList();
        for (int id : contained) {
            NBTTagCompound t = new NBTTagCompound();
            t.setInteger(ItemEnderArrow.NBT_TYPE, id);
            tl.appendTag(t);
        }
        output.getTagCompound().setTag(ItemEnderArrow.NBT_TYPES, tl);
        return output;
    }

}
