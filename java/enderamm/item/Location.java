package enderamm.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

public class Location {
	private int dimId;
	private int x;
	private int y;
	private int z;

	/**
	 * Initializes a new instance of Location
	 * 
	 * @param dimId
	 *            Dimension ID
	 * @param x
	 *            X Coordinate
	 * @param y
	 *            Y Coordinate
	 * @param z
	 *            Z Coordinate
	 */
	public Location(int dimId, int x, int y, int z) {
		this.dimId = dimId;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Less picky version of the above constructor <br />
	 * Initializes a new instance of Location <br />
	 * Sets Dimension ID to Integer.MIN_VALUE.
	 * 
	 * @param x
	 *            X Coordinate
	 * @param y
	 *            Y Coordinate
	 * @param z
	 *            Z Coordinate
	 */
	public Location(int x, int y, int z) {
		this.dimId = Integer.MIN_VALUE;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object other) {
		boolean eq = true;
		eq = eq && other instanceof Location;
		if (eq) {
			eq = eq && ((Location) other).dimId == this.dimId;
			eq = eq && ((Location) other).x == this.x;
			eq = eq && ((Location) other).y == this.y;
			eq = eq && ((Location) other).z == this.z;
		}
		return eq;
	}

	/**
	 * Initializes empty instance of Location (all values are 0s)
	 */
	public Location() {
		this.dimId = 0;
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 * Self-descriptive
	 * 
	 * @param other
	 *            Location for comparison
	 * @return Amount of space between two points, or Integer.MAX_VALUE if
	 *         another dimension
	 */
	public int getDistance(Location other) {
		if (this.dimId != other.dimId)
			return Integer.MAX_VALUE;
		int xDistance = Math.abs(this.x - other.x);
		int yDistance = Math.abs(this.y - other.y);
		int zDistance = Math.abs(this.z - other.z);

		return xDistance + yDistance + zDistance;
	}

	public Location copy() {
		return new Location(this.dimId, this.x, this.y, this.z);
	}

	public Location move(ForgeDirection dir, int space) {
		Location ret = this.copy();
		ret.x += dir.offsetX * space;
		ret.y += dir.offsetY * space;
		ret.z += dir.offsetZ * space;
		return ret;
	}

	public static final String BLOCK_LOCATION_NBT = "Location";
	private static final String DIM_ID_NBT = "dimId";
	private static final String X_NBT = "xCoord";
	private static final String Y_NBT = "yCoord";
	private static final String Z_NBT = "zCoord";

	/**
	 * Writes an instance of {@link Location} on a given NBT tag
	 * 
	 * @param nbt
	 *            NBT to write to
	 * @param location
	 *            Self-descriptive
	 */
	public static void writeToNBT(NBTTagCompound nbt, Location location) {
		NBTTagCompound LocationC = new NBTTagCompound();
		LocationC.setInteger(DIM_ID_NBT, location.dimId);
		LocationC.setInteger(X_NBT, location.x);
		LocationC.setInteger(Y_NBT, location.y);
		LocationC.setInteger(Z_NBT, location.z);
		nbt.setTag(BLOCK_LOCATION_NBT, LocationC);
	}

	/**
	 * Reads an instance of {@link Location} from a given NBT tag
	 * 
	 * @param nbt
	 *            NBT tag compound to read from
	 * @return An instance of {@link Location} containing read data
	 */
	public static Location readFromNBT(NBTTagCompound nbt) {
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		NBTTagCompound LocationC = nbt.getCompoundTag(BLOCK_LOCATION_NBT);
		Location loc = new Location();
		if ((LocationC.getInteger(X_NBT) == 0)
		        && (LocationC.getInteger(Y_NBT) == 0)
		        && (LocationC.getInteger(Z_NBT) == 0))
			return null;
		loc.setDimId(LocationC.getInteger(DIM_ID_NBT));
		loc.setX(LocationC.getInteger(X_NBT));
		loc.setY(LocationC.getInteger(Y_NBT));
		loc.setZ(LocationC.getInteger(Z_NBT));
		return loc;
	}

	@Override
	public String toString() {
		return "(" + dimId + ":" + x + "," + y + "," + z + ")";
	}

	/**
	 * Returns whether or not DimensionId is valid
	 * 
	 * @param dimId
	 *            Dimension id
	 */
	public static boolean isDimIdValid(int dimId) {
		Integer[] ids = DimensionManager.getIDs();
		for (int id : ids) {
			if (id == dimId)
				return true;
		}
		return false;
	}

	/**
	 * Gets TileEntity
	 * 
	 * @return TileEntity of block on given coordinates
	 */
	public TileEntity getTileEntity() {
		if (!isDimIdValid(this.dimId))
			return null;
		return DimensionManager.getWorld(this.dimId).getTileEntity(this.x,
		        this.y, this.z);
	}

	// Getters & setters ahead
	public int getDimId() {
		return this.dimId;
	}

	public void setDimId(int dimId) {
		this.dimId = dimId;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return this.z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	// End of getters and setters
}
