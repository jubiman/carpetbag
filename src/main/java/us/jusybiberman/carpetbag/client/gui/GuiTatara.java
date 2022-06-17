package us.jusybiberman.carpetbag.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.block.tatara.ContainerTatara;
import us.jusybiberman.carpetbag.block.tatara.TileEntityTatara;

public class GuiTatara extends GuiContainer {
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation(Carpetbag.MOD_ID,"textures/gui/tatara.png");
	private final TileEntityTatara tileTatara;

	public GuiTatara(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerTatara(player, world, x, y, z));
		this.tileTatara = (TileEntityTatara) world.getTileEntity(new BlockPos(x,y,z));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_drawGuiContainerForegroundLayer_1_, int p_drawGuiContainerForegroundLayer_2_) {
		fontRenderer.drawString(I18n.format("tile.tatara.name"), 8, 6, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_drawGuiContainerBackgroundLayer_1_, int p_drawGuiContainerBackgroundLayer_2_, int p_drawGuiContainerBackgroundLayer_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
		int offsetLeft = (this.width - this.xSize) / 2;
		int offsetTop = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(offsetLeft, offsetTop, 0, 0, this.xSize, this.ySize);
		int val;
		if(tileTatara.isBurning()) {
			val = tileTatara.getBurnLeftScaled(13);
			this.drawTexturedModalRect(offsetLeft + 56, offsetTop + 36 + 12 - val, 176, 12 - val, 14, val + 1);
		}

		val = tileTatara.getCookProgressScaled(24);
		this.drawTexturedModalRect(offsetLeft + 79, offsetTop + 34, 176, 14, val + 1, 16);
	}
}
