package enderamm.item;

import cofh.api.energy.IEnergyHandler;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.Field;
import java.util.List;

public class ItemRFDebug extends ItemBasicRF {
    public ItemRFDebug() {
        super(Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 4, "RF Debug Item", "enderamm:rfDebugItem");
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (!FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            if (stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
            Mode mode = Mode.values()[stack.getTagCompound().getInteger("mode")];
            Block targetBlock = world.getBlock(x, y, z);
            TileEntity targetTile = world.getTileEntity(x, y, z);
            ForgeDirection hit = ForgeDirection.getOrientation(side);
            if (targetTile == null) {
                player.addChatMessage(new ChatComponentText("The block you're targeting at doesn't have a tile entity!"));
                return true;
            }
            if (mode == Mode.FILL_MACHINE_WITH_RF) {
                if (!(targetTile instanceof IEnergyHandler)) {
                    player.addChatMessage(new ChatComponentText("The tile entity you're targeting at doesn't support RF (technically it doesn't implement IEnergyHandler)"));
                    return true;
                }
                IEnergyHandler handler = (IEnergyHandler) targetTile;
                int attempts = 1000;
                for (int i = 0; i < attempts; i++) {
                    int toFill = handler.getMaxEnergyStored(hit) - handler.getEnergyStored(hit);
                    handler.receiveEnergy(hit, toFill, false);
                }
            } else if (mode == Mode.LIST_MACHINE_INTERFACES) {
                Class clazz = targetTile.getClass();
                player.addChatMessage(new ChatComponentText("TileEntity class " + targetTile.getClass().getCanonicalName() + " implements following interfaces (including superclasses):"));
                while (!clazz.equals(Object.class)) {
                    Class[] interfaces = clazz.getInterfaces();
                    for (Class iface : interfaces) {
                        player.addChatMessage(new ChatComponentText("    " + iface.getCanonicalName()));
                    }
                    clazz = clazz.getSuperclass();
                }
            } else if (mode == Mode.LIST_MACHINE_VARIABLES) {
                Class clazz = targetTile.getClass();
                List<String> names = Lists.newArrayList();
                player.addChatMessage(new ChatComponentText("Dumping all the variables of targeted TE (using .toString())"));
                while (!clazz.equals(Object.class)) {
                    Field[] fields = clazz.getFields();
                    for (Field f : fields) {
                        if (!names.contains(f.getName())) {
                            try {
                                f.setAccessible(true);
                                player.addChatMessage(new ChatComponentText("    " + f.getName() + ": " + f.get(targetTile)));
                            } catch (Throwable t) {
                                player.addChatMessage(new ChatComponentText("Exception caught: " + t.getClass().getCanonicalName()));
                            }
                            names.add(f.getName());
                        }
                    }
                    clazz = clazz.getSuperclass();
                }
            }
        }
        return true;
    }

    public enum Mode {
        FILL_MACHINE_WITH_RF("Fill machine's buffer with RF"),
        LIST_MACHINE_INTERFACES("List the interfaces of targeted TE"),
        LIST_MACHINE_VARIABLES("Dump the variables of targeted TE (both public and private) via reflection"),;

        private final String desc;

        private Mode(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (!par2World.isRemote && par3EntityPlayer.isSneaking()) {
            if (par1ItemStack.stackTagCompound == null) par1ItemStack.stackTagCompound = new NBTTagCompound();
            par1ItemStack.stackTagCompound.setInteger("mode", par1ItemStack.getTagCompound().getInteger("mode") + 1);
            if (par1ItemStack.stackTagCompound.getInteger("mode") >= Mode.values().length)
                par1ItemStack.stackTagCompound.setInteger("mode", 0);
            par3EntityPlayer.addChatMessage(new ChatComponentText("New mode: " + Mode.values()[par1ItemStack.stackTagCompound.getInteger("mode")].getDesc()));
        }
        return par1ItemStack;
    }
}
