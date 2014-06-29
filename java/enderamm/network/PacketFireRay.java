package enderamm.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.item.FireRayFX;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 29.06.14
 * Time: 19:54
 * To change this template use File | Settings | File Templates.
 */
public class PacketFireRay extends IPacket {

    public float sX, sY, sZ, tX, tY, tZ;

    @Override
    public void read(DataInputStream bytes) throws Throwable {
        sX = bytes.readFloat();
        sY = bytes.readFloat();
        sZ = bytes.readFloat();
        tX = bytes.readFloat();
        tY = bytes.readFloat();
        tZ = bytes.readFloat();
    }

    @Override
    public void write(DataOutputStream bytes) throws Throwable {
        bytes.writeFloat(sX);
        bytes.writeFloat(sY);
        bytes.writeFloat(sZ);
        bytes.writeFloat(tX);
        bytes.writeFloat(tY);
        bytes.writeFloat(tZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void execute(EntityPlayerMP ep) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            World w = Minecraft.getMinecraft().theWorld;
            FireRayFX pe = new FireRayFX(w, sX, sY, sZ, tX, tY, tZ, 48, 141,
                    255, 40);
            Minecraft.getMinecraft().effectRenderer.addEffect(pe);
        }
    }

    public static void issue(float sX, float sY, float sZ, float tX, float tY, float tZ, World world) {
        PacketFireRay pfr = new PacketFireRay();
        pfr.sX = sX;
        pfr.sY = sY;
        pfr.sZ = sZ;
        pfr.tX = tX;
        pfr.tY = tY;
        pfr.tZ = tZ;
        EAPacketHandler.sendToAllAround(pfr, new NetworkRegistry.TargetPoint(world.provider.dimensionId, (double) sX, (double) sY, (double) sZ, 256.0D));
    }
}
