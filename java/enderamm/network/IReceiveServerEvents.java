package enderamm.network;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 24.02.14
 * Time: 13:18
 * To change this template use File | Settings | File Templates.
 */
public interface IReceiveServerEvents {
    public void onServerEvent(int event, NBTTagCompound nbtData);
}
