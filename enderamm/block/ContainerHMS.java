package enderamm.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import thermalexpansion.fluid.TEFluids;

public class ContainerHMS extends Container {
	protected TileEntityHMS tileEntity;

	public ContainerHMS(InventoryPlayer inventoryPlayer,
	        TileEntityHMS te) {
		this.tileEntity = te;
		this.addSlotToContainer(new EASlot(this.tileEntity, 0, 118,
		        29));
		this.bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer p) {
		return true;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); i++) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			//icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.getProgress());
			//icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.getMaxProgress());
			//icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.getStored());
			icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.redstoneTank.getFluidAmount());
			icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.glowstoneTank.getFluidAmount());
			icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.enderTank.getFluidAmount());
			
			icrafting.sendProgressBarUpdate(this, 3, this.tileEntity.getEnergyStored(ForgeDirection.UNKNOWN) & 0xFFFF);
			icrafting.sendProgressBarUpdate(this, 4, this.tileEntity.getEnergyStored(ForgeDirection.UNKNOWN) >>> 16);
		    
			icrafting.sendProgressBarUpdate(this, 5, this.tileEntity.progressGauged);
		}
	}

	@Override
	public void updateProgressBar(int i, int j) {
		if (i == 0) {
			this.tileEntity.redstoneTank.setFluid(new FluidStack(TEFluids.fluidRedstone, j));
		} else if (i == 1) {
			this.tileEntity.glowstoneTank.setFluid(new FluidStack(TEFluids.fluidGlowstone, j));
		} else if (i == 2) {
			this.tileEntity.enderTank.setFluid(new FluidStack(TEFluids.fluidEnder, j));
		} else if (i == 3) {
			this.tileEntity.setEnergyStored(this.tileEntity.getEnergyStored(ForgeDirection.UNKNOWN) & 0xFFFF0000 | j);
		} else if (i == 4) {
			this.tileEntity.setEnergyStored(this.tileEntity.getEnergyStored(ForgeDirection.UNKNOWN) & 0xFFFF | j << 16);
		} else if (i == 5) {
			this.tileEntity.progressGauged = j;
		}
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(inventoryPlayer,
				        j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18,
			        142));
		}
	}
	
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer,
			int slotIndex) {
		return null;
	}
}
