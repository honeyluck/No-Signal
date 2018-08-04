package com.nosignal.mod.main;

import com.nosignal.mod.client.renderers.RendererConnector;
import com.nosignal.mod.tileentity.TileEntityConnector;

import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityConnector.class, new RendererConnector());
	}

	@Override
	public void init() {
		
	}

	@Override
	public void postInit() {
		
	}
	
}
