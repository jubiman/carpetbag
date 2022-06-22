package us.jusybiberman.carpetbag.commands.debug;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.capability.CPBCapabilityManager;

public class CommandCarpetbagData extends CommandBase {
	@Override
	public String getName() {
		return "carpetbagdata";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "carpetbagdata";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			sender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "CarpetbagData:\n" + CPBCapabilityManager.asCarpetbagPlayer((EntityPlayer) sender).toString()));
			return;
		}
		if(args.length != 1) throw new CommandException("Too little arguments. Expected 1 got " + args.length);
		EntityPlayer player;
		try {
			player = getPlayer(Carpetbag.SERVER_INSTANCE, sender, args[0]);
		} catch (PlayerNotFoundException e) {
			throw new CommandException(TextFormatting.RED + "Player not found. Please check your spelling and try again.");
		}
		sender.sendMessage(new TextComponentString(TextFormatting.YELLOW + args[0] + "'s CarpetbagData:" + CPBCapabilityManager.asCarpetbagPlayer(player).toString()));
	}
}
