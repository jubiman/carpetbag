package us.jusybiberman.carpetbag.skills;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import us.jusybiberman.carpetbag.Carpetbag;

public class SkillMining extends SkillBase {
	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		switch(event.getState().getBlock().getTranslationKey()) {
			case "tile.oreDiamond": {
				addExp(event.getPlayer(), 40, "SkillMining");
				break;
			}
			case "tile.oreEmerald": {
				addExp(event.getPlayer(),50, "SkillMining");
				break;
			}
			case "tile.oreGold": {
				addExp(event.getPlayer(),20, "SkillMining");
				break;
			}
			case "tile.oreCoal": {
				addExp(event.getPlayer(),10, "SkillMining");
				break;
			}
		}
	}
}
