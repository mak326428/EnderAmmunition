package enderamm;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Configuration;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import enderamm.item.ItemArmorEnderBase;

public class EAClientProxy extends EACommonProxy implements ITickHandler {

	public static final ResourceLocation FIRE_TEXTURE = new ResourceLocation(
			"enderamm", "misc/fire.png");
	public static KeyBinding accelerationBinding = new KeyBinding(
			"[EA] Accelerate", Keyboard.KEY_LCONTROL);
	public static KeyBinding fireBinding = new KeyBinding(
			"[EA] Shoot Fire Ray", Keyboard.KEY_R);

	public KeyBinding[] allBindings = { accelerationBinding, fireBinding };

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
		String[] types = new String[] { "Helmet", "Chestplate", "Leggings",
				"Boots" };
		for (int i = 0; i < types.length; i++)
			LanguageRegistry.instance().addStringLocalization(
					"item.armorEnder" + i + ".name",
					String.format("Ender-Infused %s", types[i]));
		TickRegistry.registerTickHandler(this, Side.CLIENT);
		KeyBindingRegistry.registerKeyBinding(new KeyHandler(allBindings) {

			@Override
			public String getLabel() {
				return "EAKeyHandler";
			}

			@Override
			public EnumSet<TickType> ticks() {
				return EnumSet.of(TickType.CLIENT);
			}

			@Override
			public void keyUp(EnumSet<TickType> types, KeyBinding kb,
					boolean tickEnd) {
			}

			@Override
			public void keyDown(EnumSet<TickType> types, KeyBinding kb,
					boolean tickEnd, boolean isRepeat) {
			}
		});
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		// boolean isCtrlPressed = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		if (type.contains(TickType.CLIENT)) {
			boolean isSpacePressed = Minecraft.getMinecraft().gameSettings.keyBindJump.pressed;
			boolean isFirePressed = fireBinding.pressed;
			boolean isCtrlPressed = accelerationBinding.pressed;
			if (lastSpace != isSpacePressed || lastCtrl != isCtrlPressed
					|| lastFire != isFirePressed) {
				lastSpace = isSpacePressed;
				lastCtrl = isCtrlPressed;
				lastFire = isFirePressed;
				EAPacketHandler.sendKeyUpdatePacket(isCtrlPressed,
						isSpacePressed, isFirePressed);
			}
			TickUtil.CLIENT_TICKER++;
		} else {
			TickUtil.RENDER_TICKER++;
			TickUtil.onRenderTick();
		}
		TickUtil.harnessValues();
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "EAClientProxy";
	}

}
