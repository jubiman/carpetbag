package us.jusybiberman.carpetbag.crafting.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import us.jusybiberman.carpetbag.crafting.manager.CraftingManagerTatara;
import us.jusybiberman.carpetbag.item.ModItems;

public class ModRecipes {
	public void init() {
		CraftingManagerTatara.getInstance().addRecipe(Ingredient.fromItem(ModItems.tamahagane), new ItemStack(ModItems.tamahagane_heated, 1));
		CraftingManagerTatara.getInstance().addRecipe(Ingredient.fromItem(ModItems.tamahagane_wrapped), new ItemStack(ModItems.tamahagane_reheated, 1));
		CraftingManagerTatara.getInstance().addRecipe(Ingredient.fromItem(ModItems.hocho_tetsu), new ItemStack(ModItems.hocho_tetsu_heated, 1));
	}
}
