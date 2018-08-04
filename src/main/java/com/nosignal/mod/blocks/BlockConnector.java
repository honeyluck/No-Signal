package com.nosignal.mod.blocks;

import com.nosignal.mod.main.NoSignal;
import com.nosignal.mod.tileentity.TileEntityConnector;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockConnector extends BlockTileBase {

	public static AxisAlignedBB BOX = new AxisAlignedBB(0.34375,0,0.34375, 0.65625, 0.25, 0.65625);

	public BlockConnector() {
		super(Material.IRON, TileEntityConnector::new);
		this.setCreativeTab(NoSignal.tab);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOX;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return BOX;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
    public boolean isOpaqueCube(IBlockState state) {
	    return false;
    }

}
