package enderamm.item;

import cpw.mods.fml.client.FMLClientHandler;
import enderamm.DebugReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 15.07.14
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class ItemRendererWarpGem implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    public IModelCustom model;

    public ItemRendererWarpGem() {
        model = new WavefrontObject(new ResourceLocation("enderamm", "misc/wgem2.obj"));
    }

    public static final ResourceLocation OUTER_SPHERE = new ResourceLocation(
            "enderamm", "misc/wgemOuterSphere.png");
    public static final ResourceLocation INNER_SPHERE = new ResourceLocation(
            "enderamm", "misc/wgemInnerSphere.png");


    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        // This is setting the initial location.
        // GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z +
        // 0.5F);
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0.0F + 7.5F + 0.25F, 1.0F - 0.66F, 0.0F + 1.0F);
            GL11.glScalef(3.0F, 3.0F, 3.0F);
        } else {
            GL11.glTranslatef(0.0F + 1F, 0.0F + 1F, 0.0F + 1F);
            GL11.glScalef(1F, 1F, 1F);
        }
        // TODO: ItemRenderType.EQUIPPED
        // This is the texture of your block. It's pathed to be the same place
        // as your other blocks here.
        // BIND TEXTURE HERE
        GL11.glPushMatrix();
        // This rotation part is very important! Without it, your model will
        // render upside-down! And for some reason you DO need PushMatrix again!
        FMLClientHandler.instance().getClient().renderEngine
                .bindTexture(INNER_SPHERE);
        int gemColor = WarpGemEffectHelper.gemColor;
        float r = WarpGemEffectHelper.getR(gemColor) / 256.0F;
        float g = WarpGemEffectHelper.getG(gemColor) / 256.0F;
        float b = WarpGemEffectHelper.getB(gemColor) / 256.0F;
        GL11.glColor3f(r, g, b);
        {
            GL11.glEnable(GL11.GL_BLEND);
            {
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                model.renderPart("InnerSphere");
            }
            GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        FMLClientHandler.instance().getClient().renderEngine
                .bindTexture(OUTER_SPHERE);
        {
            GL11.glEnable(GL11.GL_BLEND);
            {
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                model.renderPart("OuterSphere");
            }
            GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glPopMatrix();
        GL11.glPopMatrix();

    }
}
