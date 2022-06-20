package us.jusybiberman.carpetbag.storage;

import net.minecraft.init.Items;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

import static us.jusybiberman.carpetbag.block.tatara.ProviderTatara.isItemFuel;

public class PlayerSideItemStackHandler extends ItemStackHandler {
	private final boolean allowWrite;

	public PlayerSideItemStackHandler(boolean aw, int size) {
		super(size);
		allowWrite = aw;
	}

	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if (index == 2)
		{
			return false;
		}
		else if (index != 1)
		{
			return true;
		}
		else
		{
			return isItemFuel(stack);
		}
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (allowWrite && isItemValidForSlot(slot, stack))
			return super.insertItem(slot, stack, simulate);
		else return stack;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (allowWrite) {
			return super.extractItem(slot, amount, simulate);
		} else return ItemStack.EMPTY;
	}
}
