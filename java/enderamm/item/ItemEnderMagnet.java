package enderamm.item;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.EACommonProxy;
import enderamm.EAUtil;
import enderamm.TEProxy;
import enderamm.network.EAKeyboard;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemEnderMagnet extends Item implements IEnergyContainerItem {

    public static final int MAX_STORAGE = 1000000;
    public static final int TRANSFER_RATE = 50000;
    public static final int RF_TICK_DRAW = 100;
    public static final int RF_ENTITY_INTERACTON = 10000;
    public static final String NBT_STATE = "state";
    public static final double RANGE = 16;

    public ItemEnderMagnet() {
        super();
        this.setNoRepair();
        this.setMaxStackSize(1);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.setCreativeTab(TEProxy.tabTETools);
        }
        // this.setFull3D();
        this.setFull3D();
        setUnlocalizedName("itemEnderMagnet");
        LanguageRegistry.instance().addStringLocalization(
                "item.itemEnderMagnet.name", "Ender-Infused Magnet");
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
                                      EntityPlayer par3EntityPlayer) {
        if (!par2World.isRemote) {
            // TODO: attract/disattract entities
            Entity e = EAUtil.getTarget(par2World, par3EntityPlayer, 32.0F);
            if (e != null
                    && getEnergyStored(par1ItemStack) >= RF_ENTITY_INTERACTON) {
                EAVector3 motionVec = EAVector3
                        .fromEntityCenter(par3EntityPlayer)
                        .subtract(EAVector3.fromEntityCenter(e)).normalize()
                        .multiply(4.0D).negate();
                if (EAKeyboard.isPressingCtrl(par3EntityPlayer))
                    motionVec.negate();
                e.motionX = motionVec.x;
                e.motionY = motionVec.y + (motionVec.mag() / 4);
                e.motionZ = motionVec.z;
                extractEnergy(par1ItemStack, RF_ENTITY_INTERACTON, false);
            }
            if (par3EntityPlayer.isSneaking()) {
                if (par1ItemStack.getTagCompound() == null)
                    par1ItemStack.setTagCompound(new NBTTagCompound());
                par1ItemStack.getTagCompound().setBoolean(NBT_STATE,
                        !par1ItemStack.getTagCompound().getBoolean(NBT_STATE));
                boolean newState = par1ItemStack.getTagCompound().getBoolean(
                        NBT_STATE);
                par3EntityPlayer.addChatMessage(new ChatComponentText(String.format(
                        "Magnet state: %s%s",
                        newState ? EnumChatFormatting.DARK_GREEN
                                : EnumChatFormatting.DARK_RED, newState ? "ON"
                        : "OFF")));
            }
        }
        par3EntityPlayer.swingItem();
        return par1ItemStack;
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

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon("enderamm:fluxMagnet");
    }

    @Override
    public int getMaxEnergyStored(ItemStack arg0) {
        return MAX_STORAGE;
    }

    public int receiveEnergy(ItemStack container, int maxReceive,
                             boolean simulate) {
        if (container.stackTagCompound == null) {
            TEProxy.setDefaultEnergyTag(container, 0);
        }
        int stored = container.stackTagCompound.getInteger("Energy");
        int receive = Math.min(maxReceive,
                Math.min(MAX_STORAGE - stored, TRANSFER_RATE));

        if (!simulate) {
            stored += receive;
            container.stackTagCompound.setInteger("Energy", stored);
        }
        return receive;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World,
                         Entity par3Entity, int par4, boolean par5) {
        if (!(par3Entity instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) par3Entity;
        if (par1ItemStack.stackTagCompound == null)
            par1ItemStack.stackTagCompound = new NBTTagCompound();
        if (par1ItemStack.getTagCompound().getBoolean(NBT_STATE)) {
            List<Entity> itemEnts = par2World.getEntitiesWithinAABB(
                    EntityItem.class, AxisAlignedBB.getBoundingBox(player.posX
                    - RANGE, player.posY - RANGE, player.posZ - RANGE,
                    player.posX + RANGE, player.posY + RANGE,
                    player.posZ + RANGE));
            if (itemEnts.size() > 0) {
                //System.out.println(itemEnts.size());
                if (getEnergyStored(par1ItemStack) >= RF_TICK_DRAW) {
                    if (!par2World.isRemote)
                        extractEnergy(par1ItemStack, RF_TICK_DRAW, false);
                    for (Entity e : itemEnts) {
                        EAVector3 motionVec = EAVector3
                                .fromEntityCenter(player)
                                .subtract(EAVector3.fromEntityCenter(e))
                                .normalize().multiply(1.0D);
                        //System.out.println(motionVec);
                        e.motionX = motionVec.x;
                        e.motionY = motionVec.y;
                        e.motionZ = motionVec.z;
                    }
                }
            }
        }
    }

    public int getDisplayDamage(ItemStack stack) {
        if (stack.stackTagCompound == null) {
            TEProxy.setDefaultEnergyTag(stack, 0);
        }
        return MAX_STORAGE + 1 - stack.stackTagCompound.getInteger("Energy");
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    public int getMaxDamage(ItemStack stack) {
        return MAX_STORAGE + 1;
    }

    public boolean isDamaged(ItemStack stack) {
        return stack.getItemDamage() != OreDictionary.WILDCARD_VALUE;
    }

    public static ItemStack getMagnet(int energy) {
        ItemStack result = new ItemStack(EACommonProxy.itemEnderMagnet,
                1, 0);
        result.stackTagCompound = new NBTTagCompound();
        result.stackTagCompound.setInteger("Energy", energy);
        return result;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs,
                            List par3List) {
        par3List.add(getMagnet(MAX_STORAGE));
        par3List.add(getMagnet(0));
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.epic;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack par1ItemStack,
                               EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(String.format("Charge: %d / %d RF",
                new Object[]{Integer.valueOf(getEnergyStored(par1ItemStack)),
                        Integer.valueOf(MAX_STORAGE)}));
        boolean state = par1ItemStack.getTagCompound().getBoolean(NBT_STATE);
        par3List.add(String.format("Magnet state: %s%s",
                state ? EnumChatFormatting.DARK_GREEN
                        : EnumChatFormatting.DARK_RED, state ? "ON" : "OFF"));
    }
}
