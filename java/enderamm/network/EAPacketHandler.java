package enderamm.network;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

import java.util.EnumMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 23.02.14
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class EAPacketHandler {
    public static List<Class<? extends IPacket>> packetTypes = Lists
            .newArrayList();
    private static EnumMap<Side, FMLEmbeddedChannel> channels;

    public static void load() {
        registerPacketType(PacketGUIPressButton.class);
        registerPacketType(PacketChangeState.class);
        registerPacketType(PacketFireRay.class);
        registerPacketType(PacketFlightStatusUpdate.class);
        registerPacketType(PacketKeyUpdate.class);
        channels = NetworkRegistry.INSTANCE.newChannel("enderamm",
                new EAChannelHandler());
    }

    public static void registerPacketType(Class<? extends IPacket> ptype) {
        packetTypes.add(ptype);
    }

    public static void sendToAllPlayers(IPacket packet) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(Side.SERVER).writeOutbound(packet);
    }

    public static void sendToServer(IPacket packet) {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeOutbound(packet);
    }

    public static void sendToAllAround(IPacket packet, NetworkRegistry.TargetPoint point) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void sendToPlayer(EntityPlayer ep, IPacket packet) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(Side.SERVER)
                .attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(ep);
        channels.get(Side.SERVER).writeOutbound(packet);
    }
}
