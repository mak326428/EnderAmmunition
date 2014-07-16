package enderamm.item;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GUIContainerWarpGem extends GuiContainer {

    private static final ResourceLocation GUI_LOCATION = new ResourceLocation("enderamm", "gui/wgem.png");

    public static class ContainerWarpGem extends Container {
        public ContainerWarpGem() {}

        @Override
        public boolean canInteractWith(EntityPlayer var1) {
            return true;
        }
    }

    public GUIContainerWarpGem() {
        super(new ContainerWarpGem());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(GUI_LOCATION);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
