package com.nosignal.mod.client.renderers;

import org.lwjgl.opengl.GL11;

import com.nosignal.mod.main.NoSignal;
import com.nosignal.mod.tileentity.TileEntityConnector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class RendererConnector extends TileEntitySpecialRenderer<TileEntityConnector> {
	
	Minecraft mc;
	public static final ResourceLocation TEXTURE = new ResourceLocation(NoSignal.MODID, "textures/blocks/wire.png");
	
	public RendererConnector() {
		mc = Minecraft.getMinecraft();
	}

	@Override
	public void render(TileEntityConnector te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.25, z + 0.5);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		GL11.glLineWidth(8F);
		for(BlockPos pos : te.connectionPoints) {
			
			BufferBuilder bb = Tessellator.getInstance().getBuffer();
			bb.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_TEX);
			bb.pos(0, 0, 0).tex(0, 0).endVertex();
			bb.pos(pos.getX() - te.getPos().getX(), pos.getY() - te.getPos().getY(), pos.getZ() - te.getPos().getZ()).tex(1, 1).endVertex();
			Tessellator.getInstance().draw();
		}
		GL11.glLineWidth(1F);
		GlStateManager.popMatrix();
	}

}
