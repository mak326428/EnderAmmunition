package enderamm.item;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.TEProxy;
import enderamm.network.EAKeyboard;
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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemWarpGem extends ItemBasicRF {

    public ItemWarpGem() {
        super(10 * 1000 * 1000, 100000, "Warp Gem", "enderamm:warpGem_main");
    }

    public void handlePacketServer(PacketWarpGemAction packet, ItemStack warpGemStack, EntityPlayer player) {

    }

    public void handlePacketClient(PacketWarpGemAction packet, ItemStack warpGemStack, EntityPlayer player) {

    }

}