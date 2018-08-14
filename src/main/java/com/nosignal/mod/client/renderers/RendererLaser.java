package com.nosignal.mod.client.renderers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.google.gson.stream.JsonReader;
import com.nosignal.mod.blocks.BlockLaser;
import com.nosignal.mod.events.Registries;
import com.nosignal.mod.main.NoSignal;
import com.nosignal.mod.tileentity.TileEntityLaser;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

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
		/*HashMap<BlockPos, IBlockState> map = new HashMap<>();
		if(map.isEmpty()) {
			try {
				File f = new File(Minecraft.getMinecraft().mcDataDir + "/render.json");
				if(f.exists()) {
					JsonReader jr = new JsonReader(new FileReader(f));
					jr.beginObject();
					if(jr.nextName().equals("structure")) {
						jr.beginObject();
						String mapName = jr.nextName();
						while(jr.hasNext()) {
							jr.beginObject();
							Block block = null;
							int meta = 0;
							int jx = 0;
							int jy = 0;
							int jz = 0;
							while(jr.hasNext()) {
								String name = jr.nextName();
								if(name.equals("X")) {
									jx = jr.nextInt();
								}
								else if(name.equals("Y")) {
									jy = jr.nextInt();
								}
								else if(name.equals("Z")) {
									jz = jr.nextInt();
								}
								else if(name.equals("Block")) {
									block = Block.getBlockFromName(jr.nextString());
								}
								else if(name.equals("Meta")) {
									meta = jr.nextInt();
								}
							}
							System.out.println(jx + ", " + jy + ", " + jz);
							IBlockState js = block.getStateFromMeta(meta);
							map.put(new BlockPos(jx, jy, jz), js);
							jr.endObject();
							if(jr.hasNext())mapName = jr.nextName();
						}
						jr.endObject();
					}
					jr.endObject();
					
					jr.close();
				}
			}
			catch(IOException e) {
				e.printStackTrace();
				System.err.println("HALP");
			}
		}
		GlStateManager.rotate(180, 1, 0, 0);
		for(BlockPos pos : map.keySet()) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(map.get(pos), 1F);
			GlStateManager.popMatrix();
		}*/
		GlStateManager.popMatrix();
	}
	
}
