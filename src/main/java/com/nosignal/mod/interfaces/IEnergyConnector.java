package com.nosignal.mod.interfaces;

import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface IEnergyConnector {
	
	List<BlockPos> getConnections();
	void addConnection(BlockPos pos);
}
