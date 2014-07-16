package enderamm.item;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
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
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        int xGuiPos = (this.width - this.xSize) / 2; // j
        int yGuiPos = (this.height - this.ySize) / 2;
        this.nameTextBox.drawTextBox();
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


        }
        super.keyTyped(par1, par2);
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
