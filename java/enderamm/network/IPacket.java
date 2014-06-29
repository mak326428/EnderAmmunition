package enderamm.network;

import net.minecraft.entity.player.EntityPlayerMP;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 23.02.14
 * Time: 14:41
 * To change this template use File | Settings | File Templates.
 */
public abstract class IPacket {
    public IPacket() {
    }

    /**
     * Read stuff here.
     *
     * @param bytes
     *            DataInputStream, for convenience
     * @throws Throwable
     *             If IOException/something else bad occured. (like EOF)
     */
    public abstract void read(DataInputStream bytes) throws Throwable;

    /**
     * Write stuff here.
     *
     * @param bytes
     *            DataOutputStream, for convenience
     * @throws Throwable
     */
    public abstract void write(DataOutputStream bytes) throws Throwable;

    /**
     * Executed right after {@link #read(DataInputStream)}. Isn't marked as
     * abstract, so SideOnly(Side.CLIENT) is safe here.
     * @param player
     */
    public void execute(EntityPlayerMP player) {}
}
