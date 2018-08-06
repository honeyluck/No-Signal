package com.nosignal.mod.util.common;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class SightUtils {


    /**
     * Method that detects whether a tile is the the view sight of viewer
     *
     * @param viewer The viewer entity
     * @param tile   The tile being watched by viewer
     */
    public static boolean isInSightTile(EntityLivingBase viewer, TileEntity tile) {
        double dx = tile.getPos().getX() - viewer.posX;
        double dz;
        for (dz = tile.getPos().getX() - viewer.posZ; dx * dx + dz * dz < 1.0E-4D; dz = (Math.random() - Math.random()) * 0.01D) {
            dx = (Math.random() - Math.random()) * 0.01D;
        }
        while (viewer.rotationYaw > 360) {
            viewer.rotationYaw -= 360;
        }
        while (viewer.rotationYaw < -360) {
            viewer.rotationYaw += 360;
        }
        float yaw = (float) (Math.atan2(dz, dx) * 180.0D / Math.PI) - viewer.rotationYaw;
        yaw = yaw - 90;
        while (yaw < -180) {
            yaw += 360;
        }
        while (yaw >= 180) {
            yaw -= 360;
        }

        return yaw < 60 && yaw > -60;
    }

    public static boolean isInFrontOfEntity(Entity entity, Entity target) {
        Vec3d vec3d = target.getPositionVector();
        Vec3d vec3d1 = entity.getLook(1.0F);
        Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(entity.posX, entity.posY, entity.posZ)).normalize();
        vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);
        return vec3d2.dotProduct(vec3d1) < 0.0;
    }

    public static boolean viewBlocked(EntityLivingBase viewer, EntityLivingBase angel) {
        AxisAlignedBB vB = viewer.getEntityBoundingBox();
        AxisAlignedBB aB = angel.getEntityBoundingBox();
        Vec3d[] viewerPoints = {new Vec3d(vB.minX, vB.minY, vB.minZ), new Vec3d(vB.minX, vB.minY, vB.maxZ), new Vec3d(vB.minX, vB.maxY, vB.minZ), new Vec3d(vB.minX, vB.maxY, vB.maxZ), new Vec3d(vB.maxX, vB.maxY, vB.minZ), new Vec3d(vB.maxX, vB.maxY, vB.maxZ), new Vec3d(vB.maxX, vB.minY, vB.maxZ), new Vec3d(vB.maxX, vB.minY, vB.minZ),};
        Vec3d[] angelPoints = {new Vec3d(aB.minX, aB.minY, aB.minZ), new Vec3d(aB.minX, aB.minY, aB.maxZ), new Vec3d(aB.minX, aB.maxY, aB.minZ), new Vec3d(aB.minX, aB.maxY, aB.maxZ), new Vec3d(aB.maxX, aB.maxY, aB.minZ), new Vec3d(aB.maxX, aB.maxY, aB.maxZ), new Vec3d(aB.maxX, aB.minY, aB.maxZ), new Vec3d(aB.maxX, aB.minY, aB.minZ),};

        for (int i = 0; i < viewerPoints.length; i++) {
            if (viewer.world.rayTraceBlocks(viewerPoints[i], angelPoints[i], false, true, false) == null) return false;
        }
        return true;
    }

}
