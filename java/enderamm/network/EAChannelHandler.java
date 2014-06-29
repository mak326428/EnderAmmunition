package enderamm.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.NetworkRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 23.02.14
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public class EAChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket> {

    public EAChannelHandler() {
        for (Class clazz : EAPacketHandler.packetTypes)
            addDiscriminator(EAPacketHandler.packetTypes.indexOf(clazz), clazz);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, IPacket msg,
                           ByteBuf target) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            msg.write(new DataOutputStream(baos));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        target.writeBytes(baos.toByteArray());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source,
                           IPacket msg) {
        byte[] arr = new byte[source.readableBytes()];
        source.readBytes(arr);
        ByteArrayInputStream bais = new ByteArrayInputStream(arr);
        try {
            msg.read(new DataInputStream(bais));
        } catch (Throwable e) {
            e.printStackTrace();
            return;
        }
        if (!FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
            EntityPlayerMP player = ((NetHandlerPlayServer) netHandler).playerEntity;
            msg.execute(player);
        } else msg.execute(null);
    }
}
