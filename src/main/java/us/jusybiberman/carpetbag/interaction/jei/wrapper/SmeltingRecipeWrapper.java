package us.jusybiberman.carpetbag.interaction.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import us.jusybiberman.carpetbag.crafting.recipes.SmeltingRecipe;

import java.util.List;

public class SmeltingRecipeWrapper implements IRecipeWrapper {
    SmeltingRecipe recipe;

    public SmeltingRecipeWrapper(SmeltingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        // TODO: Figure out how to use the new IIngredientType methods
        ingredients.setInputs(ItemStack.class,getInputs());
        ingredients.setOutputs(ItemStack.class,getOutputs());
    }

    public SmeltingRecipe getRecipe() {
        return recipe;
    }

    public List<ItemStack> getInputs() {
        return recipe.getRecipeInputs();
    }

    public List<ItemStack> getOutputs() { return recipe.getRecipeOutputs(); }
}
