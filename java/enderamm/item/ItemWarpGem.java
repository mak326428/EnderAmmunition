package enderamm.item;

import cofh.api.energy.IEnergyContainerItem;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.TEProxy;
import enderamm.network.EAKeyboard;
import enderamm.network.EAPacketHandler;
import enderamm.network.PacketWarpGemAction;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;
import java.util.Random;

public class ItemWarpGem extends ItemBasicRF {

    public static final String NET_NBT_WPOINT_NAME = "wpname";

    public static final String ITEM_NBT_POINTS_LIST = "wpoints";
    public static final String ITEM_NBT_X = "x";
    public static final String ITEM_NBT_Y = "y";
    public static final String ITEM_NBT_Z = "z";
    public static final String ITEM_NBT_YAW = "yaw";
    public static final String ITEM_NBT_PITCH = "pitch";
    public static final String ITEM_NBT_NAME = "name";
    public static final String ITEM_NBT_DIMID = "dimID";

    public static class WarpPoint {
        public int dimID;
        public double x;
        public double y;
        public double z;
        public double yaw;
        public double pitch;
        public String name;
    }

    public ItemWarpGem() {
        super(10 * 1000 * 1000, 100000, "Warp Gem", "enderamm:warpGem_main");
    }

    public void handlePacketServer(PacketWarpGemAction packet, ItemStack warpGemStack, EntityPlayer player) {
        int actionID = packet.actionID;
        NBTTagCompound actionData = packet.actionData;
        if (actionID == 0) {
            String waypointName = actionData.getString(NET_NBT_WPOINT_NAME);
            if (getPointByName(warpGemStack, waypointName) != null) {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "Warp point with such name already exists"));
                return;
            }
            WarpPoint point = new WarpPoint();
            point.x = player.posX;
            point.y = player.posY;
            point.z = player.posZ;
            point.yaw = player.rotationYaw;
            point.pitch = player.rotationPitch;
            point.name = waypointName;
            point.dimID = player.worldObj.provider.dimensionId;
            addPoint(warpGemStack, point);
        }
    }

    public void handlePacketClient(PacketWarpGemAction packet, ItemStack warpGemStack, EntityPlayer player) {

    }

    public static WarpPoint getPointByName(ItemStack stack, String name) {
        List<WarpPoint> allPoints = getAllPoints(stack);
        for (WarpPoint point : allPoints) {
            if (point.name.equalsIgnoreCase(name)) {
                return point;
            }
        }
        return null;
    }

    public static void addPoint(ItemStack stack, WarpPoint point) {
        List<WarpPoint> newPoints = Lists.newArrayList();
        newPoints.addAll(getAllPoints(stack));
        newPoints.add(point);
        replaceAllPointsWith(stack, newPoints);
    }

    public static void removePointByName(ItemStack stack, String name) {
        List<WarpPoint> points = getAllPoints(stack);
        int indexToDelete = -1;
        for (int i = 0; i < points.size(); i++) {
            WarpPoint item = points.get(i);
            if (item.name.equalsIgnoreCase(name))
                points.remove(i);
        }
    }

    public static List<WarpPoint> getAllPoints(ItemStack stack) {
        if (stack.stackTagCompound == null)
            stack.stackTagCompound = new NBTTagCompound();
        NBTTagList list = stack.stackTagCompound.getTagList(ITEM_NBT_POINTS_LIST, Constants.NBT.TAG_COMPOUND);
        List<WarpPoint> points = Lists.newArrayList();
        for (int i = 0; i < list.tagCount(); i++) {
            WarpPoint point = new WarpPoint();
            NBTTagCompound t2 = list.getCompoundTagAt(i);
            point.name = t2.getString(ITEM_NBT_NAME);
            point.x = t2.getDouble(ITEM_NBT_X);
            point.y = t2.getDouble(ITEM_NBT_Y);
            point.z = t2.getDouble(ITEM_NBT_Z);
            point.yaw = t2.getDouble(ITEM_NBT_YAW);
            point.pitch = t2.getDouble(ITEM_NBT_PITCH);
            point.dimID = t2.getInteger(ITEM_NBT_DIMID);
            points.add(point);
        }
        return points;
    }

    private static void replaceAllPointsWith(ItemStack stack, List<WarpPoint> newList) {
        if (stack.stackTagCompound == null)
            stack.stackTagCompound = new NBTTagCompound();
        NBTTagList nbtList = new NBTTagList();
        for (WarpPoint point : newList) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString(ITEM_NBT_NAME, point.name);
            nbt.setDouble(ITEM_NBT_X, point.x);
            nbt.setDouble(ITEM_NBT_Y, point.y);
            nbt.setDouble(ITEM_NBT_Z, point.z);
            nbt.setDouble(ITEM_NBT_YAW, point.yaw);
            nbt.setDouble(ITEM_NBT_PITCH, point.pitch);
            nbt.setInteger(ITEM_NBT_DIMID, point.dimID);
            nbtList.appendTag(nbt);
        }
        stack.getTagCompound().setTag(ITEM_NBT_POINTS_LIST, nbtList);
    }

    public void sendAddWaypointPacket(String name) {
        PacketWarpGemAction packet = new PacketWarpGemAction();
        packet.actionID = 0;
        packet.actionData = new NBTTagCompound();
        packet.actionData.setString(NET_NBT_WPOINT_NAME, name);
        EAPacketHandler.sendToServer(packet);
    }

    public void sendTeleportRequest(String name) {
        PacketWarpGemAction packet = new PacketWarpGemAction();
        packet.actionID = 1;
        packet.actionData = new NBTTagCompound();
        packet.actionData.setString(NET_NBT_WPOINT_NAME, name);
        EAPacketHandler.sendToServer(packet);
    }

    public void sendRemoveWarpPointRequest(String name) {
        PacketWarpGemAction packet = new PacketWarpGemAction();
        packet.actionID = 2;
        packet.actionData = new NBTTagCompound();
        packet.actionData.setString(NET_NBT_WPOINT_NAME, name);
        EAPacketHandler.sendToServer(packet);
    }

}