package enderamm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thermalexpansion.ThermalExpansion;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import enderamm.EnderAmmunition;

public class BlockHMS extends BlockContainer {

	public BlockHMS(int par1) {
		super(par1, Material.iron);
		if (FMLCommonHandler.instance().getSide().isClient())
			this.setCreativeTab(ThermalExpansion.tabBlocks);
		this.setStepSound(Block.soundMetalFootstep);
		this.setHardness(3.0F);
		this.setBlockBounds(0.125F, 0F, 0.125F, 0.8175F, 0.9375F, 0.8175F);
		this.setUnlocalizedName("hms");
		LanguageRegistry.instance().addStringLocalization("tile.hms.name",
				"High-Molecular Synthesizer");
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityHMS();
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (player.isSneaking())
			return false;
		else {
			if (!world.isRemote) {
				TileEntity tile = world.getBlockTileEntity(x, y, z);
				if (tile != null) {
					player.openGui(EnderAmmunition.instance, 16384, world, x, y, z);
				}
			}

			return true;
		}
	}

}
