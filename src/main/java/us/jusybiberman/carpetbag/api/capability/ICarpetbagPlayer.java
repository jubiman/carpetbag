package us.jusybiberman.carpetbag.api.capability;

import net.minecraftforge.fml.relauncher.Side;
import us.jusybiberman.carpetbag.capability.ManaStorage;
import us.jusybiberman.carpetbag.capability.PlayerOverlay;
import us.jusybiberman.carpetbag.capability.SkillStorage;
import us.jusybiberman.carpetbag.capability.StatStorage;
import us.jusybiberman.carpetbag.util.exceptions.WrongSideException;

public interface ICarpetbagPlayer {
	ManaStorage getManaStorage();

	SkillStorage getSkillStorage();
	StatStorage getStatStorage();
	PlayerOverlay getPlayerOverlay();

	void onTick(Side side);

	/**
	 * Syncs all capability data with client.
	 * Should be called only on server.
	 *
	 * @throws WrongSideException if called on client.
	 */
	void sendUpdates();
}
