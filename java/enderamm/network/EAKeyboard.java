package enderamm.network;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.FMLCommonHandler;
import enderamm.EAClientProxy;
import enderamm.EnderAmmunition;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 29.06.14
 * Time: 19:21
 * To change this template use File | Settings | File Templates.
 */
public class EAKeyboard {
    public static Map<EntityPlayer, Boolean> playersCtrl = Maps.newHashMap();
    public static Map<EntityPlayer, Boolean> playersSpace = Maps.newHashMap();
    public static Map<EntityPlayer, Boolean> playersFire = Maps.newHashMap();

    public static boolean isPressingSpace(EntityPlayer player) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            if (!playersSpace.containsKey(player))
                playersSpace.put(player, false);
            return playersSpace.get(player);
        } else {
            return ((EAClientProxy) EnderAmmunition.PROXY).lastSpace;
        }
    }

    public static boolean isPressingFire(EntityPlayer player) {
        if (!playersFire.containsKey(player))
            playersFire.put(player, false);
        return playersFire.get(player);
    }

    public static boolean isPressingCtrl(EntityPlayer player) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            if (!playersCtrl.containsKey(player))
                playersCtrl.put(player, false);
            return playersCtrl.get(player);
        } else {
            return ((EAClientProxy) EnderAmmunition.PROXY).lastCtrl;
        }
    }

}
