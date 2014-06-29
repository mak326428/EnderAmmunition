package enderamm.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;

// Server -> Client
public class PacketChangeState extends IPacket {

    private int x, y, z, eventID;
    private NBTTagCompound nbtData;

    @Override
    public void read(DataInputStream bytes) throws Throwable {
        x = bytes.readInt();
        y = bytes.readInt();
        z = bytes.readInt();
        eventID = bytes.readInt();

        int length = bytes.readInt();
        if (length != -1) {
            byte[] data = new byte[length];
            bytes.read(data, 0, length);
            nbtData = CompressedStreamTools.decompress(data);
        } else {
            nbtData = null;
        }
    }

    @Override
    public void write(DataOutputStream bytes) throws Throwable {
        bytes.writeInt(x);
        bytes.writeInt(y);
        bytes.writeInt(z);
        bytes.writeInt(eventID);

        if (nbtData == null) {
            bytes.writeInt(-1);
        } else {
            byte[] nbtBytes = CompressedStreamTools.compress(nbtData);
            bytes.writeInt(nbtBytes.length);
            bytes.write(nbtBytes);
        }
    }

    public static void issue(TileEntity te, int eventID, NBTTagCompound nbtData) {
        PacketChangeState changeState = new PacketChangeState();
        changeState.x = te.xCoord;
        changeState.y = te.yCoord;
        changeState.z = te.zCoord;
        changeState.eventID = eventID;
        changeState.nbtData = nbtData;
        EAPacketHandler.sendToAllPlayers(changeState);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void execute(EntityPlayerMP player) {
        World w = Minecraft.getMinecraft().theWorld;
        TileEntity te = w.getTileEntity(x, y, z);
        if (te != null) {
            if (te instanceof IReceiveServerEvents) {
                ((IReceiveServerEvents) te).onServerEvent(eventID, nbtData);
            }
        }
    }
}
