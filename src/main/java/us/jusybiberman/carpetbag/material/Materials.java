package us.jusybiberman.carpetbag.material;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class Materials {
	public static final Item.ToolMaterial TAMAHAGANE;

	static {
		TAMAHAGANE = EnumHelper.addToolMaterial("TAMAHAGANE", 10, 153647, 10.0f, 69.0f, 40);
	}
}
