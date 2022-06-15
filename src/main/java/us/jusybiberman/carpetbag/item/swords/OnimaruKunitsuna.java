package us.jusybiberman.carpetbag.item.swords;

<<<<<<< HEAD
import us.jusybiberman.carpetbag.item.MCBARPGTab;
=======
import us.jusybiberman.carpetbag.item.CarpetbagTab;
>>>>>>> 5acb3854655ab11c6d9c2c64bfe9be02b965aa98
import us.jusybiberman.carpetbag.material.Materials;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

<<<<<<< HEAD
import static us.jusybiberman.carpetbag.Mcbarpg.MOD_ID;
=======
import static us.jusybiberman.carpetbag.Carpetbag.MOD_ID;
>>>>>>> 5acb3854655ab11c6d9c2c64bfe9be02b965aa98

public class OnimaruKunitsuna extends ItemSword {
	public OnimaruKunitsuna() {
		super(Materials.TAMAHAGANE);
<<<<<<< HEAD
		setRegistryName(MOD_ID, "onimaru_kunitsuna");
		setCreativeTab(MCBARPGTab.tabMCBARPGItems);
	}


	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull final NonNullList<ItemStack> list) {
=======
		setTranslationKey("onimaru_kunitsuna");
		setRegistryName(MOD_ID, "onimaru_kunitsuna");
		setCreativeTab(CarpetbagTab.tabCarpetbagItems);
	}


	/*public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull final NonNullList<ItemStack> list) {
>>>>>>> 5acb3854655ab11c6d9c2c64bfe9be02b965aa98
		if (isInCreativeTab(tab)) {
			ItemStack is = new ItemStack(this);
			list.add(is);

			is = new ItemStack(this);
			// TODO: Add all upgrades for weapon or smth
			list.add(is);
		}
<<<<<<< HEAD
	}
=======
	}*/
>>>>>>> 5acb3854655ab11c6d9c2c64bfe9be02b965aa98
}
