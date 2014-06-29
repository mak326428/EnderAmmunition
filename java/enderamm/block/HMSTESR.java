package enderamm.block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class HMSTESR extends TileEntitySpecialRenderer {

    public ModelHMS model = new ModelHMS();

    public static final ResourceLocation HMS_TEXTURE = new ResourceLocation(
            "enderamm", "misc/textureHMS.png");

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y,
                                   double z, float f) {
        GL11.glPushMatrix();
        // This is setting the initial location.
        // GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z +
        // 0.5F);
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.9F, (float) z + 0.5F);
        GL11.glScalef(1.25F, 1.25F, 1.25F);
        // This is the texture of your block. It's pathed to be the same place
        // as your other blocks here.
        // BIND TEXTURE HERE
        FMLClientHandler.instance().getClient().renderEngine
                .bindTexture(HMS_TEXTURE);
        // This rotation part is very important! Without it, your model will
        // render upside-down! And for some reason you DO need PushMatrix again!
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        // A reference to your Model file. Again, very important.
        this.model
                .render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        // Tell it to stop rendering for both the PushMatrix's
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

}
