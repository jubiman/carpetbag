package us.jusybiberman.carpetbag.proxy;

import net.minecraft.item.Item;

public interface IProxy {
	Item registerModel(Item item, int metadata);
}
