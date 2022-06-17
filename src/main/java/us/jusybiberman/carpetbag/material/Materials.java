package us.jusybiberman.carpetbag.material;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class Materials {
	public static final Item.ToolMaterial bambooToolMaterial;
	public static final Item.ToolMaterial tamahaganeToolMaterial;
	public static final Item.ToolMaterial japansteelToolMaterial;
	static {
		tamahaganeToolMaterial = EnumHelper.addToolMaterial("tamahagane", 10, 153647, 10.0f, 69.0f, 40);
		japansteelToolMaterial = EnumHelper.addToolMaterial("japansteel", 3, 1710, 8.0f, 4.0f, 10);
		bambooToolMaterial = EnumHelper.addToolMaterial("bamboo", 0, 51, 0.5f, -4.0f, 1);
	}
}
