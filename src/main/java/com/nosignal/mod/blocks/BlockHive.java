package com.nosignal.mod.blocks;

import com.nosignal.mod.main.NoSignal;
import com.nosignal.mod.tileentity.TileEntityHive;
import net.minecraft.block.material.Material;

public class BlockHive extends BlockTileBase {

    public BlockHive() {
        super(Material.WOOD, TileEntityHive::new);
        this.setCreativeTab(NoSignal.beesTabs);
    }
}
