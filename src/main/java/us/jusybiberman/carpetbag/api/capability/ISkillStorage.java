package us.jusybiberman.carpetbag.api.capability;

import us.jusybiberman.carpetbag.skills.SkillBase;

public interface ISkillStorage {
	SkillBase getSkill(String name);
}
