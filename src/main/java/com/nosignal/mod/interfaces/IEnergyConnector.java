package com.nosignal.mod.interfaces;

import net.minecraft.util.math.BlockPos;

public interface IEnergyConnector {
	
	BlockPos getConnection();
	void setConnection(BlockPos pos);
}
