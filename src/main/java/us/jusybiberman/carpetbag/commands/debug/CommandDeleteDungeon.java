package us.jusybiberman.carpetbag.commands.debug;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.io.FileUtils;
import scala.actors.threadpool.Arrays;
import us.jusybiberman.carpetbag.dungeons.DimensionInformation;
import us.jusybiberman.carpetbag.dungeons.DimensionStorage;
import us.jusybiberman.carpetbag.dungeons.DungeonManager;
import us.jusybiberman.carpetbag.util.ModConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CommandDeleteDungeon extends CommandBase {
	private String[] aliases = {
			"deldungeon"
	};
	@Override
	public List<String> getAliases() {
		return Arrays.asList(aliases);
	}
	@Override
	public String getName() {
		return "deletedungeon";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "deletedungeon <dimension_id>";
	}
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		if (args.length < 1) {
			ITextComponent component = new TextComponentString(TextFormatting.RED + "The dimension parameter is missing!");
			if (sender instanceof EntityPlayer) {
				((EntityPlayer) sender).sendStatusMessage(component, false);
			} else {
				sender.sendMessage(component);
			}
			return;
		} else if (args.length > 1) {
			ITextComponent component = new TextComponentString(TextFormatting.RED + "Too many parameters!");
			if (sender instanceof EntityPlayer) {
				((EntityPlayer) sender).sendStatusMessage(component, false);
			} else {
				sender.sendMessage(component);
			}
			return;
		}

		int dim = fetchInt(sender, args);
		World world = sender.getEntityWorld();

		DungeonManager dimensionManager = DungeonManager.getDimensionManager(world);
		if (dimensionManager.getDimensionDescriptor(dim) == null) {
			ITextComponent component = new TextComponentString(TextFormatting.RED + "Not a dungeon dimension!");
			if (sender instanceof EntityPlayer) {
				((EntityPlayer) sender).sendStatusMessage(component, false);
			} else {
				sender.sendMessage(component);
			}
			return;
		}

		World w = DimensionManager.getWorld(dim);
		if (w != null) {
			ITextComponent component = new TextComponentString(TextFormatting.RED + "Dimension is still in use!");
			if (sender instanceof EntityPlayer) {
				((EntityPlayer) sender).sendStatusMessage(component, false);
			} else {
				sender.sendMessage(component);
			}
			return;
		}

		dimensionManager.removeDimension(dim);
		dimensionManager.reclaimId(dim);
		dimensionManager.save(world);

		DimensionStorage dimensionStorage = DimensionStorage.getDimensionStorage(world);
		dimensionStorage.removeDimension(dim);
		dimensionStorage.save();

		if (ModConfiguration.dimensionFolderIsDeletedWithSafeDel) {
			File rootDirectory = DimensionManager.getCurrentSaveRootDirectory();
			try {
				FileUtils.deleteDirectory(new File(rootDirectory.getPath() + File.separator + "CARPETBAG" + dim));
				ITextComponent component = new TextComponentString("Dimension deleted and dimension folder succesfully wiped!");
				if (sender instanceof EntityPlayer) {
					((EntityPlayer) sender).sendStatusMessage(component, false);
				} else {
					sender.sendMessage(component);
				}
			} catch (IOException e) {
				ITextComponent component = new TextComponentString(TextFormatting.RED + "Dimension deleted but dimension folder could not be completely wiped!");
				if (sender instanceof EntityPlayer) {
					((EntityPlayer) sender).sendStatusMessage(component, false);
				} else {
					sender.sendMessage(component);
				}
			}
		} else {
			ITextComponent component = new TextComponentString("Dimension deleted. Please remove the dimension folder from disk!");
			if (sender instanceof EntityPlayer) {
				((EntityPlayer) sender).sendStatusMessage(component, false);
			} else {
				sender.sendMessage(component);
			}
		}
	}

	private int fetchInt(ICommandSender sender, String[] args) {
		if(1 >= args.length) return 0;
		try {
			return Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			ITextComponent component = new TextComponentString(TextFormatting.RED + "Parameter is not a valid integer!");
			if (sender instanceof EntityPlayer) {
				((EntityPlayer) sender).sendStatusMessage(component, false);
			} else {
				sender.sendMessage(component);
			}
			return 0;
		}
	}
}
