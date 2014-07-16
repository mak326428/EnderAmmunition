package enderamm.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 29.06.14
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
public class PacketFlightStatusUpdate extends IPacket {

    public boolean allow, is;

    public static void issue(EntityPlayer ep, boolean allowF, boolean isF) {
        PacketFlightStatusUpdate pfsu = new PacketFlightStatusUpdate();
        pfsu.allow = allowF;
        pfsu.is = isF;
        EAPacketHandler.sendToPlayer(ep, pfsu);
    }

    @Override
    public void read(DataInputStream bytes) throws Throwable {
        allow = bytes.readBoolean();
        is = bytes.readBoolean();
    }

    @Override
    public void write(DataOutputStream bytes) throws Throwable {
        bytes.writeBoolean(allow);
        bytes.writeBoolean(is);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void execute(EntityPlayer par1) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        player.capabilities.allowFlying = allow;
        player.capabilities.isFlying = is;
    }
}
