package us.jusybiberman.carpetbag.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Objects;

public class ClientProxy implements IProxy {
	public static final KeyBinding KEY_PLAYERMENU = new KeyBinding("keybind.playermenu", Keyboard.KEY_M, "key.categories.inventory");

	public Item registerModel(Item item, int metadata)
	{
		ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
		return item;
	}

	public void registerKeybinds() {
		ClientRegistry.registerKeyBinding(KEY_PLAYERMENU);
	}
}
