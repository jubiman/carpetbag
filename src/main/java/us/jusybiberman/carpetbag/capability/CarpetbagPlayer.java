package us.jusybiberman.carpetbag.capability;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import us.jusybiberman.carpetbag.api.capability.*;
import us.jusybiberman.carpetbag.network.NetworkHandler;
import us.jusybiberman.carpetbag.network.S2CSyncCarpetbagCapMsg;
import us.jusybiberman.carpetbag.util.exceptions.WrongSideException;

import javax.annotation.Nullable;
import java.util.UUID;

public class CarpetbagPlayer implements ICarpetbagPlayer {
	private final ManaStorage manaStorage;

	private final SkillStorage skillStorage;
	private final StatStorage statStorage;
	private final PlayerOverlay playerOverlay;

	/**
	 * If equals null, then it is on client, otherwise - on server.
	 */
	private UUID playerId = null;

	public CarpetbagPlayer(ManaStorage ms, SkillStorage ss, StatStorage sts, PlayerOverlay po) {
		manaStorage = ms;
		skillStorage = ss;
		statStorage = sts;
		playerOverlay = po;
	}

	void bindPlayer(EntityPlayerMP player) {
		playerId = player.getPersistentID();
	}

	@Override
	public IManaStorage getManaStorage() {
		return manaStorage;
	}

	@Override
	public ISkillStorage getSkillStorage() {
		return skillStorage;
	}
	@Override
	public IStatStorage getStatStorage() {
		return statStorage;
	}

	@Override
	public IPlayerOverlay getPlayerOverlay() {
		return playerOverlay;
	}

	@Override
	public void onTick(Side side) {
		playerOverlay.onTick();
	}

	@Override
	public void sendUpdates() {
		if (playerId == null) {
			throw new WrongSideException(Side.CLIENT);
		}

		EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerId);
		NetworkHandler.INSTANCE.sendTo(new S2CSyncCarpetbagCapMsg(this), player);
	}

	public static class Serializer extends SyncableStorage<ICarpetbagPlayer, CarpetbagPlayer> {
		public static final Serializer INSTANCE = new Serializer();

		public Serializer() {
			super(CarpetbagPlayer.class);
		}

		@Nullable
		@Override
		public NBTBase writeNBT(Capability<ICarpetbagPlayer> capability, ICarpetbagPlayer instance, EnumFacing side) {
			CarpetbagPlayer playerCap = validateDefaultImpl(instance);

			NBTTagCompound compound = new NBTTagCompound();
			compound.setTag("mana_storage", playerCap.manaStorage.serializeNBT());
			compound.setTag("skill_storage", playerCap.skillStorage.serializeNBT());
			compound.setTag("stat_storage", playerCap.skillStorage.serializeNBT());

			return compound;
		}

		@Override
		public void readNBT(Capability<ICarpetbagPlayer> capability, ICarpetbagPlayer instance, EnumFacing side, NBTBase nbt) {
			CarpetbagPlayer playerCap = validateDefaultImpl(instance);

			NBTTagCompound compound = ((NBTTagCompound) nbt);

			if (compound.hasKey("mana_storage"))
				playerCap.manaStorage.deserializeNBT(compound.getTag("mana_storage"));
			if (compound.hasKey("skill_storage"))
				playerCap.statStorage.deserializeNBT(compound.getTag("skill_storage"));
			if (compound.hasKey("stat_storage"))
				playerCap.statStorage.deserializeNBT(compound.getTag("stat_storage"));
		}

		@Override
		public void writeToBuffer(ICarpetbagPlayer instance, PacketBuffer buffer) {
			CarpetbagPlayer playerCap = validateDefaultImpl(instance);

			playerCap.manaStorage.writeToBuffer(buffer);
			playerCap.skillStorage.writeToBuffer(buffer);
			playerCap.statStorage.writeToBuffer(buffer);
		}

		@Override
		public void readFromBuffer(ICarpetbagPlayer instance, PacketBuffer buffer) {
			CarpetbagPlayer playerCap = validateDefaultImpl(instance);

			playerCap.manaStorage.readFromBuffer(buffer);
			playerCap.skillStorage.readFromBuffer(buffer);
			playerCap.statStorage.readFromBuffer(buffer);
		}

		@Override
		public void copy(ICarpetbagPlayer from, ICarpetbagPlayer to) {
			PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());

			writeToBuffer(from, buffer);
			readFromBuffer(to, buffer);
		}
	}
}
