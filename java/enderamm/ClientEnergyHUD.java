package enderamm;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class ClientEnergyHUD {

    public static class HUDLine {
        public ItemStack displayItem;
        public String info;

        public HUDLine(ItemStack item, String i) {
            this.displayItem = item;
            this.info = i;
        }
    }

    public static void doRenderHUD(EntityPlayer player, World world,
                                   Minecraft mc) {
        /*
		 * //System.out.println("Rendering HUD"); List<HUDLine> lines =
		 * Lists.newArrayList(); // Filling list ItemStack[] armorInv =
		 * player.inventory.armorInventory; for (int i = armorInv.length - 1; i
		 * >= 0; i--) { if (armorInv[i] != null && armorInv[i].getItem()
		 * instanceof IEnergyContainerItem) { IEnergyContainerItem container =
		 * (IEnergyContainerItem) (armorInv[i] .getItem()); StringBuilder info =
		 * new StringBuilder(); info.append(String.format("%d / %d RF",
		 * container.getEnergyStored(armorInv[i]),
		 * container.getMaxEnergyStored(armorInv[i]))); info.append(" "); int
		 * percent = Math.round((float) container .getEnergyStored(armorInv[i])
		 * / (float) container.getMaxEnergyStored(armorInv[i]) 100.0F);
		 * info.append("(" + percent + " %)"); lines.add(new
		 * HUDLine(armorInv[i], info.toString())); } }
		 * //System.out.println(lines.size()); int x = 2; int y = 2;
		 * //GL11.glPushMatrix(); GL11.glDisable(GL11.GL_LIGHTING); for (HUDLine
		 * l : lines) { RenderItem itemRenderer = new RenderItem(); FontRenderer
		 * fontRenderer = mc.fontRenderer;
		 * itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine,
		 * l.displayItem, x, y);
		 * itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine,
		 * l.displayItem, x, y); x += 16; x += 2; y += 6;
		 * fontRenderer.drawString(l.info, x, y, 0xFFFFFF); y -= 6; x = 2; y +=
		 * 16; } GL11.glEnable(GL11.GL_LIGHTING); //GL11.glPopMatrix();
		 */
    }
}
