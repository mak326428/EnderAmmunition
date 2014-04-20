package enderamm;

import java.util.EnumSet;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import enderamm.item.EAFlightHandler;
import enderamm.item.EAItemMaterial;
import enderamm.item.ItemAnnihilationManipulator;
import enderamm.item.ItemArmorEnderBase;
import enderamm.item.ItemEnderMagnet;
import enderamm.item.ItemHealingGem;
import enderamm.item.ItemWarpGem;

public class EACommonProxy {

	public static int enderBootsID;
	public static int enderLeggingsID;
	public static int enderHelmetID;
	public static int enderChestplateID;
	public static int materialID;
	public static int essenceID;
	public static int itemWarpGemID;
	public static int annihilationManipulatorID;
	public static int healingGemID;
	public static int magnetID;

	public static ItemArmorEnderBase itemArmorEnderBoots;
	public static ItemArmorEnderBase itemArmorEnderLeggings;
	public static ItemArmorEnderBase itemArmorEnderHelmet;
	public static ItemArmorEnderBase itemArmorEnderChestplate;
	public static ItemWarpGem itemWarpGem;
	public static EAItemMaterial itemMaterial;
	public static ItemAnnihilationManipulator itemAnnihilationManipulator;
	public static ItemHealingGem itemHealingGem;
	public static ItemEnderMagnet itemEnderMagnet;

	public void preInit(Configuration config) {
		enderBootsID = config.getItem("itemArmorEnderBoots", 24564).getInt();
		enderLeggingsID = config.getItem("itemArmorEnderLeggings", 24565)
				.getInt();
		enderHelmetID = config.getItem("itemArmorEnderHelmet", 24566).getInt();
		enderChestplateID = config.getItem("itemArmorEnderChestplate", 24567)
				.getInt();
		materialID = config.getItem("itemMaterial", 24569).getInt();
		itemWarpGemID = config.getItem("itemWarpGem", 24570).getInt();
		annihilationManipulatorID = config.getItem(
				"itemAnnihilationManipulator", 24571).getInt();
		healingGemID = config.getItem("itemHealingGem", 24572).getInt();
		magnetID = config.getItem("itemMagnet", 24573).getInt();
		MinecraftForge.EVENT_BUS.register(new EAFlightHandler());
	}

	public void postInit() {

	}

	public void init() {
		itemArmorEnderBoots = new ItemArmorEnderBase(enderBootsID, 3);
		GameRegistry.registerItem(itemArmorEnderBoots, "armor_ender_boots");
		itemArmorEnderLeggings = new ItemArmorEnderBase(enderLeggingsID, 2);
		GameRegistry.registerItem(itemArmorEnderLeggings,
				"armor_ender_leggings");
		itemArmorEnderChestplate = new ItemArmorEnderBase(enderChestplateID, 1);
		GameRegistry.registerItem(itemArmorEnderChestplate,
				"armor_ender_chestplate");
		itemArmorEnderHelmet = new ItemArmorEnderBase(enderHelmetID, 0);
		GameRegistry.registerItem(itemArmorEnderHelmet, "armor_ender_helmet");
		itemMaterial = new EAItemMaterial(materialID);
		GameRegistry.registerItem(itemMaterial, "item_material");
		itemWarpGem = new ItemWarpGem(itemWarpGemID);
		GameRegistry.registerItem(itemWarpGem, "tool_warp_gem");
		itemAnnihilationManipulator = new ItemAnnihilationManipulator(
				annihilationManipulatorID);
		GameRegistry.registerItem(itemAnnihilationManipulator,
				"tool_fastbreaker");
		itemHealingGem = new ItemHealingGem(healingGemID);
		GameRegistry.registerItem(itemHealingGem, "tool_healing_gem");
		itemEnderMagnet = new ItemEnderMagnet(magnetID);
		GameRegistry.registerItem(itemEnderMagnet, "tool_ender_magnet");
		EAInit.addRecipes();
		TickRegistry.registerTickHandler(new ITickHandler() {

			@Override
			public EnumSet<TickType> ticks() {
				return EnumSet.of(TickType.SERVER);
			}

			@Override
			public void tickStart(EnumSet<TickType> type, Object... tickData) {
				TickUtil.SERVER_TICKER++;
				TickUtil.harnessValues();
			}

			@Override
			public void tickEnd(EnumSet<TickType> type, Object... tickData) {

			}

			@Override
			public String getLabel() {
				return "EA Server Ticker";
			}
		}, Side.SERVER);
		// MinecraftForge.EVENT_BUS.register(new EAEventHandler());
	}
}
