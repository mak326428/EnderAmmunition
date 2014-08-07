package enderamm;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.block.BlockHMS;
import enderamm.block.BlockRockExterminator;
import enderamm.block.TileEntityHMS;
import enderamm.block.TileEntityRockExterminator;
import enderamm.item.*;
import enderamm.network.EAPacketHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Method;

public class EACommonProxy {

    public static ItemArmorEnderBase itemArmorEnderBoots;
    public static ItemArmorEnderBase itemArmorEnderLeggings;
    public static ItemArmorEnderBase itemArmorEnderHelmet;
    public static ItemArmorEnderBase itemArmorEnderChestplate;
    public static ItemWarpGem itemWarpGem;
    public static EAItemMaterial itemMaterial;
    public static ItemAnnihilationManipulator itemAnnihilationManipulator;
    public static ItemHealingGem itemHealingGem;
    public static ItemEnderMagnet itemEnderMagnet;
    public static ItemEnderBow itemEnderBow;
    public static ItemEnderArrow itemEnderArrow;
    public static BlockHMS blockHMS;
    public static ItemRFDebug itemRFDebug;
    public static BlockRockExterminator blockRockExterminator;

    public static final boolean DEBUG = false;

    public static class RawItemData {
        public final String modId;
        public final String itemName;

        public RawItemData(String id, String name) {
            modId = id;
            itemName = name;
        }
    }

    private static Method getUniqueName_Item, getUniqueName_Block;

    public static boolean rawReflectionDone = false;

    private static void lazyReflectionInit() {
        if (!rawReflectionDone) {
            try {
                getUniqueName_Item = GameData.class.getDeclaredMethod("getUniqueName", Item.class);
                getUniqueName_Block = GameData.class.getDeclaredMethod("getUniqueName", Block.class);
                getUniqueName_Item.setAccessible(true);
                getUniqueName_Block.setAccessible(true);
            } catch (Exception e) {
                System.out.println("[EA] Reflection failed. This is a fatal error and not recoverable");
                throw new RuntimeException(e);
            }
            rawReflectionDone = true;
        }
    }

    public static RawItemData getItemData(ItemStack is) {
        lazyReflectionInit();
        try {
            Item i = is.getItem();
            if (i instanceof ItemBlock) {
                Block b = Block.getBlockFromItem(i);
                GameRegistry.UniqueIdentifier ui = (GameRegistry.UniqueIdentifier) getUniqueName_Block.invoke(null, b);
                return new RawItemData(ui.modId, ui.name);
            } else {
                GameRegistry.UniqueIdentifier ui = (GameRegistry.UniqueIdentifier) getUniqueName_Item.invoke(null, i);
                return new RawItemData(ui.modId, ui.name);
            }
        } catch (Throwable t) {
            System.out.println("[EA] Reflection failed. Weird error, report it.");
            t.printStackTrace();
            return null;
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTooltip(ItemTooltipEvent event) {
        if (DEBUG) {
            RawItemData rid = getItemData(event.itemStack);
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.toolTip.add("");
                event.toolTip.add(String.format("ID: %s:%s", rid.modId, rid.itemName));
                event.toolTip.add("Metadata: " + event.itemStack.getItemDamage());
                event.toolTip.add("Stack Size: " + event.itemStack.stackSize);
            }
        }
    }

    public void preInit(Configuration config) {
        TEProxy.reflect();
        ItemAnnihilationManipulator.ALLOW_EXPLOSION = config.get(Configuration.CATEGORY_GENERAL, "allowExplosionFeatures", true, "Toggles the most dangerous features griefing-wise (Annihilation Manipulator's explosion feature, Ender-Infused Explosive Arrows, etc.)").getBoolean(true);
        MinecraftForge.EVENT_BUS.register(new EAFlightHandler());
        MinecraftForge.EVENT_BUS.register(this);
        NetworkRegistry.INSTANCE.registerGuiHandler(EnderAmmunition.instance, new EAGUIHandler());
        EAPacketHandler.load();
        itemArmorEnderBoots = new ItemArmorEnderBase(3);
        GameRegistry.registerItem(itemArmorEnderBoots, "armor_ender_boots");
        itemArmorEnderLeggings = new ItemArmorEnderBase(2);
        GameRegistry.registerItem(itemArmorEnderLeggings,
                "armor_ender_leggings");
        itemArmorEnderChestplate = new ItemArmorEnderBase(1);
        GameRegistry.registerItem(itemArmorEnderChestplate,
                "armor_ender_chestplate");
        itemArmorEnderHelmet = new ItemArmorEnderBase(0);
        GameRegistry.registerItem(itemArmorEnderHelmet, "armor_ender_helmet");
        itemMaterial = new EAItemMaterial();
        GameRegistry.registerItem(itemMaterial, "item_material");
        itemWarpGem = new ItemWarpGem();
        GameRegistry.registerItem(itemWarpGem, "tool_warp_gem");
        itemAnnihilationManipulator = new ItemAnnihilationManipulator();
        GameRegistry.registerItem(itemAnnihilationManipulator,
                "tool_fastbreaker");
        itemRFDebug = new ItemRFDebug();
        GameRegistry.registerItem(itemRFDebug, "tool_rf_debug");
        itemHealingGem = new ItemHealingGem();
        GameRegistry.registerItem(itemHealingGem, "tool_healing_gem");
        itemEnderArrow = new ItemEnderArrow();
        GameRegistry.registerItem(itemEnderArrow, "item_ender_arrow");
        itemEnderBow = new ItemEnderBow();
        GameRegistry.registerItem(itemEnderBow, "tool_ender_bow");
        itemEnderMagnet = new ItemEnderMagnet();
        GameRegistry.registerItem(itemEnderMagnet, "tool_ender_magnet");
        blockHMS = new BlockHMS();
        GameRegistry.registerBlock(blockHMS, "block_hms");
        GameRegistry.registerTileEntity(TileEntityHMS.class, "enderamm.block.TileEntityHMS");
        blockRockExterminator = new BlockRockExterminator();
        GameRegistry.registerBlock(blockRockExterminator, "rock_exterminator");
        GameRegistry.registerTileEntity(TileEntityRockExterminator.class, "enderamm.block.TileEntityRockExterminator");
        TEProxy.reflect();
        EAInit.addRecipes();
    }

    public void postInit() {

    }

    public void init() {

        // MinecraftForge.EVENT_BUS.register(new EAEventHandler());
    }
}
