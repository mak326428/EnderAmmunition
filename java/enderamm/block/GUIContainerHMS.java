package enderamm.block;

import com.google.common.collect.Lists;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.EARenderHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GUIContainerHMS extends GuiContainer {

    public TileEntityHMS tileEntity;
    public static ResourceLocation GUI_LOCATION = new ResourceLocation("enderamm", "gui/hms.png");

    public GUIContainerHMS(InventoryPlayer inventoryPlayer,
                           TileEntityHMS tileEntity) {
        super(new ContainerHMS(inventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        int xGuiPos = (this.width - this.xSize) / 2; // j
        int yGuiPos = (this.height - this.ySize) / 2;
        //this.fontRenderer.drawString(
        //        StatCollector.translateToLocal("container.inventory"), 8,
        //        this.ySize - 96 + 2, 4210752);
    }

    @Override
    public void drawScreen(int x, int y, float par3) {
        super.drawScreen(x, y, par3);
        //System.out.println("X: " + x + "; Y: "+ y);
        int xStartGUI = (this.width - this.xSize) / 2;
        int yStartGUI = (this.height - this.ySize) / 2;
        Point mp = new Point(x - xStartGUI, y - yStartGUI);
        // Checks and tooltips
        {
            Rectangle energyBar = new Rectangle(6, 22, 14, 42);
            if (energyBar.contains(mp)) {
                EARenderHelper.renderTooltip(x, y, Arrays.asList(String.format("%d / %d RF", this.tileEntity.getEnergyStored(ForgeDirection.UNKNOWN), this.tileEntity.getMaxEnergyStored(ForgeDirection.UNKNOWN))));
            }
        }
        {
            FluidTank tank = tileEntity.redstoneTank;
            Rectangle bar = new Rectangle(23, 9, 18, 60);
            if (bar.contains(mp)) {
                List<String> tooltip = Lists.newArrayList();
                tooltip.add(
                        StatCollector.translateToLocal("Destabilized Redstone"));
                tooltip.add(String.format("%d / %d mB", tank.getFluidAmount(),
                        tank.getCapacity()));
                EARenderHelper.renderTooltip(x, y, tooltip);
            }
        }
        {
            FluidTank tank = tileEntity.glowstoneTank;
            Rectangle bar = new Rectangle(43, 9, 18, 60);
            if (bar.contains(mp)) {
                List<String> tooltip = Lists.newArrayList();
                tooltip.add(
                        StatCollector.translateToLocal("Energized Glowstone"));
                tooltip.add(String.format("%d / %d mB", tank.getFluidAmount(),
                        tank.getCapacity()));
                EARenderHelper.renderTooltip(x, y, tooltip);
            }
        }
        {
            FluidTank tank = tileEntity.enderTank;
            Rectangle bar = new Rectangle(63, 9, 18, 60);
            if (bar.contains(mp)) {
                List<String> tooltip = Lists.newArrayList();
                tooltip.add("Resonant Ender");
                tooltip.add(String.format("%d / %d mB", tank.getFluidAmount(),
                        tank.getCapacity()));
                EARenderHelper.renderTooltip(x, y, tooltip);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                                                   int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(GUI_LOCATION);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(GUI_LOCATION);
        EARenderHelper.renderTank(tileEntity.redstoneTank, x, y, 24, 10);
        EARenderHelper.renderTank(tileEntity.glowstoneTank, x, y, 44, 10);
        EARenderHelper.renderTank(tileEntity.enderTank, x, y, 64, 10);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(GUI_LOCATION);
        int energyGauged = Math.round((float) tileEntity.getEnergyStored(ForgeDirection.UNKNOWN) / (float) tileEntity.getMaxEnergyStored(ForgeDirection.UNKNOWN) * (float) 42);
        this.drawTexturedModalRect(x + 6, y + 22 + 42 - energyGauged, 176, 80 + 42 - energyGauged,
                14, energyGauged);
        int progress = tileEntity.progressGauged;
        this.drawTexturedModalRect(x + 92, y + 29, 226, 40, progress, 15);
        this.drawTexturedModalRect(x + 118, y + 50 + 22 - progress, 226, 10 + 22 - progress,
                22, progress);
        this.drawTexturedModalRect(x + 138 + 22 - progress, y + 29, 226 + 22 - progress, 60, progress, 15);
    }
}
