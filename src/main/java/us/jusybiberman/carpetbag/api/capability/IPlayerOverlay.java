package us.jusybiberman.carpetbag.api.capability;

import net.minecraft.block.Block;

public interface IPlayerOverlay {
	void onTick();

	/**
	 * Universal time before teleporting for all portals
	 */
	int getTimeBeforeTeleport();

	/**
	 * Call whenever a player collides with portal block
	 */
	void setInPortal(Block portalBlock);

	boolean isInPortal();

	float getPortalOverlayTime();

	float getOldPortalOverlayTime();

	Block getPortalBlockToRender();
}
