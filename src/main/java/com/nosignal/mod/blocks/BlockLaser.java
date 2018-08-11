package com.nosignal.mod.blocks;

import com.nosignal.mod.tileentity.TileEntityLaser;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLaser extends BlockTileBase {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockLaser() {
		super(Material.IRON, TileEntityLaser::new);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
	
	@Override
	public int getStrongPower(IBlockState s, IBlockAccess w, BlockPos pos, EnumFacing side) {
		TileEntity te = w.getTileEntity(pos);
		if(te != null && te instanceof TileEntityLaser) {
			return ((TileEntityLaser)te).isOn ? 15 : 0;
		}
		return 0;
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess w, BlockPos pos, EnumFacing side) {
		TileEntity te = w.getTileEntity(pos);
		if(te != null && te instanceof TileEntityLaser) {
			return ((TileEntityLaser)te).isOn ? 15 : 0;
		}
		return 0;
	}
	
}
