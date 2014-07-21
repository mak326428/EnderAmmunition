package enderamm.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import enderamm.network.PacketRenderDebug;
import enderamm.network.PacketSpawnParticle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 21.07.14
 * Time: 19:11
 * To change this template use File | Settings | File Templates.
 */
public class ItemGrowthAccelerator extends ItemBasicRF {

    public ItemGrowthAccelerator() {
        super(1000000, 100000, "Growth Accelerator", "enderamm:growth_booster");
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float argU1, float argU2, float argU3) {
        if (!world.isRemote && draw(stack, 25000)) {
            Block blck = world.getBlock(x, y, z);
            if (blck instanceof IPlantable) {
                for (int i = 0; i < 400; i++)
                    blck.updateTick(world, x, y, z, itemRand);
                for (int i1 = 0; i1 < 25; ++i1) {
                    double d0 = itemRand.nextGaussian() * 0.02D;
                    double d1 = itemRand.nextGaussian() * 0.02D;
                    double d2 = itemRand.nextGaussian() * 0.02D;
                    PacketSpawnParticle.issue("happyVillager", (double) ((float) x + itemRand.nextFloat()), (double) y + (double) itemRand.nextFloat() * blck.getBlockBoundsMaxY(), (double) ((float) z + itemRand.nextFloat()), d0, d1, d2, world);
                }
            }
        }
        player.swingItem();
        return false;
    }

}
