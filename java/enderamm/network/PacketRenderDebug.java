package enderamm.network;

import cpw.mods.fml.common.FMLCommonHandler;
import enderamm.DebugReference;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 15.07.14
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */
public class PacketRenderDebug extends IPacket {
    public PacketRenderDebug() {
    }

    @Override
    public void read(DataInputStream bytes) throws Throwable {
        translateX = bytes.readDouble();
        translateY = bytes.readDouble();
        translateZ = bytes.readDouble();
        scaleX = bytes.readDouble();
        scaleY = bytes.readDouble();
        scaleZ = bytes.readDouble();
        rotateX = bytes.readDouble();
        rotateY = bytes.readDouble();
        rotateZ = bytes.readDouble();
    }

    @Override
    public void write(DataOutputStream bytes) throws Throwable {
        bytes.writeDouble(translateX);
        bytes.writeDouble(translateY);
        bytes.writeDouble(translateZ);
        bytes.writeDouble(scaleX);
        bytes.writeDouble(scaleY);
        bytes.writeDouble(scaleZ);
        bytes.writeDouble(rotateX);
        bytes.writeDouble(rotateY);
        bytes.writeDouble(rotateZ);
    }

    public void execute(EntityPlayerMP player) {
        // TODO: handle
        DebugReference.translateX = this.translateX;
        DebugReference.translateY = this.translateY;
        DebugReference.translateZ = this.translateZ;
        DebugReference.scaleX = this.scaleX;
        DebugReference.scaleY = this.scaleY;
        DebugReference.scaleZ = this.scaleZ;
        DebugReference.rotateX = this.rotateX;
        DebugReference.rotateY = this.rotateY;
        DebugReference.rotateZ = this.rotateZ;
    }

    public double translateX = 0.0D, translateY = 0.0D, translateZ = 0.0D, scaleX = 0.0D, scaleY = 0.0D, scaleZ = 0.0D, rotateX = 0.0D, rotateY = 0.0D, rotateZ = 0.0D;
}
