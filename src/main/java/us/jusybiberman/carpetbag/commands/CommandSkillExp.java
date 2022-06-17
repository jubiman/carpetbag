package us.jusybiberman.carpetbag.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import us.jusybiberman.carpetbag.skills.SkillBase;

public class CommandSkillExp extends CommandBase {
	@Override
	public String getName() {
		return "skillexp";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "skillxp <add|remove|set> <skill_type> <amount>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 3) throw new CommandException("Too little arguments. Expected 3 got " + args.length);

		if (sender instanceof EntityPlayer) {
			String skill_type;
			switch (args[1].toLowerCase()) {
				case "mining":
				case "skillmining": {
					skill_type = "SkillMining";
					break;
				}
				case "fishing":
				case "skillfishing": {
					skill_type = "SkillFishing";
					break;
				}
				case "smithing":
				case "skillsmithing": {
					skill_type = "SkillSmithing";
					break;
				}
				case "foraging":
				case "skillforaging": {
					skill_type = "SkillForaging";
					break;
				}
				case "combat":
				case "skillcombat": {
					skill_type = "SkillCombat";
					break;
				}
				default: {
					throw new CommandException(TextFormatting.RED + "Failed to get skill_type. Please check your spelling and try again.");
				}
			}

			int amount;
			try {
				amount = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				throw new CommandException(TextFormatting.RED + "Failed to parse amount. Please check whether it's a valid number");
			}

			switch (args[0].toLowerCase()) {
				case "add": {
					SkillBase.addExp((EntityPlayer) sender, amount, skill_type);
					sender.sendMessage(new TextComponentString(TextFormatting.WHITE + "Added " + amount + " exp to your " + skill_type.substring(5) + " skill."));
					break;
				}
				case "remove": {
					SkillBase.removeExp((EntityPlayer) sender, amount, skill_type);
					sender.sendMessage(new TextComponentString(TextFormatting.WHITE + "Removed " + amount + " exp from your " + skill_type.substring(5) + " skill."));
					break;
				}
				case "set": {
					SkillBase.setExp((EntityPlayer) sender, amount, skill_type);
					sender.sendMessage(new TextComponentString(TextFormatting.WHITE + "Set your " + skill_type.substring(5) + " skill to " + amount + " exp."));
					break;
				}
				default: {
					throw new CommandException(TextFormatting.RED + "Failed to find operation (add|remove|set). Please check your spelling and try again.");
				}
			}
		}
	}
}
