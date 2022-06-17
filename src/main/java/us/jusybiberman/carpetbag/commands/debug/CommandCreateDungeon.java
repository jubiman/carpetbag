package us.jusybiberman.carpetbag.commands.debug;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import scala.Int;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.dungeons.DungeonManager;
import us.jusybiberman.carpetbag.dungeons.description.DimensionDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandCreateDungeon extends CommandBase {
	@Override
	public String getName() {
		return "createdungeon";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "createdungeon <dimension_id> [players_to_teleport...]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) throw new CommandException("Too little arguments. Expected at least 1, got 0.");
		if (args.length >= 2) {
			List<EntityPlayer> p = new ArrayList<>();
			for (String arg : Arrays.copyOfRange(args, 1, args.length)) {
				try {
					p.add(getPlayer(Carpetbag.SERVER_INSTANCE, sender, arg));
				} catch (PlayerNotFoundException e) {
					throw new CommandException("Player not found: " + arg + ". Please check your spelling and try again.");
				}
			}
			
			List<String> descriptors = new ArrayList<>(); // TODO: temp
			

			DungeonManager dimensionManager = DungeonManager.getDimensionManager(sender.getEntityWorld());
			DimensionDescriptor descriptor = new DimensionDescriptor(descriptors, Integer.parseInt(args[0]));

			Integer dim = dimensionManager.getDimensionID(descriptor);
			if(dim != null) {
				throw new CommandException(TextFormatting.RED + "A dimension with that descriptor already exists: " + dim);
			}


			Carpetbag.DUNGEON_MANAGER.createDungeon(p, ((EntityPlayer)sender).getEntityWorld(), descriptor);
			dimensionManager.save(sender.getEntityWorld());
		}
	}
}
