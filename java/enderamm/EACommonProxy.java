package enderamm;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import enderamm.block.BlockHMS;
import enderamm.block.BlockRockExterminator;
import enderamm.block.TileEntityHMS;
import enderamm.block.TileEntityRockExterminator;
import enderamm.item.*;
import enderamm.network.EAPacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

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

    public void preInit(Configuration config) {
        ItemAnnihilationManipulator.ALLOW_EXPLOSION = config.get(Configuration.CATEGORY_GENERAL, "allowExplosionFeatures", true, "Toggles the most dangerous features griefing-wise (Annihilation Manipulator's explosion feature, Ender-Infused Explosive Arrows, etc.)").getBoolean(true);
        MinecraftForge.EVENT_BUS.register(new EAFlightHandler());
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
        EAInit.addRecipes();
    }

    public void postInit() {

    }

    public void init() {

        // MinecraftForge.EVENT_BUS.register(new EAEventHandler());
    }
}
