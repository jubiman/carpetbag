package us.jusybiberman.carpetbag.crafting.manager;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import us.jusybiberman.carpetbag.crafting.recipes.SmeltingRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CraftingManagerTatara {
	private static final CraftingManagerTatara instance = new CraftingManagerTatara();

	public static CraftingManagerTatara getInstance() {
		return instance;
	}

	private final ArrayList<SmeltingRecipe> recipes = new ArrayList<>();

	private CraftingManagerTatara() {
	}

	public void addRecipe(Ingredient input, ItemStack output, int level) {
		this.recipes.add(createRecipe(input, output, level));
	}

	public void addRecipe(SmeltingRecipe recipe) {
		this.recipes.add(recipe);
	}

	protected SmeltingRecipe createRecipe(Ingredient input, ItemStack output, int level)
	{
		return new SmeltingRecipe(input, output, level);
	}

	public SmeltingRecipe getSmeltingRecipe(ItemStack input) {
		Iterator<SmeltingRecipe> var2 = this.recipes.iterator();

		SmeltingRecipe entry;
		do {
			if(!var2.hasNext()) {
				return null;
			}

			entry = var2.next();
		} while(!entry.matchesInput(input));

		return entry;
	}

	public List<SmeltingRecipe> findRecipeForRemoval(@Nonnull ItemStack input) {
		return recipes.stream().filter(recipe -> recipe.matchesInput(input)).collect(Collectors.toList());
	}

	public List<SmeltingRecipe> getRecipes() {
		return this.recipes;
	}
}
