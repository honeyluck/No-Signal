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

public class TileEntityConnector extends TileEntity implements IEnergyConnector, ITickable{
	
	public BlockPos connectionPoint = BlockPos.ORIGIN;
	
	public TileEntityConnector() {
		
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.connectionPoint = BlockPos.fromLong(tag.getLong(NBT.CONNECTION_POINT));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setLong(NBT.CONNECTION_POINT, this.connectionPoint.toLong());
		return super.writeToNBT(tag);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), -1, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong(NBT.CONNECTION_POINT, this.connectionPoint.toLong());
		return tag;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		if(world.isRemote) {
			this.connectionPoint = BlockPos.fromLong(pkt.getNbtCompound().getLong(NBT.CONNECTION_POINT));
		}
	}

	public static class NBT{
		public static final String CONNECTION_POINT = "end_pos";
	}

	@Override
	public BlockPos getConnection() {
		return this.connectionPoint;
	}

	@Override
	public void setConnection(BlockPos pos) {
		this.connectionPoint = pos;
		this.markDirty();
	}

	@Override
	public void update() {
		if(!this.connectionPoint.equals(BlockPos.ORIGIN)) {
			TileEntity te = world.getTileEntity(this.getConnection());
			if(te == null || !(te instanceof IEnergyConnector)) {
				this.setConnection(BlockPos.ORIGIN);
			}
			else if(te instanceof IEnergyConnector && !((IEnergyConnector)te).getConnection().equals(this.getPos())){
				this.setConnection(BlockPos.ORIGIN);
			}
			if(!world.isRemote) {
				for(EntityPlayerMP player : world.getEntitiesWithinAABB(EntityPlayerMP.class, Block.FULL_BLOCK_AABB.offset(this.getPos()).grow(40))) {
					player.connection.sendPacket(getUpdatePacket());
				}
			}
		}
	}
}
