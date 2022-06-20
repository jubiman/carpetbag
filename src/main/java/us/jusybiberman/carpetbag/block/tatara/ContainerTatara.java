package us.jusybiberman.carpetbag.block.tatara;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class ContainerTatara extends Container {
	private final TileEntityTatara tileTatara;
	private final EntityPlayer player;
	private int cookTime;
	private int totalCookTime;
	private int furnaceBurnTime;
	private int currentItemBurnTime;

	public ContainerTatara(EntityPlayer player, World world, int x, int y, int z) {
		tileTatara = (TileEntityTatara) world.getTileEntity(new BlockPos(x, y, z));
		this.player = player;

		addSlotToContainer(new SlotItemHandler(tileTatara.getInventory(player), 0, 56, 17));
		addSlotToContainer(new SlotItemHandler(tileTatara.getInventory(player), 1, 56, 53));
		addSlotToContainer(new SlotItemHandler(tileTatara.getInventory(player), 2, 116, 35));

		bindPlayerInventory(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null));
	}

	protected void bindPlayerInventory(IItemHandler inventoryPlayer) {
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				addSlotToContainer(new SlotItemHandler(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int i = 0; i < 9; i++)
		{
			addSlotToContainer(new SlotItemHandler(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (IContainerListener craft : this.listeners) {
			if (this.furnaceBurnTime != tileTatara.getProvider(player).furnaceBurnTime) {
				craft.sendWindowProperty(this, 0, tileTatara.getProvider(player).furnaceBurnTime);
			}

			if (this.currentItemBurnTime != tileTatara.getProvider(player).currentItemBurnTime) {
				craft.sendWindowProperty(this, 1, tileTatara.getProvider(player).currentItemBurnTime);
			}

			if (this.cookTime != tileTatara.getProvider(player).cookTime) {
				craft.sendWindowProperty(this, 2, tileTatara.getProvider(player).cookTime);
			}

			if (this.totalCookTime != tileTatara.getProvider(player).totalCookTime) {
				craft.sendWindowProperty(this, 3, tileTatara.getProvider(player).totalCookTime);
			}
		}
		this.furnaceBurnTime = tileTatara.getProvider(player).furnaceBurnTime;
		this.currentItemBurnTime = tileTatara.getProvider(player).currentItemBurnTime;
		this.cookTime = tileTatara.getProvider(player).cookTime;
		this.totalCookTime = tileTatara.getProvider(player).totalCookTime;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int fieldId, int fieldValue) {
		switch(fieldId)
		{
			case 0: tileTatara.getProvider(player).furnaceBurnTime = fieldValue; break;
			case 1: tileTatara.getProvider(player).currentItemBurnTime = fieldValue; break;
			case 2: tileTatara.getProvider(player).cookTime = fieldValue; break;
			case 3: tileTatara.getProvider(player).totalCookTime = fieldValue; break;
		}
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player)
	{
		return true;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(@Nonnull EntityPlayer player, int index)
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if(slot != null && slot.getHasStack())
		{
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if(index < 3)
			{
				if(!mergeItemStack(stack1, 3, this.inventorySlots.size(), true))
					return ItemStack.EMPTY;
			}
			else if(!mergeItemStack(stack1, 0, 3, false))
				return ItemStack.EMPTY;
			if(stack1.getCount() == 0)
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
		}
		return stack;
	}
}
