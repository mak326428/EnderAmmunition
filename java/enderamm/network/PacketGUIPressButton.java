package enderamm.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 23.02.14
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class PacketGUIPressButton extends IPacket {


    public int dimID;
    public int x;
    public int y;
    public int z;
    public int buttonID;

    @Override
    public void read(DataInputStream bytes) throws Throwable {
        dimID = bytes.readInt();
        x = bytes.readInt();
        y = bytes.readInt();
        z = bytes.readInt();
        buttonID = bytes.readInt();
    }

    @Override
    public void write(DataOutputStream bytes) throws Throwable {
        bytes.writeInt(dimID);
        bytes.writeInt(x);
        bytes.writeInt(y);
        bytes.writeInt(z);
        bytes.writeInt(buttonID);
    }

    /**
     * Creates new packet, fills it with TE's coordinates and sets buttonID to given, then sends to server.
     *
     * @param te       TileEntity to get coordinates from
     * @param buttonID ID of a button
     */
    public static void issue(TileEntity te, int buttonID) {
        PacketGUIPressButton pgpb = new PacketGUIPressButton();
        pgpb.x = te.xCoord;
        pgpb.y = te.yCoord;
        pgpb.z = te.zCoord;
        pgpb.dimID = te.getWorldObj().provider.dimensionId;
        pgpb.buttonID = buttonID;
        EAPacketHandler.sendToServer(pgpb);
    }

    public static void issue(ChunkCoordinates te, int dimID, int buttonID) {
        PacketGUIPressButton pgpb = new PacketGUIPressButton();
        pgpb.x = te.posX;
        pgpb.y = te.posY;
        pgpb.z = te.posZ;
        pgpb.dimID = dimID;
        pgpb.buttonID = buttonID;
        EAPacketHandler.sendToServer(pgpb);
    }

    @Override
    public void execute(EntityPlayerMP player) {
        //System.out.println("Execution of the PacketGUIPressButton");
        //System.out.println(FMLCommonHandler.instance().getEffectiveSide());
        //System.out.println(String.format("x: %d, y: %d, z: %d, dimID: %d, buttonID: %d", x, y, z, dimID, buttonID));
        try {
            WorldServer ws = DimensionManager.getWorld(dimID);
            if (ws != null) {
                TileEntity te = ws.getTileEntity(x, y, z);
                if (te != null && te instanceof IHasButton)
                    ((IHasButton) te).handleButtonClick(buttonID);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
