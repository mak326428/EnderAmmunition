package enderamm.block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.client.model.techne.TechneModel;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RockExterminatorTESR extends TileEntitySpecialRenderer {
    public IModelCustom model;

    public RockExterminatorTESR() {
        model = new WavefrontObject(new ResourceLocation("enderamm", "misc/rock_exterminator4.obj"));
    }

    public static final ResourceLocation CORE_TEXTURE = new ResourceLocation(
            "enderamm", "misc/fire.png");
    public static final ResourceLocation WALLS_TEXTURE = new ResourceLocation(
            "enderamm", "misc/wall.png");


    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y,
                                   double z, float f) {
        GL11.glPushMatrix();
        // This is setting the initial location.
        // GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z +
        // 0.5F);
        GL11.glTranslatef((float) x + 1F, (float) y + 1F, (float) z + 1F);
        GL11.glScalef(10F, 10F, 10F);
        // This is the texture of your block. It's pathed to be the same place
        // as your other blocks here.
        // BIND TEXTURE HERE
        GL11.glPushMatrix();
        // This rotation part is very important! Without it, your model will
        // render upside-down! And for some reason you DO need PushMatrix again!
        FMLClientHandler.instance().getClient().renderEngine
                .bindTexture(CORE_TEXTURE);
        {
            GL11.glEnable(GL11.GL_BLEND);
            {
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                model.renderPart("Sphere01");
            }
            GL11.glDisable(GL11.GL_BLEND);
        }

       /* FMLClientHandler.instance().getClient().renderEngine
                .bindTexture(WALLS_TEXTURE);
        {
            GL11.glEnable(GL11.GL_BLEND);
            {
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                model.renderAllExcept("Sphere01");
            }
            GL11.glDisable(GL11.GL_BLEND);
        }*/



        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}
