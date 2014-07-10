package enderamm.block;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import enderamm.LogicSlot;
import enderamm.network.PacketFireRay;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

public class TileEntityRockExterminator extends TileEntity implements IEnergyHandler {

    public EnergyStorage storage = new EnergyStorage(1 * 1000 * 1000);
    public int miningAtX = Integer.MIN_VALUE;
    public int miningAtY = Integer.MIN_VALUE;
    public int miningAtZ = Integer.MIN_VALUE;
    public static final int RF_PER_BLOCK = 600;
    public static FakePlayer fp;

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

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return from != ForgeDirection.DOWN;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        miningAtX = nbt.getInteger("miningAtX");
        miningAtY = nbt.getInteger("miningAtY");
        miningAtZ = nbt.getInteger("miningAtZ");

    }

    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("miningAtX", miningAtX);
        nbt.setInteger("miningAtY", miningAtY);
        nbt.setInteger("miningAtZ", miningAtZ);
    }

    public TileEntity getInventoryAround() {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            int xN = xCoord + dir.offsetX;
            int yN = yCoord + dir.offsetY;
            int zN = zCoord + dir.offsetZ;
            TileEntity te = worldObj.getTileEntity(xN, yN, zN);
            if (te instanceof IInventory) return te;
        }
        return null;
    }

    public void dropStackInWorld(World par1World, float par2, float par3, float par4,
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

    public void dropAway(List<ItemStack> stacks) {
        for (ItemStack stack : stacks)
            dropStackInWorld(worldObj, xCoord + 0.5F, yCoord + 1.5F, zCoord + 0.5F, stack);

    }

    public List<ItemStack> tryToInsertToInventory(List<ItemStack> itemStackList, TileEntity te) {
        if (!(te instanceof IInventory)) return itemStackList;
        IInventory inv = (IInventory) te;
        Queue<ItemStack> stackQueue = new ArrayDeque<ItemStack>(itemStackList);
        LogicSlot[] slots = new LogicSlot[inv.getSizeInventory()];
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            slots[i] = new LogicSlot(inv, i);
        }
        // TODO: check this, might be a bit of an overkill (performance-wise)
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            for (int j = 0; j < slots.length; j++) {
                if (stackQueue.size() == 0) break;
                ItemStack peek = stackQueue.poll();
                ItemStack leftover = slots[j].add(peek, false);
                if (leftover != null) stackQueue.add(leftover);
            }
        }
        return new ArrayList<ItemStack>(stackQueue);
    }

    public static Set<ItemStack> bulkItems = Sets.newHashSet();

    static {
        bulkItems.add(new ItemStack(Blocks.cobblestone));
        bulkItems.add(new ItemStack(Blocks.grass));
        bulkItems.add(new ItemStack(Blocks.dirt));
        bulkItems.add(new ItemStack(Blocks.gravel));
        bulkItems.add(new ItemStack(Blocks.sand));
        bulkItems.add(new ItemStack(Blocks.stone));

    }

    public void updateEntity() {
        if (worldObj.isRemote) return;
        int dim = 3 + getDimensionUpgrades() * 2; // tier 2 is 3 + 2*4 = 11 (11x11xtoBedrock)
        int blocksPerTick = getSpeedUpgrades();

        if (miningAtX == Integer.MIN_VALUE || miningAtY == Integer.MIN_VALUE || miningAtZ == Integer.MIN_VALUE) {
            miningAtX = xCoord - dim / 2;
            miningAtY = yCoord - 1;
            miningAtZ = zCoord - dim / 2;
        }
        if (fp == null && worldObj instanceof WorldServer) {
            fp = FakePlayerFactory.get((WorldServer) worldObj, new GameProfile(UUID.randomUUID().toString(), "[EnderAmmunition]"));
        }
        if (miningAtY <= 1) return;
        // TODO: energy consumption (taking eff and unbrk in account)
        Map<Enchantment, Integer> enchantmentMap = getEnchantmentMap();
        boolean silk = enchantmentMap.containsKey(Enchantment.silkTouch);
        int fortune = enchantmentMap.containsKey(Enchantment.fortune) ? enchantmentMap.get(Enchantment.fortune) : 0;
        int efficiencyDivider = enchantmentMap.containsKey(Enchantment.efficiency) ? enchantmentMap.get(Enchantment.efficiency) + 1 : 1;
        int energyConsumption = RF_PER_BLOCK / efficiencyDivider;
        if (storage.getEnergyStored() < energyConsumption * blocksPerTick) return;
        TileEntity inventory = getInventoryAround();
        boolean removeBulk = hasRemoveBulkUpgrade();
        while (blocksPerTick >= 0) {
            miningAtX++;
            //miningAtZ++;
            if (miningAtX >= xCoord + dim / 2) {
                miningAtX = xCoord - dim / 2;
                miningAtZ++;
            }
            if (miningAtZ >= zCoord + dim / 2) {
                miningAtZ = zCoord - dim / 2;
                miningAtY--;
            }
            Block blockToMine = worldObj.getBlock(miningAtX, miningAtY, miningAtZ);
            // skip air (like caves) + skip unminable blocks
            if (blockToMine == Blocks.air || blockToMine.getBlockHardness(worldObj, miningAtX, miningAtY, miningAtZ) < 0) {
                blocksPerTick++;
                continue;
            }
            List<ItemStack> drops = removeBlock(fp, worldObj, miningAtX, miningAtY, miningAtZ, fortune, silk);
            if (drops == null) {
                blocksPerTick++;
                continue;
            } else {
                storage.extractEnergy(energyConsumption, false);
            }
            if (removeBulk) {
                List<ItemStack> clearedDrops = Lists.newArrayList();
                for (ItemStack d : drops) {
                    boolean remove = false;
                    for (ItemStack b : bulkItems) {
                        if (b.getItem() == d.getItem() && b.getItemDamage() == d.getItemDamage()) {
                            remove = true;
                            break;
                        }
                    }
                    if (!remove)
                        clearedDrops.add(d);
                }
                drops = clearedDrops;
            }
            if (inventory == null) {
                dropAway(drops);
            } else {
                List<ItemStack> leftover = tryToInsertToInventory(drops, inventory);
                if (leftover != null) dropAway(leftover);
            }
            sendParticlePacket();
            blocksPerTick--;

        }
    }

    public int getDimensionUpgrades() {
        // TODO: read from upgrade slot
        return 6;
    }

    public int getSpeedUpgrades() {
        // TODO: read from upgrade slot
        return 1;
    }

    public boolean hasRemoveBulkUpgrade() {
        return true;
    }

    public Map<Enchantment, Integer> getEnchantmentMap() {
        // TODO: read from upgrade slot
        Map<Enchantment, Integer> map = Maps.newHashMap();
        map.put(Enchantment.silkTouch, 1);
        map.put(Enchantment.efficiency, 5);
        return map;
    }

    public void sendParticlePacket() {
        /*double xThis = this.xCoord < 0 ? this.xCoord - 0.5D : this.xCoord + 0.5D;
        double yThis = this.yCoord < 0 ? this.yCoord - 0.5D : this.yCoord + 0.5D;
        double zThis = this.zCoord < 0 ? this.zCoord - 0.5D : this.zCoord + 0.5D;

        double xTarget = this.miningAtX < 0 ? this.miningAtX - 0.5D : this.miningAtX + 0.5D;
        double yTarget = this.miningAtY < 0 ? this.miningAtY - 0.5D : this.miningAtY + 0.5D;
        double zTarget = this.miningAtZ < 0 ? this.miningAtZ - 0.5D : this.miningAtZ + 0.5D;*/
        double xThis = xCoord + 0.5D;
        double yThis = yCoord + 0.5D;
        double zThis = zCoord + 0.5D;
        double xTarget = this.miningAtX + 0.5D;
        double yTarget = this.miningAtY + 0.5D;
        double zTarget = this.miningAtZ + 0.5D;

        PacketFireRay.issue((float) xThis, (float) yThis, (float) zThis, (float) xTarget, (float) yTarget - 1, (float) zTarget, worldObj, 12);

    }

    public List<ItemStack> removeBlock(EntityPlayer player, World world, int x, int y,
                                       int z, int fortune, boolean silktouch) {
        if (world.isAirBlock(x, y, z))
            return null;
        Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        if (block == null)
            return null;
        if (block.getBlockHardness(world, x, y, z) < 0)
            return null;
        if (!block.removedByPlayer(world, player, x, y, z))
            return null;
        List<ItemStack> toDrop = Lists.newArrayList();
        if (silktouch)
            if (block.canSilkHarvest(world, player, x, y, z, metadata))
                toDrop.add(new ItemStack(block, 1, metadata));
            else
                toDrop = block.getDrops(world, x, y, z, metadata, 0);
        else
            toDrop = block.getDrops(world, x, y, z, metadata, fortune);
        world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 6.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
        world.playSoundEffect(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 6.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
        return toDrop;
    }
}
