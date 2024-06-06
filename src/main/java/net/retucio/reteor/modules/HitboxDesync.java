/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package net.retucio.reteor.modules;

import meteordevelopment.meteorclient.systems.modules.Module;
import net.retucio.reteor.Reteor;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class HitboxDesync extends Module {

    private static final double MAGIC_OFFSET = 0.200009968835369999878673424677777777777761; // credits to mioclient

    public HitboxDesync() {
        super(Reteor.CATEGORY, "hitbox-desync", "Slightly moves the player to desync it from its hitbox. Useful to avoid taking knockback.");
    }

    @Override
    public void onActivate() {
        if (mc.player == null) return;

        Direction hFacing  = mc.player.getHorizontalFacing();
        Box bb = mc.player.getBoundingBox();

        Vec3d center = bb.getCenter();
        Vec3d offset = new Vec3d(hFacing.getUnitVector());
        Vec3d fin = merge(Vec3d.of(BlockPos.ofFloored(center)).add(.5, 0, .5).add(offset.multiply(MAGIC_OFFSET)), hFacing);

        mc.player.setPosition(
            fin.x == 0 ? mc.player.getX() : fin.x,
            mc.player.getY(),
            fin.z == 0 ? mc.player.getZ() : fin.z
        );

    }

    private Vec3d merge(Vec3d vec, Direction facing) {
        return new Vec3d(vec.x * Math.abs(facing.getUnitVector().x()),
            vec.y * Math.abs(facing.getUnitVector().y()),
            vec.z * Math.abs(facing.getUnitVector().z())
        );
    }
}
