package us.jusybiberman.carpetbag.api.capability;

import us.jusybiberman.carpetbag.stats.CPBStatBase;

public interface IStatStorage {
	CPBStatBase getStat(String name);
}
