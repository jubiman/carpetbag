package us.jusybiberman.carpetbag.skills;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.capability.CPBCapabilityManager;

public class SkillMining extends SkillBase {
	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		switch(event.getState().getBlock().getTranslationKey()) {
			case "tile.oreDiamond": {
				CPBCapabilityManager.asCarpetbagPlayer(event.getPlayer()).getSkillStorage().getSkill("mining").addExp(40);
				break;
			}
			case "tile.oreEmerald": {
				CPBCapabilityManager.asCarpetbagPlayer(event.getPlayer()).getSkillStorage().getSkill("mining").addExp(50);
				break;
			}
			case "tile.oreGold": {
				CPBCapabilityManager.asCarpetbagPlayer(event.getPlayer()).getSkillStorage().getSkill("mining").addExp(20);
				break;
			}
			case "tile.oreCoal": {
				CPBCapabilityManager.asCarpetbagPlayer(event.getPlayer()).getSkillStorage().getSkill("mining").addExp(10);
				break;
			}
		}
	}
}
