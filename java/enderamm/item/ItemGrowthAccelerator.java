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

    public static Map<Location, Integer> accelerateQueue = Maps.newHashMap();

    public static class Location {
        public int dimID, x, y, z;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (Location loc : accelerateQueue.keySet()) {
                World world = DimensionManager.getWorld(loc.dimID);
                Block blck = world.getBlock(loc.x, loc.y, loc.z);
                if (blck != null && blck != Blocks.air) {
                    for (int i = 0; i < 5; i++)
                        blck.updateTick(world, loc.x, loc.y, loc.z, itemRand);
                    for (int i1 = 0; i1 < 10; ++i1) {
                        double d0 = itemRand.nextGaussian() * 0.02D;
                        double d1 = itemRand.nextGaussian() * 0.02D;
                        double d2 = itemRand.nextGaussian() * 0.02D;
                        PacketSpawnParticle.issue("happyVillager", (double) ((float) loc.x + itemRand.nextFloat()), (double) loc.y + (double) itemRand.nextFloat() * blck.getBlockBoundsMaxY(), (double) ((float) loc.z + itemRand.nextFloat()), d0, d1, d2, world);
                    }
                }
            }
            Map<Location, Integer> newQ = Maps.newHashMap();
            for (Map.Entry<Location, Integer> l : accelerateQueue.entrySet()) {
                if (l.getValue() - 1 > 0)
                    newQ.put(l.getKey(), l.getValue() - 1);
            }
            accelerateQueue.clear();
            accelerateQueue = newQ;
        }
    }

    public ItemGrowthAccelerator() {
        super(1000000, 100000, "Growth Accelerator", "enderamm:growth_booster");
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float argU1, float argU2, float argU3) {
        if (!world.isRemote && draw(stack, 25000)) {
            Location loc = new Location();
            loc.dimID = world.provider.dimensionId;
            loc.x = x;
            loc.y = y;
            loc.z = z;
            accelerateQueue.put(loc, 100);
        }
        player.swingItem();
        return false;
    }

}
