package enderamm.item;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public class GUIContainerWarpGem extends GuiContainer {

    private static final ResourceLocation GUI_LOCATION = new ResourceLocation("enderamm", "gui/wgem.png");

    public boolean addButtonEnabled = false;
    public boolean removeButtonEnabled = false;
    public boolean teleportButtonEnabled = false;

    public Rectangle addButtonRectangle = new Rectangle(139, 40, 16, 16);
    public Rectangle removeButtonRectangle = new Rectangle(155, 40, 16, 16);
    public Rectangle teleportButtonRectangle = new Rectangle(145, 99, 20, 20);


    public static class ContainerWarpGem extends Container {
        public ContainerWarpGem() {
        }

        @Override
        public boolean canInteractWith(EntityPlayer var1) {
            return true;
        }
    }

    public GUIContainerWarpGem() {
        super(new ContainerWarpGem());
    }

    protected void mouseClicked(int x, int y, int par3) {
        super.mouseClicked(x, y, par3);
        int xStartGUI = (this.width - this.xSize) / 2;
        int yStartGUI = (this.height - this.ySize) / 2;
        Point mp = new Point(x - xStartGUI, y - yStartGUI);
        if (addButtonEnabled) {
            if (addButtonRectangle.contains(mp)) {
                ItemWarpGem.sendAddWaypointPacket(nameTextBox.getText());
            }
        }
        if (removeButtonEnabled) {
            if (removeButtonRectangle.contains(mp)) {
                ItemWarpGem.sendRemoveWarpPointRequest(nameTextBox.getText());
            }
        }
        List<ItemWarpGem.WarpPoint> list = ItemWarpGem.getAllPoints(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
        int xC = 8;
        int yC = 58;
        Rectangle area = new Rectangle(8, 58, 128, 102);
        if (area.contains(mp)) {
            int yReq = mp.y - yC;
            int itemID = yReq / 10;
            this.nameTextBox.setText(list.get(itemID).name);
        }
        // TODO: teleport
    }

    @Override
    public void drawScreen(int x, int y, float par3) {
        super.drawScreen(x, y, par3);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int xStartGUI = (this.width - this.xSize) / 2;
        int yStartGUI = (this.height - this.ySize) / 2;
        Point mp = new Point(x - xStartGUI, y - yStartGUI);

        if (addButtonEnabled) {
            drawTexturedModalRect(xStartGUI + (int) addButtonRectangle.getX(), yStartGUI + (int) addButtonRectangle.getY(), 208, 128, addButtonRectangle.width, addButtonRectangle.height);
            if (addButtonRectangle.contains(mp))
                drawTexturedModalRect(xStartGUI + (int) addButtonRectangle.getX(), yStartGUI + (int) addButtonRectangle.getY(), 208, 144, addButtonRectangle.width, addButtonRectangle.height);
        }
        if (removeButtonEnabled) {
            drawTexturedModalRect(xStartGUI + (int) removeButtonRectangle.getX(), yStartGUI + (int) removeButtonRectangle.getY(), 224, 128, removeButtonRectangle.width, removeButtonRectangle.height);
            if (removeButtonRectangle.contains(mp))
                drawTexturedModalRect(xStartGUI + (int) removeButtonRectangle.getX(), yStartGUI + (int) removeButtonRectangle.getY(), 224, 144, removeButtonRectangle.width, removeButtonRectangle.height);
        }
        List<ItemWarpGem.WarpPoint> list = ItemWarpGem.getAllPoints(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
        int xC = 8;
        int yC = 58;
        for (ItemWarpGem.WarpPoint wp : list) {
            fontRendererObj.drawString(wp.name, xStartGUI + xC, yStartGUI + yC, 0xFFFFFF);
            yC += fontRendererObj.FONT_HEIGHT;
        }
        this.nameTextBox.drawTextBox();
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    public void updateScreen() {
        this.nameTextBox.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (this.nameTextBox.isFocused()) {
            this.nameTextBox.textboxKeyTyped(par1, par2);
            if (this.nameTextBox.getText().trim().equals("")) {
                this.nameTextBox.setText("");
                addButtonEnabled = false;
                removeButtonEnabled = false;
            } else {
                addButtonEnabled = true;
                removeButtonEnabled = true;
            }
        }
        if (par2 == 1) {
            this.mc.thePlayer.closeScreen();
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        int xGuiPos = (this.width - this.xSize) / 2; // j
        int yGuiPos = (this.height - this.ySize) / 2;
        this.nameTextBox = new GuiTextField(this.fontRendererObj, xGuiPos + 8,
                yGuiPos + 42, 128, 12);
        Keyboard.enableRepeatEvents(true);
        this.nameTextBox.setMaxStringLength(16);
        this.nameTextBox.setEnableBackgroundDrawing(false);
        this.nameTextBox.setVisible(true);
        this.nameTextBox.setFocused(true);
        this.nameTextBox.setEnableBackgroundDrawing(true);
        this.nameTextBox.setText("");
        this.nameTextBox.setCanLoseFocus(false);

    }

    public GuiTextField nameTextBox;

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
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
