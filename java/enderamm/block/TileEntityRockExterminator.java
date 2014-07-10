package enderamm.block;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import enderamm.network.PacketFireRay;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityRockExterminator extends TileEntity implements IEnergyHandler {

    public EnergyStorage storage = new EnergyStorage(1 * 1000 * 1000);
    public int tier = 5;
    public int miningAtX = Integer.MIN_VALUE;
    public int miningAtY = Integer.MIN_VALUE;
    public int miningAtZ = Integer.MIN_VALUE;
    public static final int RF_PER_BLOCK = 600;

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


    public void updateEntity() {
        if (worldObj.isRemote) return;
        int dim = 3 + tier * 4; // tier 2 is 3 + 2*4 = 11 (11x11xtoBedrock)
        int blocksPerTick = 2;
        if (miningAtX == Integer.MIN_VALUE || miningAtY == Integer.MIN_VALUE || miningAtZ == Integer.MIN_VALUE) {
            miningAtX = xCoord - dim / 2;
            miningAtY = yCoord - 1;
            miningAtZ = zCoord - dim / 2;
        }
        if (miningAtY <= 1) return;
        // TODO; checks for energy / can mine that block here / particles etc.
        for (int i = 0; i < blocksPerTick; i++) {
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

            sendParticlePacket();
            worldObj.setBlockToAir(miningAtX, miningAtY, miningAtZ);
        }
    }

    public void sendParticlePacket() {
        double xThis = this.xCoord < 0 ? this.xCoord - 0.5D : this.xCoord + 0.5D;
        double yThis = this.yCoord < 0 ? this.yCoord - 0.5D : this.yCoord + 0.5D;
        double zThis = this.zCoord < 0 ? this.zCoord - 0.5D : this.zCoord + 0.5D;

        double xTarget = this.miningAtX < 0 ? this.miningAtX - 0.5D : this.miningAtX + 0.5D;
        double yTarget = this.miningAtY < 0 ? this.miningAtY - 0.5D : this.miningAtY + 0.5D;
        double zTarget = this.miningAtZ < 0 ? this.miningAtZ - 0.5D : this.miningAtZ + 0.5D;

        PacketFireRay.issue((float)xThis, (float)yThis, (float)zThis, (float)xTarget, (float)yTarget - 1, (float)zTarget, worldObj, 12);
    }
}
