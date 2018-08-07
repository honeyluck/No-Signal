package com.nosignal.mod.main;

import com.nosignal.mod.client.renderers.RendererConnector;
import com.nosignal.mod.client.renderers.RendererLaser;
import com.nosignal.mod.tileentity.TileEntityConnector;
import com.nosignal.mod.tileentity.TileEntityLaser;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityConnector.class, new RendererConnector());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaser.class, new RendererLaser());
	}

	@Override
	public void init() {
		
	}

	@Override
	public void postInit() {
		
	}
	
}
