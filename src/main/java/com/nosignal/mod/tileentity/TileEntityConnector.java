package com.nosignal.mod.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nosignal.mod.interfaces.IEnergyConnector;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityConnector extends TileEntity implements IEnergyConnector, ITickable, IEnergyStorage {
	
	public List<BlockPos> connectionPoints = new ArrayList<>();
	public static final int MAX_POWER = 256;
	public int power = 0;
	
	public TileEntityConnector() {
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagList list = tag.getTagList(NBT.CONNECTION_POINT, Constants.NBT.TAG_LONG);
		for(NBTBase base : list) {
			this.connectionPoints.add(BlockPos.fromLong(((NBTTagLong)base).getLong()));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		NBTTagList list = new NBTTagList();
		for(BlockPos pos : this.connectionPoints) {
			list.appendTag(new NBTTagLong(pos.toLong()));
		}
		tag.setTag(NBT.CONNECTION_POINT, list);
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
			if(!world.isRemote && world.getWorldTime() % 10 == 0) {
				for(EntityPlayerMP player : world.getEntitiesWithinAABB(EntityPlayerMP.class, Block.FULL_BLOCK_AABB.offset(this.getPos()).grow(40))) {
					player.connection.sendPacket(getUpdatePacket());
				}
			}
		}
		
		for (EnumFacing f : EnumFacing.values()) {
			TileEntity te = world.getTileEntity(getPos().offset(f));
			if (te != null && te instanceof IEnergyStorage) {
				System.out.println("POWER!");
			}
		}
		
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return 10;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return 10;
	}
	
	@Override
	public int getEnergyStored() {
		return 10;
	}
	
	@Override
	public int getMaxEnergyStored() {
		return MAX_POWER;
	}
	
	@Override
	public boolean canExtract() {
		return true;
	}
	
	@Override
	public boolean canReceive() {
		return true;
	}
	
}
