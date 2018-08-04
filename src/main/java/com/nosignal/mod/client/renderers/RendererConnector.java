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
	
	public RendererConnector() {
		mc = Minecraft.getMinecraft();
	}

	@Override
	public void render(TileEntityConnector te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(!te.getConnection().equals(BlockPos.ORIGIN)) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 1, z + 0.5);
			mc.getTextureManager().bindTexture(new ResourceLocation(NoSignal.MODID, "textures/blocks/wire.png"));
			BufferBuilder bb = Tessellator.getInstance().getBuffer();
			bb.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_TEX);
			GL11.glLineWidth(8F);
			bb.pos(0, 0, 0).tex(0, 0).endVertex();
			bb.pos(te.getConnection().getX() - te.getPos().getX(), te.getConnection().getY() - te.getPos().getY(), te.getConnection().getZ() - te.getPos().getZ()).tex(1, 1).endVertex();
			Tessellator.getInstance().draw();
			GlStateManager.popMatrix();
		}
	}

}
