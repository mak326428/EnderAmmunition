package enderamm;

import enderamm.network.EAPacketHandler;
import enderamm.network.PacketRenderDebug;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 15.07.14
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public class CommandDebugRender extends CommandBase {

    @Override
    public String getCommandName() {
        return "ea-debug-render";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "/" + getCommandName() + " [translateX] [translateY] [translateZ] [scaleX] [scaleY] [scaleZ] [rotateX] [rotateY] [rotateZ]";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        if (astring.length % 3 != 0) throw new WrongUsageException("Wrong amount of arguments");
        double translateX = 0.0D, translateY = 0.0D, translateZ = 0.0D, scaleX = 0.0D, scaleY = 0.0D, scaleZ = 0.0D, rotateX = 0.0D, rotateY = 0.0D, rotateZ = 0.0D;
        try {
            if (astring.length >= 1) {
                translateX = Double.parseDouble(astring[0]);
                translateY = Double.parseDouble(astring[1]);
                translateZ = Double.parseDouble(astring[2]);

            }
            if (astring.length >= 4) {
                scaleX = Double.parseDouble(astring[3]);
                scaleY = Double.parseDouble(astring[4]);
                scaleZ = Double.parseDouble(astring[5]);
            }
            if (astring.length >= 5) {
                rotateX = Double.parseDouble(astring[6]);
                rotateY = Double.parseDouble(astring[7]);
                rotateZ = Double.parseDouble(astring[8]);
            }
            EntityPlayer ep = getCommandSenderAsPlayer(icommandsender);
            System.out.println(ep);
            PacketRenderDebug prd = new PacketRenderDebug();
            prd.translateX = translateX;
            prd.translateY = translateY;
            prd.translateZ = translateZ;
            prd.scaleX = scaleX;
            prd.scaleY = scaleY;
            prd.scaleZ = scaleZ;
            prd.rotateX = rotateX;
            prd.rotateY = rotateY;
            prd.rotateZ = rotateZ;
            EAPacketHandler.sendToPlayer(ep, prd);

        } catch (Throwable t) {
            throw new WrongUsageException(String.format("Whoops something went wrong, probably incorrect arguments. (%s)", t.getClass().getCanonicalName()));
        }
    }
}
