package us.jusybiberman.carpetbag.api.capability;

public interface IManaStorage {
	boolean useEssence(int points);

	void addEssence(int points);

	int getEssenceValue();

	int getMaxValue();

	boolean isFull();

	boolean isBeingUsed();

	void onTick();

	void regen();
}
