package us.jusybiberman.carpetbag.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.proxy.ClientProxy;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiPlayerMenu extends GuiScreen {

	public static final ResourceLocation background = new ResourceLocation(Carpetbag.MOD_ID,"textures/gui/main_menu.png");

	static final ResourceLocation CURIO_INVENTORY = new ResourceLocation(Carpetbag.MOD_ID, "textures/gui/inventory.png");

	private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	private final GuiButton bBack = new GuiButton(0, this.width - 200, this.height - 200, "Back");

	/** The old x position of the mouse pointer */
	private float oldMouseX;

	/** The old y position of the mouse pointer */
	private float oldMouseY;
	private boolean widthTooNarrow;

	private boolean buttonClicked;

	public GuiPlayerMenu()
	{
		this.allowUserInput = true;
	}

	private void resetGuiLeft()
	{
		//this.guiLeft = (this.width - this.xSize) / 2;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		//((ContainerPlayerExpanded)inventorySlots).baubles.setEventBlock(false);
		//updateActivePotionEffects();
		resetGuiLeft();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		super.initGui();
		buttonList.clear();
		buttonList.add(bBack);

		//resetGuiLeft();
	}


	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		this.oldMouseX = (float) mouseX;
		this.oldMouseY = (float) mouseY;
		super.drawScreen(mouseX, mouseY, partialTicks);
		//this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button.id == 1)
		{
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
		}
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		if (par2 == ClientProxy.KEY_PLAYERMENU.getKeyCode())
			this.mc.player.closeScreen();
		else super.keyTyped(par1, par2);
	}

	public void displayNormalInventory()
	{
		GuiInventory gui = new GuiInventory(this.mc.player);
		ObfuscationReflectionHelper.setPrivateValue(GuiInventory.class, gui, this.oldMouseX, "field_147048_u");
		ObfuscationReflectionHelper.setPrivateValue(GuiInventory.class, gui, this.oldMouseY, "field_147047_v");
		this.mc.displayGuiScreen(gui);
	}
}
