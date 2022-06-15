package us.jusybiberman.carpetbag.item;

import net.minecraft.init.Items;
import us.jusybiberman.carpetbag.Carpetbag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class CarpetbagTab extends CreativeTabs {
	public static final @Nonnull CreativeTabs tabCarpetbag, tabCarpetbagItems, tabCarpetbagMaterials, tabCarpetbagMachines;

	static {
		tabCarpetbag = new CarpetbagTab(0, "main");
		tabCarpetbagItems = new CarpetbagTab(1, "items");
		tabCarpetbagMachines = new CarpetbagTab(2, "machines");
		tabCarpetbagMaterials = new CarpetbagTab(3, "materials");
	}

	private final int meta;

	public CarpetbagTab(int meta, String name) {
		super(getUnloc(name));
		this.meta = meta;
	}

	public CarpetbagTab(int meta, int index, String name) {
		super(index, getUnloc(name));
		this.meta = meta;
	}

	private static String getUnloc(String name) {
		return Carpetbag.MOD_ID + "." + name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public @Nonnull ItemStack createIcon() {

		return new ItemStack(Items.DIAMOND_AXE, 1, meta); // Placeholder
	}
}
