package enderamm.item;

import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.EACommonProxy;
import enderamm.EAUtil;
import enderamm.TEProxy;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.oredict.OreDictionary;

public class ItemEnderBow extends Item implements IEnergyContainerItem {

	public static final String[] bowPullIconNameArray = new String[] {
			"pull_0", "pull_1", "pull_2" };
	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;

	public static final int MAX_STORAGE = 1000000;
	public static final int TRANSFER_RATE = 50000;
	public static final int RF_SHOT_BASIC = 20000;

	public ItemEnderBow() {
		super();
		this.setNoRepair();
		this.setMaxStackSize(1);
		if (FMLCommonHandler.instance().getSide().isClient()) {
			this.setCreativeTab(TEProxy.tabTETools);
		}
		// this.setFull3D();
		this.setFull3D();
		setUnlocalizedName("itemEnderBow");
		LanguageRegistry.instance().addStringLocalization(
				"item.itemEnderBow.name", "Ender-Infused Bow");
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
		this.itemIcon = par1IconRegister.registerIcon("enderamm:enderBow");
		this.iconArray = new IIcon[bowPullIconNameArray.length];

		for (int i = 0; i < this.iconArray.length; ++i) {
			this.iconArray[i] = par1IconRegister
					.registerIcon("enderamm:enderBow" + "_"
							+ bowPullIconNameArray[i]);
		}
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

	public static ItemStack getBow(int energy) {
		ItemStack result = new ItemStack(EACommonProxy.itemEnderBow, 1,
				0);
		result.stackTagCompound = new NBTTagCompound();
		result.stackTagCompound.setInteger("Energy", energy);
		return result;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player,
			ItemStack usingItem, int useRemaining) {
		ItemStack itemInUse = player.inventory.getCurrentItem();
		if (itemInUse != null && itemInUse.getItem() instanceof ItemEnderBow
				&& player.isUsingItem()) {
			int j = usingItem.getMaxItemUseDuration()
					- player.getItemInUseCount();
			if (j < 5)
				return iconArray[0];
			if (j < 7)
				return iconArray[1];
			return iconArray[2];
		}
		return itemIcon;
		// return getIcon(stack, renderPass);
	}

	@SideOnly(Side.CLIENT)
    @Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		par3List.add(getBow(MAX_STORAGE));
		par3List.add(getBow(0));
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
				new Object[] { Integer.valueOf(getEnergyStored(par1ItemStack)),
						Integer.valueOf(MAX_STORAGE) }));
	}

	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer, int par4) {
		Entity e = EAUtil.getTarget(par2World, par3EntityPlayer, 128.0D);
		if (e != null) {
			int index = -1;
			for (int i = 0; i < par3EntityPlayer.inventory.mainInventory.length; i++) {
				ItemStack is = par3EntityPlayer.inventory.mainInventory[i];
				if (is != null && is.getItem() instanceof ItemEnderArrow)
					index = i;
			}
			if (index != -1 && getEnergyStored(par1ItemStack) > RF_SHOT_BASIC) {
				par2World.playSoundAtEntity(par3EntityPlayer, "random.bow",
						1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1
								* 0.5F);
				if (!par2World.isRemote) {
					ItemStack arrowStack = par3EntityPlayer.inventory.mainInventory[index];
					extractEnergy(par1ItemStack, RF_SHOT_BASIC, false);
					((ItemEnderArrow) arrowStack.getItem()).apply(e,
							par3EntityPlayer, arrowStack,
							par3EntityPlayer.worldObj);
					par3EntityPlayer.inventory.mainInventory[index].stackSize--;
					if (par3EntityPlayer.inventory.mainInventory[index].stackSize <= 0)
						par3EntityPlayer.inventory.mainInventory[index] = null;
					par3EntityPlayer.inventoryContainer.detectAndSendChanges();
				}
			}
		}
	}

	public ItemStack onEaten(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	/**
	 * returns the action that specifies what animation to play when the items
	 * is being used
	 */
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		ArrowNockEvent event = new ArrowNockEvent(par3EntityPlayer,
				par1ItemStack);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return event.result;
		}

		if (par3EntityPlayer.capabilities.isCreativeMode
				|| par3EntityPlayer.inventory
						.hasItem(EACommonProxy.itemEnderArrow)) {
			par3EntityPlayer.setItemInUse(par1ItemStack,
					this.getMaxItemUseDuration(par1ItemStack));
		}

		return par1ItemStack;
	}

}
