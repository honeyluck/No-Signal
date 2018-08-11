package com.nosignal.mod.world.gen;

import static com.nosignal.mod.events.Registries.BLOCK_HIVE;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

public class HiveGen implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		
		final Biome biome = world.getBiomeForCoordsBody(new BlockPos(chunkX, 0, chunkZ));
		
		if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.END) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
			return;
		}
		
		tryGenerateHive(world, random,chunkX,chunkZ);
	}
	
	private static void  tryGenerateHive (World world, Random rand, int chunk_X, int chunk_Z){
		final BlockPos.MutableBlockPos variableBlockPos = new BlockPos.MutableBlockPos();
		
		int x = chunk_X + rand.nextInt(16);
		int z = chunk_Z + rand.nextInt(16);
		int y = world.getHeight(x, z) - 1;
		variableBlockPos.setPos(x, y, z);
		
		if (!isBlockLeaves(world, variableBlockPos)) return;
		
		int newY = getHeightBelowLeaves(world, x, y, z);
		
		if (newY < 0) return;
		
		variableBlockPos.setY(newY);
		//System.out.println(variableBlockPos.toString());
		world.setBlockState(variableBlockPos, BLOCK_HIVE.getDefaultState());
	}
	
	private static boolean isBlockLeaves(World world, BlockPos blockPos) {
		IBlockState blockState = world.getBlockState(blockPos);
		final Block block = blockState.getBlock();
		return block.isLeaves(blockState, world, blockPos);
	}
	
	private static int getHeightBelowLeaves(World world,int posX, int posY, int posZ){
		final BlockPos.MutableBlockPos variableBlockPos = new BlockPos.MutableBlockPos();
		variableBlockPos.setPos(posX, posY, posZ);
		
		//Try to get a air position below a leave. If not, return -1
		for (int y = posY, sealevel = world.getSeaLevel(); y >= sealevel; --y) {
			variableBlockPos.setY(y);
			final IBlockState blockState = world.getBlockState(variableBlockPos);
			final Block block = blockState.getBlock();
			
			if (block.isLeaves(blockState, world, variableBlockPos)) continue;
			
			if (world.isAirBlock(variableBlockPos)) return y;
			
			return -1;
		}
		return -1;
	}
	
}
