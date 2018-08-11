package com.nosignal.mod.blocks;

import com.nosignal.mod.main.NoSignal;
import com.nosignal.mod.tileentity.TileEntityConnector;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConnector extends BlockTileBase {
	
	
	public static final PropertyEnum<EnumOrientation> FACING = PropertyEnum.<BlockConnector.EnumOrientation>create("facing", BlockConnector.EnumOrientation.class);
	
	protected static final AxisAlignedBB CONNECTOR_NORTH_AABB = new AxisAlignedBB(0.34375D, 0.34375D, 0.75D, 0.65625D, 0.65625D, 1.0D);
	protected static final AxisAlignedBB CONNECTOR_SOUTH_AABB = new AxisAlignedBB(0.34375D, 0.34375D, 0.0D, 0.65625D, 0.65625D, 0.25D);
	protected static final AxisAlignedBB CONNECTOR_WEST_AABB = new AxisAlignedBB(0.75D, 0.34375D, 0.34375D, 1.0D, 0.65625D, 0.65625D);
	protected static final AxisAlignedBB CONNECTOR_EAST_AABB = new AxisAlignedBB(0.0D, 0.34375D, 0.3125D, 0.25D, 0.65625D, 0.6875D);
	protected static final AxisAlignedBB CONNECTOR_DOWN_AABB = new AxisAlignedBB(0.34375,1.0,0.34375, 0.65625, 0.75, 0.65625);
	protected static final AxisAlignedBB CONNECTOR_UP_AABB = new AxisAlignedBB(0.34375,0,0.34375, 0.65625, 0.25, 0.65625);
	
	public BlockConnector() {
		super(Material.IRON, TileEntityConnector::new);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, BlockConnector.EnumOrientation.NORTH));
		this.setCreativeTab(NoSignal.tab);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(FACING)) {
			case EAST:
				return CONNECTOR_EAST_AABB;
			case WEST:
				return CONNECTOR_WEST_AABB;
			case SOUTH:
				return CONNECTOR_SOUTH_AABB;
			case NORTH:
				return CONNECTOR_NORTH_AABB;
			case UP_Z:
			case UP_X:
			default:
				return CONNECTOR_UP_AABB;
			case DOWN_Z:
			case DOWN_X:
				return CONNECTOR_DOWN_AABB;
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return getBoundingBox(blockState, worldIn, pos);
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	/**
	 * Check whether this Block can be placed at pos, while aiming at the specified side of an adjacent block
	 */
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	{
		return canAttachTo(worldIn, pos, side);
	}
	
	/**
	 * Checks if this block can be placed exactly at the given position.
	 */
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		for (EnumFacing enumfacing : EnumFacing.values())
		{
			if (canAttachTo(worldIn, pos, enumfacing))
			{
				return true;
			}
		}
		
		return false;
	}
	
	protected static boolean canAttachTo(World worldIn, BlockPos pos, EnumFacing direction)
	{
		BlockPos blockpos = pos.offset(direction.getOpposite());
		IBlockState iblockstate = worldIn.getBlockState(blockpos);
		boolean flag = iblockstate.getBlockFaceShape(worldIn, blockpos, direction) == BlockFaceShape.SOLID;
		Block block = iblockstate.getBlock();
		
		if (direction == EnumFacing.UP)
		{
			return iblockstate.isTopSolid() || !isExceptionBlockForAttaching(block) && flag;
		}
		else
		{
			return !isExceptBlockForAttachWithPiston(block) && flag;
		}
	}
	
	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
	 * IBlockstate
	 */
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState iblockstate = this.getDefaultState();
		
		if (canAttachTo(worldIn, pos, facing))
		{
			return iblockstate.withProperty(FACING, BlockConnector.EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
		}
		else
		{
			for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
			{
				if (enumfacing != facing && canAttachTo(worldIn, pos, enumfacing))
				{
					return iblockstate.withProperty(FACING, BlockConnector.EnumOrientation.forFacings(enumfacing, placer.getHorizontalFacing()));
				}
			}
			
			if (worldIn.getBlockState(pos.down()).isTopSolid())
			{
				return iblockstate.withProperty(FACING, BlockConnector.EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing()));
			}
			else
			{
				return iblockstate;
			}
		}
	}
	
	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, BlockConnector.EnumOrientation.byMetadata(meta & 7));
	}
	
	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i = i | state.getValue(FACING).getMetadata();
		
		return i;
	}
	
	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		switch (rot)
		{
			case CLOCKWISE_180:
				
				switch (state.getValue(FACING))
				{
					case EAST:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.WEST);
					case WEST:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.EAST);
					case SOUTH:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.NORTH);
					case NORTH:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.SOUTH);
					default:
						return state;
				}
				
			case COUNTERCLOCKWISE_90:
				
				switch (state.getValue(FACING))
				{
					case EAST:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.NORTH);
					case WEST:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.SOUTH);
					case SOUTH:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.EAST);
					case NORTH:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.WEST);
					case UP_Z:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.UP_X);
					case UP_X:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.UP_Z);
					case DOWN_X:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.DOWN_Z);
					case DOWN_Z:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.DOWN_X);
				}
				
			case CLOCKWISE_90:
				
				switch (state.getValue(FACING))
				{
					case EAST:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.SOUTH);
					case WEST:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.NORTH);
					case SOUTH:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.WEST);
					case NORTH:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.EAST);
					case UP_Z:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.UP_X);
					case UP_X:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.UP_Z);
					case DOWN_X:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.DOWN_Z);
					case DOWN_Z:
						return state.withProperty(FACING, BlockConnector.EnumOrientation.DOWN_X);
				}
				
			default:
				return state;
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
	
	public static enum EnumOrientation implements IStringSerializable
	{
		DOWN_X(0, "down_x", EnumFacing.DOWN, new Vec3d(0.5D, 0.75D,0.5D)),
		EAST(1, "east", EnumFacing.EAST, new Vec3d(0.25D, 0.5D,0.5D)),
		WEST(2, "west", EnumFacing.WEST, new Vec3d(0.75D, 0.5D,0.5D)),
		SOUTH(3, "south", EnumFacing.SOUTH, new Vec3d(0.5D, 0.5D,0.25D)),
		NORTH(4, "north", EnumFacing.NORTH, new Vec3d(0.5D, 0.5D,0.75D)),
		UP_Z(5, "up_z", EnumFacing.UP, new Vec3d(0.5D, 0.25D,0.5D)),
		UP_X(6, "up_x", EnumFacing.UP, new Vec3d(0.5D, 0.25D,0.5D)),
		DOWN_Z(7, "down_z", EnumFacing.DOWN, new Vec3d(0.5D, 0.75D,0.5D));
		
		private static final BlockConnector.EnumOrientation[] META_LOOKUP = new BlockConnector.EnumOrientation[values().length];
		private final int meta;
		private final String name;
		private final EnumFacing facing;
		private final Vec3d originInBlock;
		
		EnumOrientation(int meta, String name, EnumFacing facing, Vec3d originInBlock)
		{
			this.meta = meta;
			this.name = name;
			this.facing = facing;
			this.originInBlock = originInBlock;
		}
		
		public Vec3d getOriginInBlock() {
			return originInBlock;
		}
		
		public int getMetadata()
		{
			return this.meta;
		}
		
		public EnumFacing getFacing()
		{
			return this.facing;
		}
		
		@Override
		public String toString()
		{
			return this.name;
		}
		
		public static BlockConnector.EnumOrientation byMetadata(int meta)
		{
			if (meta < 0 || meta >= META_LOOKUP.length)
			{
				meta = 0;
			}
			
			return META_LOOKUP[meta];
		}
		
		public static BlockConnector.EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing)
		{
			switch (clickedSide)
			{
				case DOWN:
					
					switch (entityFacing.getAxis())
					{
						case X:
							return DOWN_X;
						case Z:
							return DOWN_Z;
						default:
							throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
					}
					
				case UP:
					
					switch (entityFacing.getAxis())
					{
						case X:
							return UP_X;
						case Z:
							return UP_Z;
						default:
							throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
					}
					
				case NORTH:
					return NORTH;
				case SOUTH:
					return SOUTH;
				case WEST:
					return WEST;
				case EAST:
					return EAST;
				default:
					throw new IllegalArgumentException("Invalid facing: " + clickedSide);
			}
		}
		
		@Override
		public String getName()
		{
			return this.name;
		}
		
		static
		{
			for (BlockConnector.EnumOrientation blockconnector$enumorientation : values())
			{
				META_LOOKUP[blockconnector$enumorientation.getMetadata()] = blockconnector$enumorientation;
			}
		}
	}
	
}
