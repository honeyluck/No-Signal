package com.nosignal.mod.blocks;

import java.util.function.Supplier;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class BlockTileBase extends BlockContainer{

	Supplier<TileEntity> tile;
	
	public BlockTileBase(Material materialIn, Supplier<TileEntity> te) {
		super(materialIn);
		this.tile = te;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return tile.get();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

}
