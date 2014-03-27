package enderamm.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.oredict.OreDictionary;
import thermalexpansion.ThermalExpansion;
import cofh.api.energy.IEnergyContainerItem;
import cofh.util.EnergyHelper;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.EAPacketHandler;
import enderamm.EAUtil;

public class ItemArmorEnderBase extends ItemArmor implements ISpecialArmor,
		IEnergyContainerItem {
	public static final String ENERGY_NBT = "Energy";
	public static int RENDER_INDEX;
	@SideOnly(Side.CLIENT)
	public Icon[] icons;

	public ItemArmorEnderBase(int id, int armorType) {
		super(id, EnumArmorMaterial.DIAMOND, RENDER_INDEX, armorType);
		this.setNoRepair();
		this.setMaxStackSize(1);
		if (FMLCommonHandler.instance().getSide().isClient())
			this.setCreativeTab(ThermalExpansion.tabTools);
	}

	public static Map<EntityPlayer, Integer> fireMap = Maps.newHashMap();

	public static final int ENERGY_STORAGE = 10000000;
	public static final int TRANSFER_RATE = 100000;
	public static final int ENERGY_PER_TICK_FLYING = 278;
	public static final int ENERGY_HYPERJUMP = 4000;
	public static final int ENERGY_SPEEDUP_TICKING = 6000;
	public static final int ENERGY_FIRE_RAY = 30000;
	public static final int ENERGY_PER_HUNGER_POINT = 10000;

	@Override
	public int extractEnergy(ItemStack container, int maxExtract,
			boolean simulate) {
		if (container.stackTagCompound == null) {
			EnergyHelper.setDefaultEnergyTag(container, 0);
		}
		int stored = container.stackTagCompound.getInteger(ENERGY_NBT);
		int extract = Math.min(maxExtract, stored);

		if (!simulate) {
			stored -= extract;
			container.stackTagCompound.setInteger(ENERGY_NBT, stored);
		}
		return extract;
	}

	@Override
	public int getEnergyStored(ItemStack container) {
		if (container.stackTagCompound == null) {
			EnergyHelper.setDefaultEnergyTag(container, 0);
		}
		return container.stackTagCompound.getInteger(ENERGY_NBT);
	}

	@Override
	public int getMaxEnergyStored(ItemStack arg0) {
		return ENERGY_STORAGE;
	}

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive,
			boolean simulate) {
		if (container.stackTagCompound == null) {
			EnergyHelper.setDefaultEnergyTag(container, 0);
		}
		int stored = container.stackTagCompound.getInteger(ENERGY_NBT);
		int receive = Math.min(maxReceive,
				Math.min(ENERGY_STORAGE - stored, TRANSFER_RATE));

		if (!simulate) {
			stored += receive;
			container.stackTagCompound.setInteger(ENERGY_NBT, stored);
		}
		return receive;
	}

	@Override
	public int getDisplayDamage(ItemStack stack) {
		if (stack.stackTagCompound == null) {
			EnergyHelper.setDefaultEnergyTag(stack, 0);
		}
		return 1 + ENERGY_STORAGE
				- stack.stackTagCompound.getInteger(ENERGY_NBT);
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return 1 + ENERGY_STORAGE;
	}

	@Override
	public boolean isDamaged(ItemStack stack) {
		return stack.getItemDamage() != OreDictionary.WILDCARD_VALUE;
	}

	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		if (getEnergyStored(armor) >= getEnergyPerDamage()) {
			return (int) Math.round(20.0D * getBaseAbsorptionRatio()
					* getDamageAbsorptionRatio());
		}
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack,
			DamageSource source, int damage, int slot) {
		extractEnergy(stack, damage * getEnergyPerDamage(), false);
	}

	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add(String.format("" + "Charge: %d / %d RF",
				getEnergyStored(par1ItemStack), ENERGY_STORAGE));
		boolean chestplate = armorType == 1;
		boolean helmet = armorType == 0;
		boolean leggings = armorType == 2;
		boolean boots = armorType == 3;
		boolean shiftDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
				|| Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		if (!shiftDown) {
			par3List.add("Hold " + EnumChatFormatting.ITALIC + EnumChatFormatting.YELLOW + "Shift" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + " for Details");
			 
		} else {
			par3List.add("");
			par3List.add("Abilities: ");
			if (chestplate) {
				par3List.add("Extinguisher");
				par3List.add("Creative-Mode Flight");
				par3List.add("Active Item Charger");
			} else if (helmet) {
				par3List.add("Fire Ray Shooter");
				par3List.add("Food Replenisher");
			} else if (leggings) {
				par3List.add("Underwater Motion Accelerator");
				par3List.add("Sprint Booster");
			} else if (boots) {
				par3List.add("Jump Booster");
			}
		}
	}

	private double getBaseAbsorptionRatio() {
		switch (this.armorType) {
		case 0:
			return 0.15D;
		case 1:
			return 0.4D;
		case 2:
			return 0.3D;
		case 3:
			return 0.15D;
		}
		return 0.0D;
		// TODO: check the recipes!!! do not forget, it would be awful!!!
	}

	public double getDamageAbsorptionRatio() {
		return 1.1D;
	}

	public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase entity,
			ItemStack armor, DamageSource source, double damage, int slot) {
		if ((source == DamageSource.fall) && (this.armorType == 3)) {
			int energyPerDamage = getEnergyPerDamage();
			int damageLimit = energyPerDamage > 0 ? 25 * getEnergyStored(armor)
					/ energyPerDamage : 0;

			return new ISpecialArmor.ArmorProperties(10, 1.0D, damageLimit);
		} else {
			if (source.isUnblockable())
				return new ISpecialArmor.ArmorProperties(0, 0.0D, 0);

			double absorptionRatio = getBaseAbsorptionRatio()
					* getDamageAbsorptionRatio();
			int energyPerDamage = getEnergyPerDamage();

			int damageLimit = energyPerDamage > 0 ? 25 * getEnergyStored(armor)
					/ energyPerDamage : 0;

			return new ISpecialArmor.ArmorProperties(0, absorptionRatio,
					damageLimit);
		}
	}

	public static HashMap<EntityPlayer, Boolean> onGroundMap = new HashMap<EntityPlayer, Boolean>();
	private static float jumpCharge;
	public static final float MAX_JUMP_CHARGE = 1.3F;
	public static HashMap<EntityPlayer, Integer> speedTickerMap = new HashMap<EntityPlayer, Integer>();

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player,
			ItemStack itemStack) {
		boolean chestplate = armorType == 1;
		boolean helmet = armorType == 0;
		boolean leggings = armorType == 2;
		boolean boots = armorType == 3;
		boolean ctrlDown = EAPacketHandler.isPressingCtrl(player);
		// System.out.println(ctrlDown);
		boolean jumpDown = EAPacketHandler.isPressingSpace(player);
		boolean fireDown = EAPacketHandler.isPressingFire(player);
		if (chestplate) {
			if (getEnergyStored(itemStack) >= ENERGY_PER_TICK_FLYING) {
				EAFlightHandler.allowFlight(player);
				player.capabilities.allowFlying = true;
				if (player.capabilities.isFlying) {
					if (ctrlDown) {
						float boost = 0.23f;
						boolean doBoost = true;
						if (jumpDown) {
							player.motionY += boost + 0.03D;
							doBoost = false;
						}
						if (player.isSneaking()) {
							player.motionX -= boost + 0.03D;
							doBoost = false;
						}
						if (doBoost) {
							player.moveFlying(0.0F, 1.0F, boost);
						}
						// System.out.println("Boosting");
						// System.out.println("world.isRemote: " +
						// world.isRemote);
						if (!world.isRemote)
							if (!player.capabilities.isCreativeMode) {
								extractEnergy(itemStack,
										(int) (ENERGY_PER_TICK_FLYING * 3F),
										false);
							}
					}
					if (!world.isRemote) {
						if (!player.capabilities.isCreativeMode) {
							extractEnergy(itemStack, ENERGY_PER_TICK_FLYING,
									false);
						}
					}
				}
			} else {
				EAFlightHandler.removePlayerFlight(player);
			}
			if (!world.isRemote) {
				player.extinguish();
				ItemStack currentlyHolding = player.inventory.getCurrentItem();
				if (currentlyHolding != null
						&& EnergyHelper.isEnergyContainerItem(currentlyHolding)) {
					IEnergyContainerItem ic = (IEnergyContainerItem) currentlyHolding
							.getItem();
					extractEnergy(itemStack, ic.receiveEnergy(currentlyHolding,
							getEnergyStored(itemStack), false), false);
				}
			}
		} else if (helmet) {
			// InventoryPlayer inventory = player.inventory;
			if (player.getFoodStats().getFoodLevel() < 18
					&& !world.isRemote
					&& getEnergyStored(itemStack) >= ENERGY_PER_HUNGER_POINT * 2) {
				player.getFoodStats().addStats(2, 8);
				extractEnergy(itemStack, ENERGY_PER_HUNGER_POINT * 2, false);
			}
			Entity e = EAUtil.getTarget(world, player, 128.0D);
			if (e != null && getEnergyStored(itemStack) >= ENERGY_FIRE_RAY
					&& !world.isRemote && fireDown) {
				if (!fireMap.containsKey(player))
					fireMap.put(player, 0);
				int cooldown = fireMap.get(player);
				boolean shouldFire = cooldown == 0;
				if (shouldFire) {
					fireMap.remove(player);
					fireMap.put(player, 10);
					extractEnergy(itemStack, ENERGY_FIRE_RAY, false);
					e.setFire(10);
					e.attackEntityFrom(DamageSource.inFire, 15);
					for (int i = 0; i < 10; i++)
						EAPacketHandler.sendFireRayPacket((float) player.posX,
								(float) (player.posY + 1.6F),
								(float) player.posZ, (float) e.posX,
								(float) (e.posY + (e.height / 2)),
								(float) e.posZ, world);
				} else {
					cooldown--;
					fireMap.remove(player);
					fireMap.put(player, cooldown);
				}
			}
		} else if (leggings) {
			if (!speedTickerMap.containsKey(player))
				speedTickerMap.put(player, 0);
			float speed = 0.3F;
			if (getEnergyStored(itemStack) > ENERGY_SPEEDUP_TICKING
					&& ((player.onGround) || (player.isInWater()))
					&& (player.isSprinting())) {
				int speedTicker = speedTickerMap.containsKey(player) ? ((Integer) speedTickerMap
						.get(player)).intValue() : 0;
				speedTicker++;

				if (speedTicker >= 10) {
					speedTicker = 0;
					extractEnergy(itemStack, ENERGY_SPEEDUP_TICKING, false);
				}
				speedTickerMap.remove(player);
				speedTickerMap.put(player, Integer.valueOf(speedTicker));

				if (player.isInWater()) {
					speed = 0.1F;
					if (jumpDown)
						player.motionY += 0.1000000014901161D;
				}

				if (speed > 0.0F)
					player.moveFlying(0.0F, 1.0F, speed);
			}
		} else if (boots) {
			if (!onGroundMap.containsKey(player))
				onGroundMap.put(player, true);
			if (!world.isRemote) {
				boolean wasOnGround = onGroundMap.containsKey(player) ? ((Boolean) onGroundMap
						.get(player)).booleanValue() : true;

				if ((wasOnGround) && (!player.onGround) && (jumpDown)
						&& (ctrlDown)) {
					extractEnergy(itemStack, ENERGY_HYPERJUMP, false);
				}
				onGroundMap.remove(player);
				onGroundMap.put(player, Boolean.valueOf(player.onGround));
			} else {
				if (getEnergyStored(itemStack) > ENERGY_HYPERJUMP
						&& (player.onGround))
					jumpCharge = MAX_JUMP_CHARGE;

				if ((player.motionY >= 0.0D) && (jumpCharge > 0.0F)
						&& (!player.isInWater())) {
					if ((jumpDown && ctrlDown)) {
						if (jumpCharge == MAX_JUMP_CHARGE) {
							player.motionX *= 2.5D;
							player.motionZ *= 2.5D;
						}

						player.motionY += jumpCharge * 0.3F;
						jumpCharge = ((float) (jumpCharge * 0.75D));
					} else if (jumpCharge < 1.0F) {
						jumpCharge = 0.0F;
					}
				}
			}
		}
	}

	public int getEnergyPerDamage() {
		return 10000;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		icons = new Icon[4];
		String[] types = new String[] { "Helmet", "Chestplate", "Legs", "Boots" };
		for (int i = 0; i < types.length; i++)
			icons[i] = par1IconRegister.registerIcon("enderamm:itemArmorEnder"
					+ types[i]);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIconFromDamage(int par1) {
		return icons[armorType];
	}

	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return "item.armorEnder" + armorType;
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		ItemStack charged = new ItemStack(par1, 1, 0);
		ItemStack discharged = new ItemStack(par1, 1, 0);
		discharged.stackTagCompound = new NBTTagCompound();
		charged.stackTagCompound = new NBTTagCompound();
		discharged.stackTagCompound.setInteger(ENERGY_NBT, 0);
		charged.stackTagCompound.setInteger(ENERGY_NBT, ENERGY_STORAGE);
		par3List.add(charged);
		par3List.add(discharged);
	}

	public boolean allowsFlight(ItemStack is) {
		return armorType == 1;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.epic;
	}

}
