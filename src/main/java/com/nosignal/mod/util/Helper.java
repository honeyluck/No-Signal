package com.nosignal.mod.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Helper {
	
	public static NBTTagCompound getItemTag(ItemStack stack) {
		if(stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		return stack.getTagCompound();
	}
	
	public static String formatBlockPos(BlockPos pos) {
		if(!pos.equals(BlockPos.ORIGIN)) {
			return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
		}
		return "None";
	}
	
	public static Vec3d vec3iToVec3d(Vec3i pos) {
		return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public static Vec3d getVecForFace(EnumFacing face) {
		switch (face) {
			case NORTH:
				return new Vec3d(0, 0, -1);
			case EAST:
				return new Vec3d(1, 0, 0);
			case SOUTH:
				return new Vec3d(0, 0, 1);
			default:
				return new Vec3d(-1, 0, 0);
		}
	}
}
