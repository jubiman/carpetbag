package us.jusybiberman.carpetbag.interaction.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.interaction.jei.wrapper.SmeltingRecipeWrapper;
import us.jusybiberman.carpetbag.init.ModItems;

import javax.annotation.Nonnull;

public class TataraRecipeCategory implements IRecipeCategory<SmeltingRecipeWrapper> {
    public static final String UID = "bwa.tatara";
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final IDrawable flame;
    @Nonnull
    private final IDrawable arrow;
    @Nonnull
    private final String localizedName;
    public TataraRecipeCategory(IGuiHelper helper) {
        ResourceLocation location = new ResourceLocation(Carpetbag.MOD_ID, "textures/gui/tatara.png");
        background = helper.createDrawable(location, 55, 16, 82, 54);

        IDrawableStatic flameDrawable = helper.createDrawable(location, 176, 0, 14, 14);
        flame = helper.createAnimatedDrawable(flameDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic arrowDrawable = helper.createDrawable(location, 176, 14, 24, 17);
        arrow = helper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

        localizedName = Translator.translateToLocal("inv.tatara.name");
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        flame.draw(minecraft, 2, 20);
        arrow.draw(minecraft, 24, 18);
    }

    @Nonnull
    @Override
    public String getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SmeltingRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 0, 0);
        guiItemStacks.init(1, true, 0, 36);
        guiItemStacks.init(2, false, 60, 18);

        guiItemStacks.set(0, recipeWrapper.getInputs());
        guiItemStacks.set(1, new ItemStack(ModItems.rice_ash, 1));
        guiItemStacks.set(2, recipeWrapper.getOutputs());
    }

    @Override
    public String getModName() {
        return Carpetbag.MOD_NAME;
    }
}
