package enderamm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import enderamm.block.ContainerHMS;
import enderamm.block.GUIContainerHMS;
import enderamm.block.TileEntityHMS;

public class EAGUIHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityHMS)
			return new ContainerHMS(player.inventory, (TileEntityHMS)te);
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityHMS)
			return new GUIContainerHMS(player.inventory, (TileEntityHMS)te);
		return null;
	}

}
