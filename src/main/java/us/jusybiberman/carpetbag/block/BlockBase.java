package us.jusybiberman.carpetbag.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.item.CarpetbagTab;

public abstract class BlockBase extends Block {
	protected boolean disabled;

	protected BlockBase(String name, Material materialIn) {
		super(materialIn);

		this.setTranslationKey(name);
		this.setRegistryName(new ResourceLocation(Carpetbag.MOD_ID, name));
		this.setCreativeTab(CarpetbagTab.tabCarpetbagMachines);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		super.getSubBlocks(tab, items);
	}
}
