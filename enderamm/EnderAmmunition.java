package enderamm;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = EnderAmmunition.MOD_ID, name = EnderAmmunition.MOD_NAME, version = EnderAmmunition.VERSION, dependencies = "required-after:IC2@[2.0,)")
@NetworkMod(channels = { EnderAmmunition.MOD_ID }, clientSideRequired = true, serverSideRequired = false, packetHandler=EAPacketHandler.class)
public class EnderAmmunition {
	public static final String MOD_ID = "EnderAmmunition";
	public static final String MOD_NAME = "EnderAmmunition";
	public static final String VERSION = "0.8b";
	@Instance(EnderAmmunition.MOD_ID)
	public static EnderAmmunition instance;
	
	@SidedProxy(clientSide="enderamm.EAClientProxy", serverSide="enderamm.EACommonProxy")
	public static EACommonProxy PROXY;
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		PROXY.init();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		PROXY.preInit(config);
		config.save();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit();
	}
	
}
