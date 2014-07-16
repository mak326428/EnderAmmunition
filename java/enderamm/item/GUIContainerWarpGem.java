package enderamm.item;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GUIContainerWarpGem extends GuiContainer {

    private static final ResourceLocation GUI_LOCATION = new ResourceLocation("enderamm", "gui/wgem.png");

    public boolean addButtonEnabled = false;
    public boolean removeButtonEnabled = false;
    public boolean teleportButtonEnabled = false;

    public Rectangle addButtonRectangle = new Rectangle(139, 40, 16, 16);
    public Rectangle removeButtonRectangle = new Rectangle(155, 40, 16, 16);
    public Rectangle teleportButtonRectangle = new Rectangle(145, 99, 20, 20);
    ItemWarpGem.WarpPoint curSel = null;

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
                updateButtonState();
                return;
            }
        }
        if (removeButtonEnabled) {
            if (removeButtonRectangle.contains(mp)) {
                ItemWarpGem.sendRemoveWarpPointRequest(nameTextBox.getText());
                updateButtonState();
                return;
            }
        }
        if (teleportButtonEnabled) {
            if (teleportButtonRectangle.contains(mp)) {
                ItemWarpGem.sendTeleportRequest(nameTextBox.getText());
                updateButtonState();
                return;
            }
        }
        // TODO: teleport EXACTLY HERE
        List<ItemWarpGem.WarpPoint> list = ItemWarpGem.getAllPoints(getStack());
        int xC = 10;
        int yC = 60;
        Rectangle area = new Rectangle(10, 60, 130, 100);
        if (area.contains(mp)) {
            int yReq = mp.y - yC;
            int itemID = yReq / fontRendererObj.FONT_HEIGHT;
            if (itemID < list.size()) {
                ItemWarpGem.WarpPoint wp = list.get(itemID);
                curSel = wp;
                this.nameTextBox.setText(wp.name);
            } else {
                curSel = null;
            }
        } else {
            curSel = null;
        }
        updateButtonState();
    }

    public ItemStack getStack() {
        return Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
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
        if (teleportButtonEnabled) {
            drawTexturedModalRect(xStartGUI + (int) teleportButtonRectangle.getX(), yStartGUI + (int) teleportButtonRectangle.getY(), 208, 192, teleportButtonRectangle.width, teleportButtonRectangle.height);
            if (teleportButtonRectangle.contains(mp))
                drawTexturedModalRect(xStartGUI + (int) teleportButtonRectangle.getX(), yStartGUI + (int) teleportButtonRectangle.getY(), 208, 212, teleportButtonRectangle.width, teleportButtonRectangle.height);
        }
        List<ItemWarpGem.WarpPoint> list = ItemWarpGem.getAllPoints(getStack());
        int xC = 10;
        int yC = 60;
        for (ItemWarpGem.WarpPoint wp : list) {
            fontRendererObj.drawString(wp.name, xStartGUI + xC, yStartGUI + yC, 0xFFFFFF);
            yC += fontRendererObj.FONT_HEIGHT;
        }
        this.nameTextBox.drawTextBox();
        if (curSel != null) {
            fontRendererObj.drawString("Dimension: " + DimensionManager.getProvider(curSel.dimID).getDimensionName(), xStartGUI + 7, yStartGUI + 7, 4210752);
            fontRendererObj.drawString("X: " + Math.round(curSel.x) + ", Y: " + Math.round(curSel.y) + ", Z: " + Math.round(curSel.z), xStartGUI + 7, yStartGUI + 17, 4210752);
        }
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    public void updateScreen() {
        this.nameTextBox.updateCursorCounter();
    }

    public void updateButtonState() {
        if (this.nameTextBox.getText().trim().equalsIgnoreCase("")) {
            this.addButtonEnabled = false;
            this.removeButtonEnabled = false;
            teleportButtonEnabled = false;
        } else {
            curSel = ItemWarpGem.getPointByName(getStack(), this.nameTextBox.getText());
            if (curSel == null) {
                this.addButtonEnabled = true;
                this.removeButtonEnabled = false;
                teleportButtonEnabled = false;
            } else {
                this.addButtonEnabled = false;
                this.removeButtonEnabled = true;
                teleportButtonEnabled = true;
            }
        }
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (this.nameTextBox.isFocused()) {
            this.nameTextBox.textboxKeyTyped(par1, par2);
            if (this.nameTextBox.getText().trim().equals("")) {
                this.nameTextBox.setText("");
            }
            updateButtonState();
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
        updateButtonState();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
