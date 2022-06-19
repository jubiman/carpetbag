package us.jusybiberman.carpetbag.interaction.jei.category;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;

@SuppressWarnings("rawtypes")
public abstract class CPBRecipeCategory implements IRecipeCategory {
    @Nonnull
    private final IDrawable background;

    @Nonnull
    private final String localizedName;

    public CPBRecipeCategory(@Nonnull IDrawable background, String unlocalizedName)
    {
        this.background = background;
        this.localizedName = I18n.translateToLocal(unlocalizedName);
    }

    @Nonnull
    public String getTitle()
    {
        return localizedName;
    }

    @Nonnull
    public IDrawable getBackground()
    {
        return background;
    }
}