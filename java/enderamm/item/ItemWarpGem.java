package enderamm.item;

import java.util.List;
import java.util.Random;

import enderamm.TEProxy;
import enderamm.network.EAKeyboard;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cofh.api.energy.IEnergyContainerItem;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWarpGem extends Item implements IEnergyContainerItem {
    public static final int ENERGY_STORAGE = 10000000;
    public static final int TRANSFER_RATE = 100000;
    public static final String ENERGY_NBT = "Energy";
    public static final int RF_PER_BLOCK = 2500;
    public static final int RF_PER_PEARL = 100000;
    public IIcon mainIcon;
    public IIcon overlayIcon;
    public static int oldColor;
    public static int newColor;
    public static int stage = -1;
    public static int gemColor;
    public static int STAGES = 60;
    public static final String NBT_DIM_ID = "dimID";
    public static final String NBT_X = "x";
    public static final String NBT_Y = "y";
    public static final String NBT_Z = "z";
    public static final String NBT_COOLDOWN = "cooldown";

    public ItemWarpGem() {
        super();
        setNoRepair();
        setMaxStackSize(1);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            setCreativeTab(TEProxy.tabTETools);
        }
        setUnlocalizedName("itemWarpGem");
        LanguageRegistry.instance().addStringLocalization(
                "item.itemWarpGem.name", "Warp Gem");
    }

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

    public int getEnergyStored(ItemStack container) {
        if (container.stackTagCompound == null) {
            TEProxy.setDefaultEnergyTag(container, 0);
        }
        return container.stackTagCompound.getInteger("Energy");
    }

    public int getMaxEnergyStored(ItemStack arg0) {
        return 10000000;
    }

    public int receiveEnergy(ItemStack container, int maxReceive,
                             boolean simulate) {
        if (container.stackTagCompound == null) {
            TEProxy.setDefaultEnergyTag(container, 0);
        }
        int stored = container.stackTagCompound.getInteger("Energy");
        int receive = Math.min(maxReceive, Math.min(10000000 - stored, 100000));

        if (!simulate) {
            stored += receive;
            container.stackTagCompound.setInteger("Energy", stored);
        }
        return receive;
    }

    public int getDisplayDamage(ItemStack stack) {
        if (stack.stackTagCompound == null) {
            TEProxy.setDefaultEnergyTag(stack, 0);
        }
        return 10000001 - stack.stackTagCompound.getInteger("Energy");
    }

    public int getMaxDamage(ItemStack stack) {
        return 10000001;
    }

    public boolean isDamaged(ItemStack stack) {
        return stack.getItemDamage() != 32767;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs,
                            List par3List) {
        ItemStack charged = new ItemStack(par1, 1, 0);
        ItemStack discharged = new ItemStack(par1, 1, 0);
        discharged.stackTagCompound = new NBTTagCompound();
        charged.stackTagCompound = new NBTTagCompound();
        discharged.stackTagCompound.setInteger("Energy", 0);
        charged.stackTagCompound.setInteger("Energy", 10000000);
        par3List.add(charged);
        par3List.add(discharged);
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.epic;
    }

    public static int combine(int r, int g, int b) {
        int rgb = (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
        return rgb;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if (renderPass == 0) {
            return gemColor;
        }
        return 16777215;
    }

    public static int interpolate(float firstColor, float secondColor,
                                  float stage, float maxStages) {
        return (int) (firstColor + (secondColor - firstColor) * stage
                / maxStages);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
        this.overlayIcon = par1IconRegister
                .registerIcon("enderamm:warpGemOverlay");

        this.mainIcon = par1IconRegister.registerIcon("enderamm:warpGemMain");
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 0)
            return this.overlayIcon;
        if (pass == 1)
            return this.mainIcon;
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack,
                               EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(String.format("Charge: %d / %d RF",
                new Object[]{Integer.valueOf(getEnergyStored(par1ItemStack)),
                        Integer.valueOf(10000000)}));
        if (par1ItemStack.stackTagCompound.hasKey(NBT_DIM_ID)) {
            par3List.add("");
            par3List.add("Bound to:");
            par3List.add(String.format(
                    "[%s] %d:%d:%d",
                    new Object[]{par1ItemStack.stackTagCompound.getInteger(NBT_DIM_ID), par1ItemStack.stackTagCompound.getInteger(NBT_X),
                            par1ItemStack.stackTagCompound.getInteger(NBT_Y), par1ItemStack.stackTagCompound.getInteger(NBT_Z)}));

        }
    }

    public boolean ensureCooldown(ItemStack is) {
        if (is.getTagCompound() == null)
            is.setTagCompound(new NBTTagCompound());
        if (!is.getTagCompound().hasKey("cooldown")) {
            is.getTagCompound().setInteger("cooldown", 10);
            return true;
        }
        if (is.getTagCompound().getInteger("cooldown") == 0) {
            is.getTagCompound().setInteger("cooldown", 10);
            return true;
        }
        return false;
    }

    public void updateCooldown(ItemStack is) {
        if (is.getTagCompound() == null)
            is.setTagCompound(new NBTTagCompound());
        if (is.getTagCompound().getInteger("cooldown") > 0)
            is.getTagCompound().setInteger("cooldown",
                    is.getTagCompound().getInteger("cooldown") - 1);
    }

    public void onUpdate(ItemStack par1ItemStack, World par2World,
                         Entity par3Entity, int par4, boolean par5) {
        if (!(par3Entity instanceof EntityPlayer))
            return;
        updateCooldown(par1ItemStack);
        EntityPlayer player = (EntityPlayer) par3Entity;
        if ((!par2World.isRemote) && (ensureCooldown(par1ItemStack))
                && (player.inventory.getCurrentItem() == par1ItemStack)
                && (EAKeyboard.isPressingFire(player))
                && (getEnergyStored(par1ItemStack) >= 100000)) {
            if (!player.capabilities.isCreativeMode)
                extractEnergy(par1ItemStack, 100000, false);
            par2World.playSoundAtEntity(player, "random.bow", 0.5F,
                    0.4F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));

            par2World
                    .spawnEntityInWorld(new EntityEnderPearl(par2World, player));
        }
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world,
                                      EntityPlayer player) {
        // DimensionalLinkEntry[] entries = readEntries(itemStack);
        if (itemStack.stackTagCompound == null && !world.isRemote)
            itemStack.stackTagCompound = new NBTTagCompound();
        if (player.isSneaking()) {
            if (!world.isRemote) {
                int dimID = world.provider.dimensionId;
                // addEntry(
                // itemStack,
                // new DimensionalLinkEntry(dimID, (int) Math
                // .floor(player.posX), (int) Math
                // .floor(player.posY), (int) Math
                // .floor(player.posZ)));

                itemStack.stackTagCompound.setInteger(NBT_DIM_ID, dimID);
                itemStack.stackTagCompound.setInteger(NBT_X,
                        (int) Math.floor(player.posX));
                itemStack.stackTagCompound.setInteger(NBT_Y, (int) Math.floor(player.posY));
                itemStack.stackTagCompound.setInteger(NBT_Z, (int) Math.floor(player.posZ));

                player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN
                        + "Warp point set."));
            }
        } else {
            if (!itemStack.stackTagCompound.hasKey(NBT_DIM_ID)) {
                if (!world.isRemote)
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED
                            + "Warp point not set."));
                return itemStack;
            }
            int dimID_in_this_world = world.provider.dimensionId;
            int dimID_itemStack = itemStack.stackTagCompound.getInteger(NBT_DIM_ID);
            if (dimID_in_this_world == dimID_itemStack) {
                double targetX = itemStack.stackTagCompound.getInteger(NBT_X) + 0.5D;
                double targetY = itemStack.stackTagCompound.getInteger(NBT_Y) + 1.0D;
                double targetZ = itemStack.stackTagCompound.getInteger(NBT_Z) + 0.5D;
                EAVector3 targetVec = new EAVector3(targetX, targetY, targetZ);
                EAVector3 playerVec = EAVector3.fromEntityCenter(player);
                double distance = targetVec.subtract(playerVec).mag();
                int rfToConsume = (int) (distance * 2500.0D);
                if ((getEnergyStored(itemStack) >= rfToConsume)
                        || ((player.capabilities.isCreativeMode) && (ensureCooldown(itemStack)))) {
                    for (int i = 0; i < 128; i++) {
                        world.spawnParticle("portal", targetX, targetY,
                                targetZ, world.rand.nextGaussian(), 0.0D,
                                world.rand.nextGaussian());
                    }

                    if (!world.isRemote) {
                        if (!player.capabilities.isCreativeMode)
                            extractEnergy(itemStack, rfToConsume, false);
                        player.setPositionAndUpdate(targetX, targetY, targetZ);
                        player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN
                                + "Teleported."));
                    }
                } else {
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED
                            + "Not enough energy."));
                }
            } else {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED
                        + "Wrong dimension."));
            }

        }

        return itemStack;
    }

    public static int getR(int color) {
        return color >> 16 & 255;
    }

    public static int getG(int color) {
        return color >> 8 & 255;
    }

    public static int getB(int color) {
        return color & 255;
    }

    public static void generateWarpGemColor() {
        if (stage == -1) {
            Random rnd = new Random();
            oldColor = newColor;
            newColor = combine(rnd.nextInt(256), rnd.nextInt(256),
                    rnd.nextInt(256));
            stage = 0;
        }
        if (stage >= STAGES - 1) {
            stage = -1;
            return;
        }
        stage++;
        gemColor = combine(
                interpolate(getR(oldColor), getR(newColor), stage, STAGES),
                interpolate(getG(oldColor), getG(newColor), stage, STAGES),
                interpolate(getB(oldColor), getB(newColor), stage, STAGES));

    }

    public static int interpolateColor(int c1, int c2, int st, int sts) {
        return combine(interpolate(getR(c1), getR(c2), st, sts),
                interpolate(getG(c1), getG(c2), st, sts),
                interpolate(getB(c1), getB(c2), st, sts));
    }
}