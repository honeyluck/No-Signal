package com.nosignal.mod.client.renderers;

import com.nosignal.mod.blocks.BlockConnector;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.Vec3d;
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
		for (BlockPos connection : te.getConnections()) {
			IBlockState originState = te.getWorld().getBlockState(te.getPos());
			IBlockState connectionState = te.getWorld().getBlockState(connection);
			if (originState == null || originState.getValue(BlockConnector.FACING) == null) {
				continue;
			}
			if (connectionState == null || connectionState.getValue(BlockConnector.FACING) == null) {
				continue;
			}
			Vec3d originInLoc = originState.getValue(BlockConnector.FACING).getOriginInBlock();
			Vec3d connectionInLoc = connectionState.getValue(BlockConnector.FACING).getOriginInBlock();
			Vec3d originLoc = originInLoc.addVector(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
			Vec3d connectionLoc = connectionInLoc.addVector(connection.getX(), connection.getY(), connection.getZ());

			Vec3d diffInLoc = connectionInLoc.subtract(originInLoc);

			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			mc.getTextureManager().bindTexture(new ResourceLocation(NoSignal.MODID, "textures/blocks/wire.png"));
			{
				BufferBuilder bb = Tessellator.getInstance().getBuffer();
				bb.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_TEX);
				GL11.glLineWidth(8F);
				bb.pos(originInLoc.x, originInLoc.y, originInLoc.z).tex(0, 0).endVertex();
				bb.pos((connectionLoc.x + connectionInLoc.x) - originLoc.x - diffInLoc.x,   (connectionLoc.y + connectionInLoc.y) - originLoc.y - diffInLoc.y, (connectionLoc.z + connectionInLoc.z) - originLoc.z - diffInLoc.z).tex(1, 1).endVertex();
				Tessellator.getInstance().draw();
				GL11.glLineWidth(1F);
			}
			GlStateManager.popMatrix();
		}
	}

}
