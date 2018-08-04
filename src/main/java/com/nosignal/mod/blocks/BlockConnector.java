package com.nosignal.mod.blocks;

import com.nosignal.mod.main.NoSignal;
import com.nosignal.mod.tileentity.TileEntityConnector;

import net.minecraft.block.material.Material;

public class BlockConnector extends BlockTileBase {

	public BlockConnector() {
		super(Material.IRON, TileEntityConnector::new);
		this.setCreativeTab(NoSignal.tab);
	}

}
