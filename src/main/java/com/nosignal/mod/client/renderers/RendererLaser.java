package com.nosignal.mod.client.renderers;

import com.nosignal.mod.blocks.BlockLaser;
import com.nosignal.mod.events.Registries;
import com.nosignal.mod.main.NoSignal;
import com.nosignal.mod.tileentity.TileEntityLaser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class RendererLaser extends TileEntitySpecialRenderer<TileEntityLaser> {

    public RendererLaser() {
    }

    @Override
    public void render(TileEntityLaser te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        IBlockState state = te.getWorld().getBlockState(te.getPos());
        if (state.getBlock() == Registries.BLOCK_LASER_EMITTER) {
            GL11.glLineWidth(12F);
            //TODO:Make a texture
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(NoSignal.MODID, "textures/blocks/wire.png"));
            BlockPos pos = te.getPos().offset(state.getValue(BlockLaser.FACING), 6);
            BufferBuilder bb = Tessellator.getInstance().getBuffer();
            bb.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_TEX);
            bb.pos(0, 0, 0).tex(0, 0).endVertex();
            bb.pos(pos.getX() - te.getPos().getX(), pos.getY() - te.getPos().getY(), pos.getZ() - te.getPos().getZ()).tex(1, 1).endVertex();
            Tessellator.getInstance().draw();
            GL11.glLineWidth(1F);
        }
        GlStateManager.popMatrix();
    }

}
