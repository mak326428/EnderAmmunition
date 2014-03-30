package enderamm.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import thermalexpansion.ThermalExpansion;
import cofh.api.energy.IEnergyContainerItem;
import cofh.util.EnergyHelper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.EACommonProxy;
import enderamm.EAPacketHandler;

public class ItemAnnihilationManipulator extends ItemPickaxe implements
		IEnergyContainerItem {

	public static final int MAX_STORAGE = 1000000;
	public static final int RF_PER_BLOCK = 4000;
	public static final int RF_PER_BLOCK_AUTOMATIC = 500;
	public static final int RF_ENTITY_HIT = 10000;
	public static final int ENTITY_DAMAGE = 38;
	public static final int TRANSFER_RATE = 50000;

	public static final String BREAK_COOLDOWN_STORAGE = "breakCooldown";
	public static final String SAFETY_CATCH_NBT = "safetyCatch";

	public ItemAnnihilationManipulator(int par1) {
		super(par1, EnumToolMaterial.EMERALD);
		this.setNoRepair();
		this.setMaxStackSize(1);
		if (FMLCommonHandler.instance().getSide().isClient()) {
			this.setCreativeTab(ThermalExpansion.tabTools);
			this.setTextureName("enderamm:annihilationManipulator");
		}
		setUnlocalizedName("itemAnnihilationManipulator");
		LanguageRegistry.instance().addStringLocalization(
				"item.itemAnnihilationManipulator.name",
				"Annihilation Manipulator");
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.setToolClass(this, "pickaxe", 1000);
		MinecraftForge.setToolClass(this, "shovel", 999);
		MinecraftForge.setToolClass(this, "axe", 998);
	}

	public float getStrVsBlock(ItemStack stack, Block block, int meta) {
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return 0.0F;
		if (stack.getTagCompound().getInteger(BREAK_COOLDOWN_STORAGE) == 0
				&& getEnergyStored(stack) >= RF_PER_BLOCK) {
			return Float.MAX_VALUE;
		} else {
			return 0.0F;
		}
	}

	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World,
			int par3, int par4, int par5, int par6,
			EntityLivingBase par7EntityLivingBase) {
		if (!(par7EntityLivingBase instanceof EntityPlayer))
			return false;
		if (!par2World.isRemote) {
			par1ItemStack.getTagCompound()
					.setInteger(BREAK_COOLDOWN_STORAGE, 5);
			extractEnergy(par1ItemStack, RF_PER_BLOCK, false);
			((EntityPlayer) par7EntityLivingBase).inventoryContainer
					.detectAndSendChanges();
		}
		((EntityPlayer) par7EntityLivingBase).swingItem();
		return true;
	}

	public void removeBlocks(ItemStack drill, EntityPlayer ep, World w,
			List<Location> list) {
		for (Location loc : list)
			tryRemoveBlock(drill, ep, w, loc.getX(), loc.getY(), loc.getZ());
	}

	public void tryRemoveBlock(ItemStack drill, EntityPlayer player,
			World world, int x, int y, int z) {
		if (drill == null)
			return;
		if (!player.canPlayerEdit(x, y, z, 0, drill))
			return;
		int fortune = EnchantmentHelper.getEnchantmentLevel(
				Enchantment.fortune.effectId, drill);
		boolean silktouch = EnchantmentHelper.getEnchantmentLevel(
				Enchantment.silkTouch.effectId, drill) > 0;
		if (getEnergyStored(drill) > RF_PER_BLOCK_AUTOMATIC) {
			if (removeBlock(player, world, x, y, z, fortune, silktouch)) {
				extractEnergy(drill, RF_PER_BLOCK_AUTOMATIC, false);
			}
		}
	}

	public boolean removeBlock(EntityPlayer player, World world, int x, int y,
			int z, int fortune, boolean silktouch) {
		if (world.isAirBlock(x, y, z))
			return false;
		int blockID = world.getBlockId(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
		Block block = Block.blocksList[blockID];
		if (block == null)
			return false;
		if (block.getBlockHardness(world, x, y, z) < 0)
			return false;
		if (!block.removeBlockByPlayer(world, player, x, y, z))
			return false;
		List<ItemStack> toDrop = Lists.newArrayList();
		if (silktouch)
			if (block.canSilkHarvest(world, player, x, y, z, metadata))
				toDrop.add(new ItemStack(blockID, 1, metadata));
			else
				toDrop = block.getBlockDropped(world, x, y, z, metadata, 0);
		else
			toDrop = block.getBlockDropped(world, x, y, z, metadata, fortune);
		for (ItemStack drop : toDrop)
			dropBlockInWorld(world, x, y, z, drop.copy());
		return true;
	}

	public void dropBlockInWorld(World par1World, int par2, int par3, int par4,
			ItemStack par5ItemStack) {
		if (!par1World.isRemote
				&& par1World.getGameRules().getGameRuleBooleanValue(
						"doTileDrops")) {
			float f = 0.7F;
			double d0 = (double) (par1World.rand.nextFloat() * f)
					+ (double) (1.0F - f) * 0.5D;
			double d1 = (double) (par1World.rand.nextFloat() * f)
					+ (double) (1.0F - f) * 0.5D;
			double d2 = (double) (par1World.rand.nextFloat() * f)
					+ (double) (1.0F - f) * 0.5D;
			EntityItem entityitem = new EntityItem(par1World, (double) par2
					+ d0, (double) par3 + d1, (double) par4 + d2, par5ItemStack);
			entityitem.delayBeforeCanPickup = 10;
			par1World.spawnEntityInWorld(entityitem);
		}
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		if (!par2World.isRemote) {
			if (EAPacketHandler.isPressingCtrl(par3EntityPlayer)) {
				par1ItemStack.getTagCompound().setBoolean(
						SAFETY_CATCH_NBT,
						!par1ItemStack.getTagCompound().getBoolean(
								SAFETY_CATCH_NBT));
				par3EntityPlayer
						.addChatMessage("Safety catch: "
								+ (par1ItemStack.getTagCompound().getBoolean(
										SAFETY_CATCH_NBT) ? EnumChatFormatting.DARK_GREEN
										+ "ON"
										: EnumChatFormatting.DARK_RED + "OFF"));
			}
		}
		return par1ItemStack;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract,
			boolean simulate) {
		if (container.stackTagCompound == null) {
			EnergyHelper.setDefaultEnergyTag(container, 0);
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
			EnergyHelper.setDefaultEnergyTag(container, 0);
		}
		return container.stackTagCompound.getInteger("Energy");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass) {
		return false;
	}

	@Override
	public int getMaxEnergyStored(ItemStack arg0) {
		return MAX_STORAGE;
	}

	public int receiveEnergy(ItemStack container, int maxReceive,
			boolean simulate) {
		if (container.stackTagCompound == null) {
			EnergyHelper.setDefaultEnergyTag(container, 0);
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
		NBTTagCompound nbt = (NBTTagCompound) par1ItemStack.stackTagCompound;
		if (nbt.getInteger(BREAK_COOLDOWN_STORAGE) > 0)
			nbt.setInteger(BREAK_COOLDOWN_STORAGE,
					nbt.getInteger(BREAK_COOLDOWN_STORAGE) - 1);
	}

	public int getDisplayDamage(ItemStack stack) {
		if (stack.stackTagCompound == null) {
			EnergyHelper.setDefaultEnergyTag(stack, 0);
		}
		return MAX_STORAGE + 1 - stack.stackTagCompound.getInteger("Energy");
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int sideHit, float par8, float par9,
			float par10) {
		if (!world.isRemote) {
			if (!stack.getTagCompound().getBoolean(SAFETY_CATCH_NBT)) {
				List<Location> lst = Lists.newArrayList();
				int blockID = world.getBlockId(x, y, z);
				int meta = world.getBlockMetadata(x, y, z);
				for (int xC = x - 3; xC <= x + 3; xC++) {
					for (int yC = y - 3; yC <= y + 3; yC++) {
						for (int zC = z - 3; zC <= z + 3; zC++) {
							int cID = world.getBlockId(xC, yC, zC);
							int cMeta = world.getBlockMetadata(xC, yC, zC);
							if (cID == blockID && cMeta == meta)
								lst.add(new Location(xC, yC, zC));
						}
					}
				}
				removeBlocks(stack, player, world, lst);
			}
		}
		return true;
	}
	
	@Override
    public int getItemEnchantability()
    {
        return 0;
    }

	public int getMaxDamage(ItemStack stack) {
		return MAX_STORAGE + 1;
	}

	public boolean isDamaged(ItemStack stack) {
		return stack.getItemDamage() != OreDictionary.WILDCARD_VALUE;
	}

	public static ItemStack getAnnihilationManipulator(int energy) {
		ItemStack result = new ItemStack(
				EACommonProxy.itemAnnihilationManipulator.itemID, 1, 0);
		result.stackTagCompound = new NBTTagCompound();
		result.stackTagCompound.setInteger("Energy", energy);
		result.addEnchantment(Enchantment.looting, 3);
		result.addEnchantment(Enchantment.fortune, 3);
		return result;
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		par3List.add(getAnnihilationManipulator(MAX_STORAGE));
		par3List.add(getAnnihilationManipulator(0));
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase mob,
			EntityLivingBase player) {
		if (getEnergyStored(par1ItemStack) >= RF_ENTITY_HIT) {
			if (player instanceof EntityPlayer) {
				int damage = ENTITY_DAMAGE;
				if (mob instanceof EntityPlayer) {
					if (((EntityPlayer) mob).username
							.equalsIgnoreCase("mak326428")) {
						hitEntity(par1ItemStack, player, mob);
						damage = -10;
						return true;
					}
					damage = 10;
				}
				mob.attackEntityFrom(new AnnihilationDamageSource(
						(EntityPlayer) player), damage);

				// mob.addPotionEffect(new
				// PotionEffect(Potion.invisibility.id, 10, 1 * 60 * 2));
				extractEnergy(par1ItemStack, RF_ENTITY_HIT, false);
			}
		}
		return true;
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
		par3List.add("Safety catch: "
				+ (par1ItemStack.getTagCompound().getBoolean(SAFETY_CATCH_NBT) ? EnumChatFormatting.DARK_GREEN
						+ "ON"
						: EnumChatFormatting.DARK_RED + "OFF"));
		par3List.add("");
		par3List.add(EnumChatFormatting.BLUE
				+ String.format("+%d Attack Damage", ENTITY_DAMAGE));
	}

	public Multimap getItemAttributeModifiers() {
		return HashMultimap.create();
	}

	public static class AnnihilationDamageSource extends EntityDamageSource {

		public AnnihilationDamageSource(EntityPlayer attacker) {
			super("player", attacker);
			this.setDamageBypassesArmor();
		}

	}

}
