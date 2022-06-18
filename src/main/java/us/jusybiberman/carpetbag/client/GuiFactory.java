package us.jusybiberman.carpetbag.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.config.ModConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GuiFactory implements IModGuiFactory {
	@Override
	public void initialize(Minecraft minecraftInstance) {
		// NO-OP
	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new CarpetbagGuiConfig(parentScreen);
	}


	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}


	public static class CarpetbagGuiConfig extends GuiConfig {

		public CarpetbagGuiConfig(GuiScreen parentScreen) {
			super(parentScreen, getAllElements(), Carpetbag.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ModConfiguration.getMainConfig().toString()));
		}

		public static List<IConfigElement> getAllElements() {

			Set<String> categories = ModConfiguration.getMainConfig().getCategoryNames();

			return new ArrayList<>(categories.stream().filter(s -> !s.contains(".")).map(s -> new DummyConfigElement.DummyCategoryElement(s, s, new ConfigElement(ModConfiguration.getMainConfig().getCategory(s)).getChildElements())).collect(Collectors.toList()));
		}
	}

}
