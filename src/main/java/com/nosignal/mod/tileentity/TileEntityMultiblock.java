package com.nosignal.mod.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityMultiblock extends TileEntity{

    private BlockPos masterPos = BlockPos.ORIGIN;
	
	public TileEntityMultiblock() {}

    public BlockPos getMasterPos() {
        return this.masterPos;
    }

    public void setMasterPos(BlockPos pos) {
        this.masterPos = pos.toImmutable();
        this.markDirty();
    }

}
