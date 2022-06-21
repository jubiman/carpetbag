package us.jusybiberman.carpetbag.crafting.recipes;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import us.jusybiberman.carpetbag.api.IHasLevelReq;
import us.jusybiberman.carpetbag.util.IHasSize;

import java.util.*;
import java.util.stream.Collectors;

public class SmeltingRecipe implements IHasLevelReq {
	public Ingredient input;
	public ItemStack output;

	private final int levelReq;

	public SmeltingRecipe(Ingredient i, ItemStack o, int lvlReq) {
		input = i;
		output = o;
		levelReq = lvlReq;
	}

	public List<ItemStack> getOutput(List<ItemStack> inputs, TileEntity tile) {
		return Lists.newArrayList(this.output);
	}

	public List<ItemStack> getRecipeInputs() {
		return Lists.newArrayList(input.getMatchingStacks());
	}

	public List<ItemStack> getRecipeOutputs() {
		List<ItemStack> inputs = getRecipeInputs();
		return inputs.stream().map(input1 -> getOutput(Lists.newArrayList(input1), null)).map(this::collapse).collect(Collectors.toCollection(ArrayList::new));
	}

	private ItemStack collapse(List<ItemStack> stacks) {
		if(stacks.isEmpty())
			return ItemStack.EMPTY;
		else
			return stacks.get(0);
	}

	public boolean matchesInput(ItemStack item) {
		return input.apply(item);
	}

	public int getInputCount() {
		return input instanceof IHasSize ? ((IHasSize) input).getSize() : 1;
	}

	@Override
	public int getLevelReq() {
		return levelReq;
	}
}
