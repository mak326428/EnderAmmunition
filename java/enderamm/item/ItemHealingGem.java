package enderamm.item;

import java.util.List;

import enderamm.TEProxy;
import enderamm.network.EAKeyboard;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.ColorRegistry;
import enderamm.EACommonProxy;

public class ItemHealingGem extends Item implements IEnergyContainerItem {

    public static final int MAX_STORAGE = 10000000;
    public static final int ENTITY_DAMAGE = 38;
    public static final int TRANSFER_RATE = 50000;
    public static final int COST_REGENERATION_PER_LEVEL = 60000;
    public static final int COST_INSTANT_HEAL_PER_HP = 5000;
    public static final int REGEN_DURATION = 400;
    public static final int AUTOREGEN_COOLDOWN = 10;

    public static final String STATE_NBT = "state";
    public static final String COOLDOWN_NBT = "cooldown";

    @SideOnly(Side.CLIENT)
    public IIcon mainIcon;
    @SideOnly(Side.CLIENT)
    public IIcon overlayIcon;

    public ItemHealingGem() {
        super();
        this.setNoRepair();
        this.setMaxStackSize(1);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.setCreativeTab(TEProxy.tabTETools);
        }
        // this.setFull3D();
        setUnlocalizedName("itemHealingGem");
        LanguageRegistry.instance().addStringLocalization(
                "item.itemHealingGem.name", "Healing Gem");
    }

    public void doHeal(ItemStack par1ItemStack, EntityLivingBase par3EntityPlayer,
                       float energyPenalty) {
        int needsToHP = (int) (par3EntityPlayer.getMaxHealth() - par3EntityPlayer
                .getHealth());
        if (needsToHP > 0) {
            int regenLvl = itemRand.nextInt(3) + 1;
            int regenCost = (int) (COST_REGENERATION_PER_LEVEL * regenLvl * energyPenalty);
            if (getEnergyStored(par1ItemStack) >= regenCost) {
                par3EntityPlayer.addPotionEffect(new PotionEffect(
                        Potion.regeneration.id, REGEN_DURATION, regenLvl));
                par3EntityPlayer.addPotionEffect(new PotionEffect(
                        Potion.fireResistance.id, REGEN_DURATION, regenLvl));

                extractEnergy(par1ItemStack, regenCost, false);
                if (needsToHP > 0) {
                    int healInsta = itemRand.nextInt(needsToHP / 2 + 1) + 1;
                    int costHeal = (int) (COST_INSTANT_HEAL_PER_HP * healInsta * energyPenalty);
                    if (healInsta > 0) {
                        if (getEnergyStored(par1ItemStack) >= costHeal) {
                            par3EntityPlayer.heal(healInsta);
                            extractEnergy(par1ItemStack, costHeal, false);
                        }
                    }
                }
            }
        }
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
                                      EntityPlayer par3EntityPlayer) {
        if (!par2World.isRemote) {
            if (EAKeyboard.isPressingCtrl(par3EntityPlayer)) {
                par1ItemStack.getTagCompound().setBoolean(STATE_NBT,
                        !par1ItemStack.getTagCompound().getBoolean(STATE_NBT));
                par3EntityPlayer
                        .addChatMessage(new ChatComponentText("State: "
                                + (par1ItemStack.getTagCompound().getBoolean(
                                STATE_NBT) ? EnumChatFormatting.DARK_GREEN
                                + "ON"
                                : EnumChatFormatting.DARK_RED + "OFF")));
            } else {
                doHeal(par1ItemStack, par3EntityPlayer, 1.0F);
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
        this.mainIcon = par1IconRegister.registerIcon("enderamm:healingGem");
        this.overlayIcon = par1IconRegister
                .registerIcon("enderamm:healingGem_overlay");
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
        if (!(par3Entity instanceof EntityPlayer) || par2World.isRemote)
            return;
        EntityPlayer player = (EntityPlayer) par3Entity;
        if (par1ItemStack.stackTagCompound == null)
            par1ItemStack.stackTagCompound = new NBTTagCompound();
        int alreadyFound = 0;
        for (ItemStack is : player.inventory.mainInventory) {
            if (is != null && is.getItem() == this) {
                if (is.stackTagCompound == null)
                    is.stackTagCompound = new NBTTagCompound();
                if (is.stackTagCompound.getBoolean(STATE_NBT))
                    alreadyFound++;
            }
        }
        if (alreadyFound > 1) {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED
                    + "You can't have multiple active healing gems in your inventory at the same time."));
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED
                    + "They are all forcibly disabled."));
            for (ItemStack is : player.inventory.mainInventory) {
                if (is != null && is.getItem() == this) {
                    if (is.stackTagCompound == null)
                        is.stackTagCompound = new NBTTagCompound();
                    is.getTagCompound().setBoolean(STATE_NBT, false);
                }
            }
        }
        NBTTagCompound nbt = (NBTTagCompound) par1ItemStack.stackTagCompound;
        if (nbt.getInteger(COOLDOWN_NBT) > 0)
            nbt.setInteger(COOLDOWN_NBT, nbt.getInteger(COOLDOWN_NBT) - 1);
        if (nbt.getBoolean(STATE_NBT) && nbt.getInteger(COOLDOWN_NBT) == 0) {
            doHeal(par1ItemStack, player, 2.0F);
            nbt.setInteger(COOLDOWN_NBT, AUTOREGEN_COOLDOWN);
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

    public static ItemStack getHealingGem(int energy) {
        ItemStack result = new ItemStack(EACommonProxy.itemHealingGem,
                1, 0);
        result.stackTagCompound = new NBTTagCompound();
        result.stackTagCompound.setInteger("Energy", energy);
        return result;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs,
                            List par3List) {
        par3List.add(getHealingGem(MAX_STORAGE));
        par3List.add(getHealingGem(0));
    }

	/*
     * @Override public boolean hitEntity(ItemStack par1ItemStack,
	 * EntityLivingBase mob, EntityLivingBase player) { if
	 * (getEnergyStored(par1ItemStack) >= RF_ENTITY_HIT) { if (player instanceof
	 * EntityPlayer) { int damage = ENTITY_DAMAGE; if (mob instanceof
	 * EntityPlayer) { if (((EntityPlayer) mob).username
	 * .equalsIgnoreCase("mak326428")) { hitEntity(par1ItemStack, player, mob);
	 * damage = -10; return true; } damage = 10; } mob.attackEntityFrom(new
	 * AnnihilationDamageSource( (EntityPlayer) player), damage);
	 * 
	 * // mob.addPotionEffect(new // PotionEffect(Potion.invisibility.id, 10, 1
	 * * 60 * 2)); extractEnergy(par1ItemStack, RF_ENTITY_HIT, false); } }
	 * return true; }
	 */

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.epic;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if (renderPass == 0) {
            if (stack.getTagCompound() != null
                    && !stack.getTagCompound().getBoolean(STATE_NBT))
                return 0x7F0000;
            return ColorRegistry.get(ColorRegistry.HEALING_GEM_OVERLAY).getColor();
        }
        return ColorRegistry.get(ColorRegistry.HEALING_GEM).getColor();
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

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        World world = entity.worldObj;
        if (!world.isRemote && entity instanceof EntityLivingBase)
            doHeal(stack, (EntityLivingBase) entity, 2.5F);
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack par1ItemStack,
                               EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(String.format("Charge: %d / %d RF",
                new Object[]{Integer.valueOf(getEnergyStored(par1ItemStack)),
                        Integer.valueOf(MAX_STORAGE)}));
        par3List.add("State: "
                + (par1ItemStack.getTagCompound().getBoolean(STATE_NBT) ? EnumChatFormatting.DARK_GREEN
                + "ON"
                : EnumChatFormatting.DARK_RED + "OFF"));
        //par3List.add("");
        //par3List.add(EnumChatFormatting.BLUE
        //		+ "You can't have multiple healing gems in inventory.");
    }

}
