package enderamm.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Lists;

/**
 * A pending replace for OreFinder.
 * 
 * @author mak326428
 * 
 */
public class BlockFinder {
	private ArrayList<Location> blocksFound = Lists.newArrayList();

	private int initialX;
	private int initialY;
	private int initialZ;
	private ItemStack targetBlock;
	private World world;

	public BlockFinder(World w, int x, int y, int z, ItemStack targetBlock) {
		this.initialX = x;
		this.initialY = y;
		this.initialZ = z;
		this.world = w;
		this.targetBlock = targetBlock;
		Location initialBlock = new Location(x, y, z);
		findContinuation(initialBlock, 10);
	}

	/**
	 * Quite self-descriptive.
	 * 
	 * @return
	 */
	public List<Location> getBlocksFound() {
		return this.blocksFound;
	}

	private void findContinuation(Location loc, int power) {
		boolean found = false;
		if (loc != null) {
			for (Location locat : blocksFound) {
				if (locat.equals(loc))
					found = true;
			}
			if (!found)
				blocksFound.add(loc);
		}
		System.out.println(power);
		if (!found) {
			for (int dir = 0; dir < 6; dir++) {
				Location newTh = loc.move(ForgeDirection.values()[dir], 1);
				int currX = newTh.getX();
				int currY = newTh.getY();
				int currZ = newTh.getZ();

				Block currBlock = Block.blocksList[this.world.getBlockId(currX,
						currY, currZ)];
				int meta = world.getBlockMetadata(currX,
						currY, currZ);
				if (currBlock != null) {
					if (currBlock.blockID == targetBlock.itemID && meta == targetBlock.getItemDamage()) {
						if (power >= 0) {
							findContinuation(new Location(
									this.world.provider.dimensionId, currX,
									currY, currZ), power - 1);
						} else {
							//blocksFound = Lists.newArrayList();
							return;
						}
					}
				}
			}
		}
	}
}