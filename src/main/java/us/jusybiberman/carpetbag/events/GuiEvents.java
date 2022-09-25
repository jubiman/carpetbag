package us.jusybiberman.carpetbag.events;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import us.jusybiberman.carpetbag.client.gui.GuiMenuButton;

public class GuiEvents {
	@SubscribeEvent
	public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if (event.getGui() instanceof GuiInventory) {
			event.getButtonList().add(new GuiMenuButton(55, (GuiContainer) event.getGui(), 64, 9, 10, 10,
					I18n.format("button.stats")));
		}
	}
}
