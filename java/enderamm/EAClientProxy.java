package enderamm;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;
import enderamm.block.HMSTESR;
import enderamm.block.ItemRendererHMS;
import enderamm.block.TileEntityHMS;
import enderamm.item.ItemArmorEnderBase;
import enderamm.network.PacketKeyUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.lwjgl.input.Keyboard;

public class EAClientProxy extends EACommonProxy {

    public static final ResourceLocation FIRE_TEXTURE = new ResourceLocation(
            "enderamm", "misc/fire.png");
    public static KeyBinding accelerationBinding = new KeyBinding(
            "[EA] Accelerate", Keyboard.KEY_LCONTROL, "EnderAmmunition");
    public static KeyBinding fireBinding = new KeyBinding(
            "[EA] Shoot Fire Ray", Keyboard.KEY_R, "EnderAmmunition");

    public KeyBinding[] allBindings = {accelerationBinding, fireBinding};

    public boolean lastSpace, lastCtrl, lastFire;

    @Override
    public void preInit(Configuration config) {
        ItemArmorEnderBase.RENDER_INDEX = RenderingRegistry
                .addNewArmourRendererPrefix("rfender");
        super.preInit(config);
    }

    @Override
    public void init() {
        super.init();
        String[] types = new String[]{"Helmet", "Chestplate", "Leggings",
                "Boots"};
        for (int i = 0; i < types.length; i++)
            LanguageRegistry.instance().addStringLocalization(
                    "item.armorEnder" + i + ".name",
                    String.format("Ender-Infused %s", types[i]));

        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        for (KeyBinding binding : allBindings)
            ClientRegistry.registerKeyBinding(binding);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHMS.class,
                new HMSTESR());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(
                EACommonProxy.blockHMS), new ItemRendererHMS());
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent renderTickEvent) {
        if (renderTickEvent.phase == TickEvent.Phase.END) {
            TickUtil.RENDER_TICKER++;
            TickUtil.onRenderTick();
        }
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.END) {
            boolean isSpacePressed = Minecraft.getMinecraft().gameSettings.keyBindJump.getIsKeyPressed();
            boolean isFirePressed = fireBinding.getIsKeyPressed();
            boolean isCtrlPressed = accelerationBinding.getIsKeyPressed();
            if (lastSpace != isSpacePressed || lastCtrl != isCtrlPressed
                    || lastFire != isFirePressed) {
                lastSpace = isSpacePressed;
                lastCtrl = isCtrlPressed;
                lastFire = isFirePressed;
                //EAPacketHandler_old.sendKeyUpdatePacket(isCtrlPressed,
                //        isSpacePressed, isFirePressed);
                PacketKeyUpdate.issue(isCtrlPressed, isSpacePressed, isFirePressed);
            }
            TickUtil.CLIENT_TICKER++;
        }
    }

}
