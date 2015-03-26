package enderamm.item;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enderamm.ColorRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
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

    public static class ContainerWarpGem extends Container {
        public ContainerWarpGem() {
        }

        @Override
        public boolean canInteractWith(EntityPlayer var1) {
            return true;
        }

        @Override
        public Slot getSlot(int p_75139_1_) {
            return new Slot(new IInventory() {
                @Override
                public int getSizeInventory() {
                    return 0;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public ItemStack getStackInSlot(int p_70301_1_) {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public String getInventoryName() {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public boolean hasCustomInventoryName() {
                    return false;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public int getInventoryStackLimit() {
                    return 0;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void markDirty() {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
                    return false;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void openInventory() {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void closeInventory() {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
                    return false;  //To change body of implemented methods use File | Settings | File Templates.
                }
            }, 0, 0, 0);
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
        addButton.mouseClicked(mp.x, mp.y);
        removeButton.mouseClicked(mp.x, mp.y);
        // int xZ = xStartGUI + 8 + 2;
        // int yZ = yStartGUI + 22 + 2;
        // yZ += 10;
        if (mp.getX() > 8 && mp.getX() < 8 + 128) {
            int yR = mp.y - (22 + 2);
            int chosen = yR / 10;
            if (chosen <= 14) {
                List<ItemWarpGem.WarpPoint> wpList = ItemWarpGem.getAllPoints(getStack());
                if (chosen >= 0 && chosen < wpList.size()) {
                    ItemWarpGem.WarpPoint tW = wpList.get(chosen);
                    if (selected != null && selected.name.equals(tW.name)) {
                        // Teleport
                        ItemWarpGem.sendTeleportRequest(tW.name);
                    } else {
                        selected = tW;
                        selID = chosen;
                        nameTextBox.setText(tW.name);
                    }
                }
            }
        }
        //updateButtonState();
    }

    public ItemWarpGem.WarpPoint selected = null;
    public int selID = -2;

    public class ButtonTextured {
        public int x;
        public int y;
        public int u;
        public int v;
        public int sizeX;
        public int sizeY;
        public GUIContainerWarpGem gui;
        public Runnable onClick;

        public ButtonTextured(int x, int y, int u, int v, int sizeX, int sizeY, GUIContainerWarpGem gui, Runnable onClick) {
            this.x = x;
            this.y = y;
            this.u = u;
            this.v = v;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.gui = gui;
            this.onClick = onClick;
        }

        public void drawButton(int xM, int yM) {
            int xStartGUI = (gui.width - gui.xSize) / 2;
            int yStartGUI = (gui.height - gui.ySize) / 2;
            Rectangle rect = new Rectangle(x, y, sizeX, sizeY);
            Point p = new Point(xM, yM);
            gui.drawTexturedModalRect(xStartGUI + x, yStartGUI + y, u, v, sizeX, sizeY);
            //System.out.println(rect + " " + p + " " + rect.contains(p));
            if (rect.contains(p))
                gui.drawTexturedModalRect(xStartGUI + x, yStartGUI + y, u, v + sizeY, sizeX, sizeY);
        }

        public void mouseClicked(int xM, int yM) {
            Rectangle rect = new Rectangle(x, y, sizeX, sizeY);
            Point p = new Point(xM, yM);
            if (rect.contains(p)) {
                // TODO: play sound too
                onClick.run();
            }
        }
    }

    public ItemStack getStack() {
        return Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
    }

    int lastHighlighted;

    @Override
    public void drawScreen(int x, int y, float par3) {

        boolean dtpp = true;
        super.drawScreen(x, y, par3);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int xStartGUI = (this.width - this.xSize) / 2;
        int yStartGUI = (this.height - this.ySize) / 2;
        Point mp = new Point(x - xStartGUI, y - yStartGUI);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(GUI_LOCATION);
        List<ItemWarpGem.WarpPoint> list = ItemWarpGem.getAllPoints(getStack());

        int highlighted = getSelectedID(mp);
        int required = (int)Math.floor(ColorRegistry.FPS / 2);
        if (highlighted != -1 && highlighted != selID) {
            double d = ((tick % required + 1) / (double)required);
            if (d == 1)
                dtpp = false;
            drawOverlay(highlighted, 181, d);
        }
        if (lastHighlighted != highlighted) {
            tick = 0;
            dtpp = true;
        }
        lastHighlighted = highlighted;
        if (selID > 0) {
            drawOverlay(selID, 191, 1.0D);
        }
        addButton.drawButton(mp.x, mp.y);
        removeButton.drawButton(mp.x, mp.y);
        nameTextBox.drawTextBox();
        int xZ = xStartGUI + 8 + 2;
        int yZ = yStartGUI + 22 + 2;
        for (ItemWarpGem.WarpPoint wp : list) {
            int dimid = Minecraft.getMinecraft().theWorld.provider.dimensionId;
            String s = EnumChatFormatting.WHITE + wp.name + " " + (dimid == wp.dimID ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED) + "[" + dimid + "]";
            fontRendererObj.drawString(s, xZ, yZ, 0xFFFFFF);
            yZ += 10;
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        if (dtpp)
            tick++;
    }

    long tick;

    public int getSelectedID(Point mp) {
        if (mp.getX() > 8 && mp.getX() < 8 + 128) {
            int yR = mp.y - (22 + 2);
            int chosen = yR / 10;
            if (chosen <= 14) {
                List<ItemWarpGem.WarpPoint> wpList = ItemWarpGem.getAllPoints(getStack());
                if (chosen >= 0 && chosen < wpList.size()) {
                    return chosen;
                }
            }
        }
        return -1;
    }

    public void drawOverlay(int id, int v, double u) {
        int xStartGUI = (this.width - this.xSize) / 2;
        int yStartGUI = (this.height - this.ySize) / 2;
        int xZ = xStartGUI + 8;
        int yZ = yStartGUI + 22 + id * 10;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        drawTexturedModalRect(xZ, yZ, 0, v, (int)Math.ceil(u * 162), 10);
        GL11.glDisable(GL11.GL_BLEND);
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
            }
            //updateButtonState();
        }
        if (par2 == 1) {
            this.mc.thePlayer.closeScreen();
        }
    }

    ButtonTextured removeButton;
    ButtonTextured addButton;

    @Override
    public void initGui() {
        super.initGui();
        int xGuiPos = (this.width - this.xSize) / 2; // j
        int yGuiPos = (this.height - this.ySize) / 2;
        this.nameTextBox = new GuiTextField(this.fontRendererObj, xGuiPos + 8,
                yGuiPos + 6, 128, 12);
        Keyboard.enableRepeatEvents(true);
        this.nameTextBox.setMaxStringLength(16);
        this.nameTextBox.setEnableBackgroundDrawing(false);
        this.nameTextBox.setVisible(true);
        this.nameTextBox.setFocused(true);
        this.nameTextBox.setEnableBackgroundDrawing(true);
        this.nameTextBox.setText("");
        this.nameTextBox.setCanLoseFocus(false);
        addButton = new ButtonTextured(140 - 1, 5 - 1, 208, 128, 16, 16, this, new Runnable() {
            @Override
            public void run() {
                onAddClicked();
            }
        });

        removeButton = new ButtonTextured(140 + 16 - 1, 5 - 1, 208 + 16, 128, 16, 16, this, new Runnable() {
            @Override
            public void run() {
                onRemoveClicked();
            }
        });
    }

    public void onAddClicked() {
        //if (ItemWarpGem.getPointByName(nameTextBox.getText()) != null)
        ItemWarpGem.sendAddWaypointPacket(nameTextBox.getText());
    }

    public void onRemoveClicked() {
        ItemWarpGem.sendRemoveWarpPointRequest(nameTextBox.getText());
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
        //updateButtonState();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
