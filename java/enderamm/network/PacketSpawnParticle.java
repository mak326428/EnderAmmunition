package enderamm.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 21.07.14
 * Time: 19:40
 * To change this template use File | Settings | File Templates.
 */
public class PacketSpawnParticle extends IPacket {

    public String name;
    public double x, y, z, velX, velY, velZ;

    @Override
    public void read(DataInputStream bytes) throws Throwable {
        name = bytes.readUTF();
        x = bytes.readDouble();
        y = bytes.readDouble();
        z = bytes.readDouble();
        velX = bytes.readDouble();
        velY = bytes.readDouble();
        velZ = bytes.readDouble();

    }

    @Override
    public void write(DataOutputStream bytes) throws Throwable {
        bytes.writeUTF(name);
        bytes.writeDouble(x);
        bytes.writeDouble(y);
        bytes.writeDouble(z);
        bytes.writeDouble(velX);
        bytes.writeDouble(velY);
        bytes.writeDouble(velZ);
    }

    public static void issue(String name, double x, double y, double z, double velX, double velY, double velZ, World world) {
        PacketSpawnParticle psp = new PacketSpawnParticle();
        psp.name = name;
        psp.x = x;
        psp.y = y;
        psp.z = z;
        psp.velX = velX;
        psp.velY = velY;
        psp.velZ = velZ;
        EAPacketHandler.sendToAllAround(psp, new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 128.0D));
    }

    @SideOnly(Side.CLIENT)
    public void execute(EntityPlayer player) {
        World w = player.worldObj;
        w.spawnParticle(name, x, y, z, velX, velY, velZ);
    }
}
