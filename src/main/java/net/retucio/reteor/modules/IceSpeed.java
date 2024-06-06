package net.retucio.reteor.modules;

/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

import net.retucio.reteor.mixin.accessors.AbstractBlockAccessor;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.Arrays;

public class IceSpeed extends Module {

    SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> slipperiness = sgGeneral.add(new DoubleSetting.Builder()
        .name("slipperiness")
        .description("Really low and really high values make you very speedy, while medium values make you slower.")
        .min(0.1)
        .sliderMax(2)
        .defaultValue(0.4)
        .build()
    );

    public IceSpeed() {
        super(Categories.Movement, "ice-speed", "Modifies ice's slipperiness to make you speedy.");
    }

    @EventHandler
    private void onPreTick(TickEvent.Post event) {
        if (mc.player == null) return;
        setSlipperiness(slipperiness.get().floatValue());
    }

    @Override
    public void onDeactivate() {
        if (mc.player == null) return;
        setSlipperiness(0.98f);
    }

    private void setSlipperiness(float slipperiness) {
        for (Block block: Arrays.asList(Blocks.ICE, Blocks.PACKED_ICE, Blocks.FROSTED_ICE, Blocks.BLUE_ICE)) {
            ((AbstractBlockAccessor) block).setSlipperiness(slipperiness);
        }
    }
}
