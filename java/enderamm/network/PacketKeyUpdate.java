package enderamm.network;

import enderamm.item.ItemArmorEnderBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 29.06.14
 * Time: 19:06
 * To change this template use File | Settings | File Templates.
 */
// Client -> Server
public class PacketKeyUpdate extends IPacket {

    public boolean boost, space, fire;

    public static void issue(boolean boost, boolean space, boolean fire) {
        PacketKeyUpdate pku = new PacketKeyUpdate();
        pku.boost = boost;
        pku.space = space;
        pku.fire = fire;
        EAPacketHandler.sendToServer(pku);
    }

    @Override
    public void read(DataInputStream bytes) throws Throwable {
        boost = bytes.readBoolean();
        space = bytes.readBoolean();
        fire = bytes.readBoolean();
    }

    @Override
    public void write(DataOutputStream bytes) throws Throwable {
        bytes.writeBoolean(boost);
        bytes.writeBoolean(space);
        bytes.writeBoolean(fire);
    }

    @Override
    public void execute(EntityPlayerMP player) {
        if (EAKeyboard.isPressingFire(player) && !fire) {
            ItemStack is = player.inventory.armorInventory[0];
            if (is != null && is.getItem() instanceof ItemArmorEnderBase && ((ItemArmor) is.getItem()).armorType == 0) {
                ((ItemArmorEnderBase) is.getItem()).doFireRay(player.worldObj, player, is);
            }
        }
        if (EAKeyboard.playersCtrl.containsKey(player))
            EAKeyboard.playersCtrl.remove(player);
        if (EAKeyboard.playersSpace.containsKey(player))
            EAKeyboard.playersSpace.remove(player);
        if (EAKeyboard.playersFire.containsKey(player))
            EAKeyboard.playersFire.remove(player);
        EAKeyboard.playersCtrl.put(player, boost);
        EAKeyboard.playersSpace.put(player, space);
        EAKeyboard.playersFire.put(player, fire);
    }


}
