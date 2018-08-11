package com.nosignal.mod.interfaces;

import java.util.List;

import net.minecraft.util.math.BlockPos;

public interface IEnergyConnector {
	
	List<BlockPos> getConnections();
	void addConnection(BlockPos pos);
}
