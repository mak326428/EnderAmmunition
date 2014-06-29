package enderamm.block;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ItemRendererHMS implements IItemRenderer {

	public ModelHMS model = new ModelHMS();

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {

		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {

		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
		// item laying on the ground
		case ENTITY: {
			this.renderHMS(-0.5F, -1.2F, 0.5F, 1.4F);
			return;
		}
		case INVENTORY: {
			this.renderHMS(0.06F, -1.08F, 0F, 1.2F);
			return;
		}
		default:
			return;
		}
	}
	
	public static final ResourceLocation HMS_TEXTURE = new ResourceLocation(
			"enderamm", "misc/textureHMSItem.png");

	private void renderHMS(float x, float y, float z, float scale) {

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);

		// Scale, Translate, Rotate
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslatef(x, y, z);
		// GL11.glRotatef(-90F, 1F, 0, 0);

		// Bind texture
		FMLClientHandler.instance().getClient().renderEngine
				.bindTexture(HMS_TEXTURE);

		// Render
		this.model
				.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
