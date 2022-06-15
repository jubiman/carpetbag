package us.jusybiberman.carpetbag.item;

import net.minecraft.init.Items;
import us.jusybiberman.carpetbag.Mcbarpg;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class MCBARPGTab extends CreativeTabs {
	public static final @Nonnull CreativeTabs tabMCBARPG, tabMCBARPGItems, tabMCBARPGMaterials, tabMCBARPGMachines;

	static {
		tabMCBARPG = new MCBARPGTab(0, "main");
		tabMCBARPGItems = new MCBARPGTab(1, "items");
		tabMCBARPGMachines = new MCBARPGTab(2, "machines");
		tabMCBARPGMaterials = new MCBARPGTab(3, "materials");
	}

	private final int meta;

	public MCBARPGTab(int meta, String name) {
		super(getUnloc(name));
		this.meta = meta;
	}

	public MCBARPGTab(int meta, int index, String name) {
		super(index, getUnloc(name));
		this.meta = meta;
	}

	private static String getUnloc(String name) {
		return Mcbarpg.MOD_ID + "." + name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public @Nonnull ItemStack createIcon() {
		return new ItemStack(Items.STONE_AXE, 1, meta);
	}
	/*public static final CreativeTabs WEAPONS = new CreativeTabs("mcbammorpg:weapons") {
		@SideOnly(Side.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(CustomItems.ONIMARU_KUNITSUNA);
		}
	};
	public static CreativeTabs MATERIALS = new CreativeTabs("mcbammorpg:materials") {
		public ItemStack createIcon() {
			return new ItemStack(CustomItems.TAMAHAGANE_INGOT);
		}
	};
	public static CreativeTabs FUNCTIONAL_BLOCKS = new CreativeTabs("mcbammorpg:functional") {
		@SideOnly(Side.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(CustomItems.TATARA);
		}
	};*/
}
