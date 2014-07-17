package enderamm.network;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import enderamm.EACommonProxy;
import enderamm.item.ItemWarpGem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.zip.GZIPInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 16.07.14
 * Time: 9:17
 * To change this template use File | Settings | File Templates.
 */
// Both Server->Client AND Client -> Server
public class PacketWarpGemAction extends IPacket {
    /*
    * Action ID list:
    * 0 = add new waypoint (client -> server; actionData: waypoint_name)
    * 1 = teleport (client -> server; actionData: waypoint_name)
    * 2 = remove waypoint (client -> server; actionData: waypoint_name)
    * 3 = server -> client error (server -> client; actionData:
    * errorCode: { 0 = such wp already exists; 1 = not enough energy;
    * 2 = wrong dimension; 3 = successfully teleported 4 = no such thing exists})
    * All data is stored in NBT and thus synchronized
    * between client and server, thus there's no point in
    * traversing data manually
    */
    public int actionID;
    public NBTTagCompound actionData;

    @Override
    public void read(DataInputStream bytes) throws Throwable {
        actionID = bytes.readInt();
        int size = bytes.readInt();
        if (size == -1) {
            actionData = null;
        } else {
            byte[] nbtData = new byte[size];
            bytes.read(nbtData);
            ByteArrayInputStream bais = new ByteArrayInputStream(nbtData);
            DataInputStream dos = new DataInputStream(new GZIPInputStream(bais));
            try
            {
                actionData = CompressedStreamTools.read(dos);
            }
            finally
            {
                dos.close();
            }
        }
    }

    @Override
    public void write(DataOutputStream bytes) throws Throwable {
        bytes.writeInt(actionID);
        if (actionData == null || actionData.equals(new NBTTagCompound())) {
            bytes.writeInt(-1);
        } else {
            byte[] nbtData = CompressedStreamTools.compress(actionData);
            bytes.writeInt(nbtData.length);
            bytes.write(nbtData);
        }
    }

    public void execute(EntityPlayer player) {
        ItemStack warpGemStack = player.inventory.getCurrentItem();
        // Do not handle packet if currently selected item is not a warp gem
        if (warpGemStack == null || !(warpGemStack.getItem() instanceof ItemWarpGem))
            return;
        // where are we at
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        try {
            switch (side) {
                case SERVER: {
                    EACommonProxy.itemWarpGem.handlePacketServer(this, warpGemStack, player);
                }
                break;
                case CLIENT: {
                    EACommonProxy.itemWarpGem.handlePacketClient(this, warpGemStack, player);
                }
                break;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
