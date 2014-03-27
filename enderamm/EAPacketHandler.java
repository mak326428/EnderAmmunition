package enderamm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.item.FireRayFX;

/**
 * Just a not so powerful packet handler, as there's no such great demand.
 */
public class EAPacketHandler implements IPacketHandler {

	public EAPacketHandler() {
	}

	@SideOnly(Side.CLIENT)
	public void handleFlightStatusUpdate(boolean allowFlight, boolean isFlying) {
		if (!FMLCommonHandler.instance().getSide().isClient())
			return;
		EntityPlayer ep = Minecraft.getMinecraft().thePlayer;
		ep.capabilities.allowFlying = allowFlight;
		ep.capabilities.isFlying = isFlying;
	}

	public static Map<EntityPlayer, Boolean> playersCtrl = Maps.newHashMap();
	public static Map<EntityPlayer, Boolean> playersSpace = Maps.newHashMap();
	public static Map<EntityPlayer, Boolean> playersFire = Maps.newHashMap();

	public void handleKeyUpdate(boolean ctrl, boolean space, boolean fire,
			EntityPlayer player) {
		// System.out.println(String.format("Ctrl: %s, Space: %s, Fire: %s", ""
		// + ctrl, "" + space, "" + fire));
		if (playersCtrl.containsKey(player))
			playersCtrl.remove(player);
		if (playersSpace.containsKey(player))
			playersSpace.remove(player);
		if (playersFire.containsKey(player))
			playersFire.remove(player);
		playersCtrl.put(player, ctrl);
		playersSpace.put(player, space);
		playersFire.put(player, fire);
		// System.out.println("isPressingCtrl(player): " +
		// isPressingCtrl(player));
	}
	
	@SideOnly(Side.CLIENT)
	public void handleRenderUpdate(int x, int y, int z) {
		Minecraft.getMinecraft().theWorld.markBlockForRenderUpdate(x, y, z);
	}

	public static boolean isPressingSpace(EntityPlayer player) {
		if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
			if (!playersSpace.containsKey(player))
				playersSpace.put(player, false);
			return playersSpace.get(player);
		} else {
			return ((EAClientProxy) EnderAmmunition.PROXY).lastSpace;
		}
	}

	public static boolean isPressingFire(EntityPlayer player) {
		if (!playersFire.containsKey(player))
			playersFire.put(player, false);
		return playersFire.get(player);
	}

	public static boolean isPressingCtrl(EntityPlayer player) {
		if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
			if (!playersCtrl.containsKey(player))
				playersCtrl.put(player, false);
			return playersCtrl.get(player);
		} else {
			return ((EAClientProxy) EnderAmmunition.PROXY).lastCtrl;
		}
	}

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals(EnderAmmunition.MOD_ID)) {
			try {
				DataInputStream dis = new DataInputStream(
						new ByteArrayInputStream(packet.data));
				/*
				 * 0 = Flight Kick update (server -> client; allowFlight;
				 * isFlying) 1 = Key Update (client -> server; ctrl; space;
				 * fire) 2 = Fire Ray (server -> client; informing clients that
				 * a player shoot a ray)
				 */
				switch (dis.readInt()) {
				case 0:
					handleFlightStatusUpdate(dis.readBoolean(),
							dis.readBoolean());
					break;
				case 1:
					handleKeyUpdate(dis.readBoolean(), dis.readBoolean(),
							dis.readBoolean(), (EntityPlayer) player);
					break;
				case 2:
					handleFireRayPacket(dis.readFloat(), dis.readFloat(),
							dis.readFloat(), dis.readFloat(), dis.readFloat(),
							dis.readFloat());
					break;
				case 3:
					handleRenderUpdate(dis.readInt(), dis.readInt(), dis.readInt());
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void handleFireRayPacket(float sX, float sY, float sZ, float tX,
			float tY, float tZ) {
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			World w = Minecraft.getMinecraft().theWorld;
			FireRayFX pe = new FireRayFX(w, sX, sY, sZ, tX, tY, tZ, 48, 141,
					255, 40);
			Minecraft.getMinecraft().effectRenderer.addEffect(pe);
		}
	}

	public static void sendFlightStatusPacket(EntityPlayer ep,
			boolean allowFlight, boolean isFlying) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(0);
			dos.writeBoolean(allowFlight);
			dos.writeBoolean(isFlying);

		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] data = baos.toByteArray();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = EnderAmmunition.MOD_ID;
		packet.length = data.length;
		packet.data = data;
		packet.isChunkDataPacket = false;
		PacketDispatcher.sendPacketToPlayer(packet, (Player) ep);
	}

	public static void sendKeyUpdatePacket(boolean ctrl, boolean space,
			boolean fire) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(1);
			dos.writeBoolean(ctrl);
			dos.writeBoolean(space);
			dos.writeBoolean(fire);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] data = baos.toByteArray();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = EnderAmmunition.MOD_ID;
		packet.length = data.length;
		packet.data = data;
		packet.isChunkDataPacket = false;
		PacketDispatcher.sendPacketToServer(packet);
	}

	public static void sendFireRayPacket(float sX, float sY, float sZ,
			float tX, float tY, float tZ, World w) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(2);
			dos.writeFloat(sX);
			dos.writeFloat(sY);
			dos.writeFloat(sZ);
			dos.writeFloat(tX);
			dos.writeFloat(tY);
			dos.writeFloat(tZ);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] data = baos.toByteArray();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = EnderAmmunition.MOD_ID;
		packet.length = data.length;
		packet.data = data;
		packet.isChunkDataPacket = false;
		PacketDispatcher.sendPacketToAllAround(sX, sY, sZ, 512.0D,
				w.provider.dimensionId, packet);
	}
	
	public static void sendReRenderPacket(World w, int x, int y, int z) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(3);
			dos.writeFloat(x);
			dos.writeFloat(y);
			dos.writeFloat(z);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] data = baos.toByteArray();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = EnderAmmunition.MOD_ID;
		packet.length = data.length;
		packet.data = data;
		packet.isChunkDataPacket = false;
		PacketDispatcher.sendPacketToAllAround(x, y, z, 64.0D,
				w.provider.dimensionId, packet);
	}

}
