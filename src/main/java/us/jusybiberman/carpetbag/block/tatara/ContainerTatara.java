package us.jusybiberman.carpetbag.block.tatara;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import us.jusybiberman.carpetbag.api.capability.ICarpetbagPlayer;
import us.jusybiberman.carpetbag.capability.CPBCapabilityManager;
import us.jusybiberman.carpetbag.crafting.manager.CraftingManagerTatara;

import javax.annotation.Nonnull;

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
	public ItemStack transferStackInSlot(@Nonnull EntityPlayer player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if(slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if(index == 2) {// Clicked in the tatara somewhere
				if (!mergeItemStack(stack1, 3, this.inventorySlots.size(), true)) return ItemStack.EMPTY;
				giveExp(stack1);
			}
			else if (index > 2) {
				// if it can be smelted, place in the input slots
				if (CraftingManagerTatara.getInstance().getSmeltingRecipe(stack1) != null) {
					// try to place in either Input slot; add 1 to final input slot because mergeItemStack uses < index
					if (!mergeItemStack(stack1, 0, 1, false))
						return ItemStack.EMPTY;
				}
				// if it's an energy source, place in Fuel slot
				else if (ProviderTatara.isItemFuel(stack1)) {
					if (!mergeItemStack(stack1, 1, 2, false))
						return ItemStack.EMPTY;
				}
				// item in player's inventory, but not in action bar
				else if (index < 30) {
					// place in action bar
					if (!mergeItemStack(stack1, 30, 39, false))
						return ItemStack.EMPTY;
				}
				// item in action bar - place in player inventory
				else if (index < 39 && !mergeItemStack(stack1, 3, 30, false))
					return ItemStack.EMPTY;
			}
			else if(!mergeItemStack(stack1, 3, 39, false)) // Try to merge from tatara inv to player inv
				return ItemStack.EMPTY;



			if(stack1.getCount() == 0)
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
		}
		return stack;
	}

	private void giveExp(ItemStack stack) {
		ICarpetbagPlayer p = CPBCapabilityManager.asCarpetbagPlayer(player);
		boolean lvlup = false;
		switch (stack.getItem().getRegistryName().toString()) {
			case "carpetbag:kera":
				lvlup = p.getSkillStorage().getSkill("smithing").addExp(100L * stack.getCount());
				break;
			case "carpetbag:hocho_tetsu_heated":
			case "carpetbag:tamahagane_heated":
				lvlup = p.getSkillStorage().getSkill("smithing").addExp(300L * stack.getCount());
				break;
			case "carpetbag:tamahagane_reheated":
				lvlup = p.getSkillStorage().getSkill("smithing").addExp(700L * stack.getCount());
				break;
		}
		if (lvlup)
			player.sendMessage(new TextComponentString("Your smithing skill leveled up to level " + p.getSkillStorage().getSkill("smithing").getLevel()));
	}

	@Nonnull
	@Override
	public ItemStack slotClick(int slotId, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull EntityPlayer player) {
		if (slotId == 2) {
			ItemStack stack = this.inventorySlots.get(2).getStack();
			if (this.inventorySlots.get(2).getHasStack() && (clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.PICKUP_ALL) && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
				giveExp(stack);
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
}
