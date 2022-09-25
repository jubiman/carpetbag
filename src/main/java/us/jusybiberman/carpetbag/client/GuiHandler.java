package us.jusybiberman.carpetbag.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import us.jusybiberman.carpetbag.client.gui.GuiPlayerMenu;
import us.jusybiberman.carpetbag.client.gui.GuiTatara;
import us.jusybiberman.carpetbag.lib.GuiIds;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GuiHandler implements IGuiHandler
{
	private enum GuiContainerConnection
	{
		TATARA(GuiIds.TATARA, "us.jusybiberman.carpetbag.client.gui.GuiTatara", "us.jusybiberman.carpetbag.block.tatara.ContainerTatara"),
		SOAKING_BOX(GuiIds.SOAKING_BOX, "us.jusybiberman.carpetbag.client.gui.GuiSoakingBox", "us.jusybiberman.carpetbag.block.ContainerCherryBox"),
		DRYING_BOX(GuiIds.DRYING_BOX, "us.jusybiberman.carpetbag.client.gui.GuiDryingBox", "us.jusybiberman.carpetbag.block.ContainerCherryBox"),
		INFUSER(GuiIds.INFUSER, "us.jusybiberman.carpetbag.client.gui.GuiInfuser", "us.jusybiberman.carpetbag.block.infuser.ContainerInfuser");

		final int guiID;
		final String guiClass;
		final String containerClass;

		GuiContainerConnection(int guiID, String guiClass, String containerClass)
		{
			this.guiID = guiID;
			this.guiClass = guiClass;
			this.containerClass = containerClass;
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
			case GuiIds.TATARA:
				return new GuiTatara(player, world, x, y, z);
			case GuiIds.PLAYER_MENU:
				return new GuiPlayerMenu();
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(ID) {
			case GuiIds.TATARA:
				return new GuiTatara(player, world, x, y, z);
			case GuiIds.PLAYER_MENU:
				return new GuiPlayerMenu();
		}
		return null;
	}
}