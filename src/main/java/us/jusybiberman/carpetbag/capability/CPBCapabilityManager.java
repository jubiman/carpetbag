package us.jusybiberman.carpetbag.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.api.capability.ICarpetbagPlayer;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
public class CPBCapabilityManager {
	public static final ResourceLocation CARPETBAG_PLAYER_CAP = new ResourceLocation(Carpetbag.MOD_ID,"carpetbag_player");

	@CapabilityInject(ICarpetbagPlayer.class)
	private static final Capability<ICarpetbagPlayer> CARPETBAG_PLAYER = null;

	public static ICarpetbagPlayer asCarpetbagPlayer(EntityPlayer player) {
		return player.getCapability(getCarpetbagPlayerCap(), null);
	}

	@Nonnull
	public static Capability<ICarpetbagPlayer> getCarpetbagPlayerCap() {
		return CARPETBAG_PLAYER;
	}

	public static void init() {
		CapabilityManager.INSTANCE.register(ICarpetbagPlayer.class, CarpetbagPlayer.Serializer.INSTANCE, () -> new CarpetbagPlayer(new ManaStorage(), new SkillStorage(), new StatStorage(), new PlayerOverlay()));
	}

	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer) {
			event.addCapability(CARPETBAG_PLAYER_CAP, new CarpetbagPlayerCapProvider());
		}
	}

	@SubscribeEvent
	public static void syncCapabilities(EntityJoinWorldEvent event) {
		if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer) {
			EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
			CarpetbagPlayer carpetbagPlayer = (CarpetbagPlayer) asCarpetbagPlayer(player);

			carpetbagPlayer.bindPlayer(player);
			carpetbagPlayer.sendUpdates();
		}
	}

	@SubscribeEvent
	public static void onPlayerDeath(PlayerEvent.Clone event) {
		CarpetbagPlayer.Serializer.INSTANCE.copy(asCarpetbagPlayer(event.getOriginal()), asCarpetbagPlayer(event.getEntityPlayer()));
		//We don't need to send packets here, because after this method, the method onPlayerJoin will be fired.
	}
}
