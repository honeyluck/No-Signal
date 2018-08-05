package com.nosignal.mod.tileentity;

import com.nosignal.mod.interfaces.IEnergyConnector;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileEntityConnector extends TileEntity implements IEnergyConnector, ITickable {
	
	public List<BlockPos> connectionPoints = new ArrayList<>();
	
	public TileEntityConnector() {
		
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		String[] points = tag.getString(NBT.CONNECTION_POINT_STR).split("\t");
		for (String point : points) {
		    long l;
		    try {
		        l = Long.parseLong(point.replace("\t", ""));
                this.connectionPoints.add(BlockPos.fromLong(l));
            } catch (NumberFormatException e) {
		        e.printStackTrace();
            }
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		StringBuilder sb = new StringBuilder();
		for (BlockPos pos : connectionPoints){
			sb.append(pos.toLong() + "\t");
		}
		tag.setString(NBT.CONNECTION_POINT_STR, sb.toString());
		return super.writeToNBT(tag);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), -1, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return tag;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		if(world.isRemote) {
			readFromNBT(pkt.getNbtCompound());
		}
	}

	public static class NBT{
		public static final String CONNECTION_POINT = "end_pos";
		public static final String CONNECTION_POINT_STR = "end_pos_str";
	}

	@Override
	public List<BlockPos> getConnections() {
		return connectionPoints;
	}

	@Override
	public void addConnection(BlockPos pos) {
		this.connectionPoints.add(pos);
	}

	@Override
	public void update() {
		if (connectionPoints.size() > 0) {
            Iterator<BlockPos> connections = connectionPoints.iterator();
            while(connections.hasNext()) {
                BlockPos point = connections.next();
                TileEntity te = world.getTileEntity(point);
                if (te == null || !(te instanceof IEnergyConnector)) {
                    connections.remove();
                } else if (te instanceof IEnergyConnector && !((IEnergyConnector) te).getConnections().contains(this.getPos())) {
                    connections.remove();
                }
            }
			if(!world.isRemote) {
				for(EntityPlayerMP player : world.getEntitiesWithinAABB(EntityPlayerMP.class, Block.FULL_BLOCK_AABB.offset(this.getPos()).grow(40))) {
					player.connection.sendPacket(getUpdatePacket());
				}
			}
		}

	}
}
