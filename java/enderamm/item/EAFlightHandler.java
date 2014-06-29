package enderamm.item;

import java.util.Map;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import enderamm.network.PacketFlightStatusUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.google.common.collect.Maps;

public class EAFlightHandler {

	public static Map<EntityPlayer, Boolean> modEnabledFlights = Maps
			.newHashMap();

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (!(event.entityLiving instanceof EntityPlayer))
			return;
		World w = event.entityLiving.worldObj;
		if (w.isRemote)
			return;
		doChecks((EntityPlayer) event.entityLiving, w);
	}

	public void doChecks(EntityPlayer ep, World w) {
		try {
			if (!modEnabledFlights.containsKey(ep))
				return;
			boolean shouldFly = modEnabledFlights.get(ep);
			if (!shouldFly && ep.capabilities.allowFlying) {
				ep.capabilities.isFlying = false;
				ep.capabilities.allowFlying = false;
				//PacketFlightUpdate pfu = new PacketFlightUpdate();
				//pfu.allowFlying = false;
				//pfu.isFlying = false;
				//PacketDispatcher.sendPacketToPlayer(
				//		PacketTypeHandler.populatePacket(pfu), (Player) ep);
				PacketFlightStatusUpdate.issue(ep, false, false);
				modEnabledFlights.remove(ep);
			} else {
				boolean foundFlight = false;
				ItemStack[] inventory = ep.inventory.mainInventory;
				ItemStack[] armor = ep.inventory.armorInventory;
				for (ItemStack is : armor) {
					if (is == null)
						continue;
					if (is.getItem() instanceof ItemArmorEnderBase)
						if (((ItemArmorEnderBase) is.getItem()).allowsFlight(is, ep))
							foundFlight = true;
				}
				if (!foundFlight) {
					removePlayerFlight(ep);
				}
			}
		} catch (Exception e) {
			// LogHelper
			// .severe("Fatal error happened in FlightHandler.doChecks method, report this!");
			e.printStackTrace();
		}
	}

	public static void removePlayerFlight(EntityPlayer ep) {
		if (!modEnabledFlights.containsKey(ep)) {
			modEnabledFlights.put(ep, false);
		} else {
			modEnabledFlights.remove(ep);
			modEnabledFlights.put(ep, false);
		}
	}

	public static void allowFlight(EntityPlayer ep) {
		if (!modEnabledFlights.containsKey(ep)) {
			modEnabledFlights.put(ep, true);
		} else {
			modEnabledFlights.remove(ep);
			modEnabledFlights.put(ep, true);
		}
		ep.capabilities.allowFlying = true;
	}
}
