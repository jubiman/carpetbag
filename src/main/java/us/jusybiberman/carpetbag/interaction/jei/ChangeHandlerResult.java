package us.jusybiberman.carpetbag.interaction.jei;

import us.jusybiberman.carpetbag.crafting.ICraftingResult;

public abstract class ChangeHandlerResult extends ChangeHandler {
    ICraftingResult lastResult;

    public ICraftingResult getLastResult() {
        return lastResult;
    }

    @Override
    public void recalculate() {
        lastResult = getResult();
        setup(lastResult);
    }

    public abstract ICraftingResult getResult();

    public abstract void setup(ICraftingResult result);
}
