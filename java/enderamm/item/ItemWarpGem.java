package enderamm.item;

import cofh.api.energy.IEnergyContainerItem;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.EnderAmmunition;
import enderamm.TEProxy;
import enderamm.network.EAKeyboard;
import enderamm.network.EAPacketHandler;
import enderamm.network.PacketWarpGemAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnderPearl;
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
    public static final String NET_NBT_ERROR_ID = "id";
    public static double RF_PER_BLOCK = 1000;

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

        public static WarpPoint createDummy(String name) {
            WarpPoint wp = new WarpPoint();
            wp.x = 0;
            wp.y = 0;
            wp.z = 0;
            wp.dimID = 0;
            wp.yaw = 0;
            wp.pitch = 0;
            wp.name = name;
            return wp;
        }
    }

    public ItemWarpGem() {
        super(50 * 1000 * 1000, 100000, "Warp Gem", "enderamm:warpGem_main");
    }

    public void handlePacketServer(PacketWarpGemAction packet, ItemStack warpGemStack, EntityPlayer player) {
        int actionID = packet.actionID;
        NBTTagCompound actionData = packet.actionData;
        if (actionID == 0) {
            String waypointName = actionData.getString(NET_NBT_WPOINT_NAME);
            if (getPointByName(warpGemStack, waypointName) != null) {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "Warp point with such name already exists"));
                sendServerToClientError(0, player);
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
            if (getAllPoints(warpGemStack).size() < 11)
                addPoint(warpGemStack, point);
        } else if (actionID == 1) {
            String waypointName = actionData.getString(NET_NBT_WPOINT_NAME);
            WarpPoint point = getPointByName(warpGemStack, waypointName);
            if (point == null) {
                sendServerToClientError(4, player);
                return;
            }
            if (point.dimID != player.worldObj.provider.dimensionId) {
                sendServerToClientError(2, player);
                return;
            }
            EAVector3 playerVec = EAVector3.fromEntityCenter(player);
            EAVector3 targetVec = new EAVector3(point.x, point.y, point.z);
            double distance = targetVec.subtract(playerVec).mag();
            int rfCost = (int)Math.round(distance * RF_PER_BLOCK);
            if (getEnergyStored(warpGemStack) < rfCost) {
                sendServerToClientError(1, player);
                return;
            }
            extractEnergy(warpGemStack, rfCost, false);
            player.setPositionAndUpdate(point.x, point.y, point.z);
            player.setLocationAndAngles(point.x, point.y, point.z, (float)point.yaw, (float)point.pitch);
            sendServerToClientError(3, player);
            player.worldObj.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        } else if (actionID == 2) {
            String wpName = actionData.getString(NET_NBT_WPOINT_NAME);
            if (getPointByName(warpGemStack, wpName) == null) {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "No warp point with such name exists"));
                sendServerToClientError(4, player);
                return;
            }
            removePointByName(warpGemStack, wpName);
        }
    }

    enum Error {
        WP_ALREADY_EXISTS,
        NOT_ENOUGH_ENERGY,
        WRONG_DIMENSION,
        SUCCESSFULLY_TELEPORTED,
        WP_DOESNT_EXIST
    }

    @SideOnly(Side.CLIENT)
    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.epic;
    }

    public void sendServerToClientError(int id, EntityPlayer player) {
        //System.out.println(Error.values()[id]);
        PacketWarpGemAction p = new PacketWarpGemAction();
        p.actionID = 3;
        p.actionData = new NBTTagCompound();
        p.actionData.setInteger(NET_NBT_ERROR_ID, id);
        EAPacketHandler.sendToPlayer(player, p);
    }

    @SideOnly(Side.CLIENT)
    public void handlePacketClient(PacketWarpGemAction packet, ItemStack warpGemStack, EntityPlayer player) {
        if (packet.actionID == 3) {
            if (packet.actionData.getInteger(NET_NBT_ERROR_ID) == 3) {
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        }
    }

    public static WarpPoint getPointByName(ItemStack stack, String name) {
        if (stack == null || name == null) return null;
        List<WarpPoint> allPoints = getAllPoints(stack);
        for (WarpPoint point : allPoints) {
            if (point.name.equalsIgnoreCase(name)) {
                return point;
            }
        }
        return null;
    }

    public static void addPoint(ItemStack stack, WarpPoint point) {
        if (stack == null || point == null) return;
        List<WarpPoint> newPoints = Lists.newArrayList();
        newPoints.addAll(getAllPoints(stack));
        newPoints.add(point);
        replaceAllPointsWith(stack, newPoints);
    }

    public static void removePointByName(ItemStack stack, String name) {
        if (stack == null || name == null) return;
        List<WarpPoint> points = getAllPoints(stack);
        for (int i = 0; i < points.size(); i++) {
            WarpPoint item = points.get(i);
            if (item.name.equalsIgnoreCase(name))
                points.remove(i);
        }
        replaceAllPointsWith(stack, points);
    }

    public static List<WarpPoint> getAllPoints(ItemStack stack) {
        if (stack == null) return Lists.newArrayList();
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
        if (stack == null || newList == null) return;
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

    public static void sendAddWaypointPacket(String name) {
        PacketWarpGemAction packet = new PacketWarpGemAction();
        packet.actionID = 0;
        packet.actionData = new NBTTagCompound();
        packet.actionData.setString(NET_NBT_WPOINT_NAME, name);
        EAPacketHandler.sendToServer(packet);
    }

    public static void sendTeleportRequest(String name) {
        PacketWarpGemAction packet = new PacketWarpGemAction();
        packet.actionID = 1;
        packet.actionData = new NBTTagCompound();
        packet.actionData.setString(NET_NBT_WPOINT_NAME, name);
        EAPacketHandler.sendToServer(packet);
    }

    public static void sendRemoveWarpPointRequest(String name) {
        PacketWarpGemAction packet = new PacketWarpGemAction();
        packet.actionID = 2;
        packet.actionData = new NBTTagCompound();
        packet.actionData.setString(NET_NBT_WPOINT_NAME, name);
        EAPacketHandler.sendToServer(packet);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (par2World.isRemote) {
            par3EntityPlayer.openGui(EnderAmmunition.instance, 8192, par2World, (int) par3EntityPlayer.posX, (int) par3EntityPlayer.posY, (int) par3EntityPlayer.posZ);
        }
        return par1ItemStack;
    }

}