package com.nosignal.mod.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

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
}
