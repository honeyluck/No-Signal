package com.nosignal.mod.tileentity;

import com.nosignal.mod.blocks.BlockLaser;
import com.nosignal.mod.util.Helper;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class TileEntityLaser extends TileEntity implements ITickable {

    public boolean isOn = false;
    public int offTicks = 0;

    public TileEntityLaser() {

    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.getPos(), -1, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("on", isOn);
        tag.setInteger("time", offTicks);
        return tag;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        if (world.isRemote) {
            this.isOn = pkt.getNbtCompound().getBoolean("on");
            this.offTicks = pkt.getNbtCompound().getInteger("time");
        }
    }

    @Override
    public void update() {
        EnumFacing face = world.getBlockState(getPos()).getValue(BlockLaser.FACING);
        if (!world.isRemote) {
            Vec3d ray = Helper.getVecForFace(face).scale(7D);
            AxisAlignedBB bb = Block.FULL_BLOCK_AABB.offset(this.getPos().offset(face)).expand(ray.x, ray.y, ray.z);
            if (world.getEntitiesWithinAABB(Entity.class, bb).size() > 0) {
               this.isOn = true;
            }
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return Block.FULL_BLOCK_AABB.grow(7);
    }

}
