package enderamm.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 10.07.14
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
@SideOnly(Side.CLIENT)
public class ItemRendererRockExterminator implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
        new RockExterminatorTESR().renderTileEntityAt(new TileEntityRockExterminator(), 0.0D, 0.0D, 0.0D, 0.0F);
    }
}
