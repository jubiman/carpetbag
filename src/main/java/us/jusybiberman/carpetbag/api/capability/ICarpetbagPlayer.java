package us.jusybiberman.carpetbag.api.capability;

import net.minecraftforge.fml.relauncher.Side;
import us.jusybiberman.carpetbag.util.exceptions.WrongSideException;

public interface ICarpetbagPlayer {
	IManaStorage getManaStorage();

	IPlayerOverlay getPlayerOverlay();

	ISkillStorage getSkillStorage();

	void onTick(Side side);

	/**
	 * Syncs all capability data with client.
	 * Should be called only on server.
	 *
	 * @throws WrongSideException if called on client.
	 */
	void sendUpdates();
}
