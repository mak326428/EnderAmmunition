package enderamm.block;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import enderamm.TEProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 09.07.14
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class BlockRockExterminator extends BlockContainer {
    public BlockRockExterminator() {
        super(Material.iron);
        if (FMLCommonHandler.instance().getSide().isClient())
            this.setCreativeTab(TEProxy.tabTEBlocks);
        this.setStepSound(Block.soundTypeMetal);
        this.setHardness(6.0F);
        this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
        this.setBlockName("rock_exterminator");
        LanguageRegistry.instance().addStringLocalization("tile.rock_exterminator.name",
                "Rock Exterminator");
        setHarvestLevel("pickaxe", 3);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityRockExterminator();
    }
}
