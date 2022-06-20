package us.jusybiberman.carpetbag.storage;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.UUID;

public class PlayerSideItemStackHandler extends ItemStackHandler {
	private final boolean allowWrite;

	public PlayerSideItemStackHandler(boolean aw, int size) {
		super(size);
		allowWrite = aw;
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (allowWrite) {
			return super.insertItem(slot, stack, simulate);
		} else return stack;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (allowWrite) {
			return super.extractItem(slot, amount, simulate);
		} else return ItemStack.EMPTY;
	}
}
