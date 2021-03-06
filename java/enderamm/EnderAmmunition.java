package enderamm;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = EnderAmmunition.MOD_ID, name = EnderAmmunition.MOD_NAME, version = EnderAmmunition.VERSION, dependencies="required-after:ThermalExpansion")
public class EnderAmmunition {
    public static final String MOD_ID = "EnderAmmunition";
    public static final String MOD_NAME = "EnderAmmunition";
    public static final String VERSION = "1.0alpha";
    @Instance(EnderAmmunition.MOD_ID)
    public static EnderAmmunition instance;

    @SidedProxy(clientSide = "enderamm.EAClientProxy", serverSide = "enderamm.EACommonProxy")
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

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        ((ServerCommandManager) MinecraftServer.getServer().getCommandManager())
                .registerCommand(new CommandDebugRender());
    }

}
