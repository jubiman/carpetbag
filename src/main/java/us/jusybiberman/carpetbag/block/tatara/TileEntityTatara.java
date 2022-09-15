package us.jusybiberman.carpetbag.block.tatara;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.server.FMLServerHandler;
import net.minecraftforge.items.IItemHandler;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.capability.CPBCapabilityManager;
import us.jusybiberman.carpetbag.crafting.manager.CraftingManagerTatara;
import us.jusybiberman.carpetbag.crafting.recipes.SmeltingRecipe;
import us.jusybiberman.carpetbag.item.ModItems;
import us.jusybiberman.carpetbag.storage.PlayerSideItemStackHandler;
import us.jusybiberman.carpetbag.tileentity.SimpleItemStackHandler;
import us.jusybiberman.carpetbag.tileentity.TileEntityBase;

import java.util.HashMap;
import java.util.UUID;

public class TileEntityTatara extends TileEntityBase implements ITickable {
	private final HashMap<UUID, ProviderTatara> providers = new HashMap<>();
	private NBTTagCompound compound = new NBTTagCompound();

	public void createProvider(EntityPlayer player) {
		if (compound.hasKey(player.getUniqueID().toString())) {
			ProviderTatara p = new ProviderTatara(this, player);
			p.readDataFromNBT(compound.getCompoundTag(player.getUniqueID().toString()));
			providers.put(player.getUniqueID(), p);
			return;
		}
		providers.put(player.getUniqueID(), new ProviderTatara(this, player));
	}

	public PlayerSideItemStackHandler getInventory(EntityPlayer player) {
		if (!providers.containsKey(player.getUniqueID()))
			createProvider(player);
		return providers.get(player.getUniqueID()).inventory;
	}

	public ProviderTatara getProvider(EntityPlayer player) {
		if (!providers.containsKey(player.getUniqueID()))
			createProvider(player);
		return providers.get(player.getUniqueID());
	}


	public boolean isValidStructure()
	{
		//DON'T ASK QUESTIONS YOU'RE NOT READY
		for (int z = -1; z <= 1; z++)
			for (int x = -1; x <= 1; x++)
			{
				IBlockState upperstate = world.getBlockState(pos.add(x,+1,z));
				if(!isTopping(upperstate))
					return false;
				if((z != x || x != 0) && !isBedding(world.getBlockState(pos.add(x,-1,z))))
					return false;
				if(Math.abs(x) == Math.abs(z) && x != 0 && !isStoneBrick(world.getBlockState(pos.add(x,0,z))))
					return false;
			}

		boolean hasSiding = isSiding(world.getBlockState(pos.north())) && isSiding(world.getBlockState(pos.south()));
		hasSiding ^= isSiding(world.getBlockState(pos.east())) && isSiding(world.getBlockState(pos.west()));
		return isHeatSource(world.getBlockState(pos.down())) && hasSiding;
	}

	public boolean isSiding(IBlockState state)
	{
		return state.getBlock() == Blocks.IRON_BLOCK;
	}

	public boolean isBedding(IBlockState state)
	{
		return state.getBlock() == Blocks.CLAY;
	}

	public boolean isTopping(IBlockState state)
	{
		return state.getBlock() == Blocks.NETHER_BRICK;
	}

	public boolean isStoneBrick(IBlockState state)
	{
		return state.getBlock() == Blocks.STONEBRICK;
	}

	public boolean isHeatSource(IBlockState state)
	{
		return state.getMaterial() == Material.LAVA || state.getMaterial() == Material.FIRE;
	}

	@Override
	public void update() {
		for (UUID k : providers.keySet())
			providers.get(k).update();
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound) {
		// We also want to save the other NBT tags even when a player hasn't been online
		compound.merge(this.compound);
		for (UUID k : providers.keySet()) {
			NBTTagCompound tag = new NBTTagCompound();
			providers.get(k).writeDataToNBT(tag);
			compound.setTag(k.toString(), tag);
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound) {
		// We read the data when a provider is created
		Carpetbag.getLogger().debug(compound);
		this.compound = compound;
	}
}
