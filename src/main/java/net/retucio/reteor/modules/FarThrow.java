package net.retucio.reteor.modules;/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.util.Hand;
import net.retucio.reteor.Reteor;

import java.util.Arrays;

public class FarThrow extends Module {

    SettingGroup sgGeneral = settings.getDefaultGroup();
    SettingGroup sgItems = settings.createGroup("Items");

    private final Setting<Integer> spoof = sgGeneral.add(new IntSetting.Builder()
        .name("spoof")
        .description("Spoof amount")
        .range(0, 500)
        .sliderRange(0, 500)
        .defaultValue(70)
        .build()
    );


    private final Setting<Boolean> arrows = sgItems.add(new BoolSetting.Builder()
        .name("Arrows")
        .description("Arrows?")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> pots = sgItems.add(new BoolSetting.Builder()
        .name("Potions")
        .description("Fortnite poties")
        .defaultValue(true)
        .build()
    );

    private final Setting<EnderPearlMode> pearls = sgItems.add(new EnumSetting.Builder<EnderPearlMode>()
        .name("Ender Pearls")
        .description("Black man pearls")
        .defaultValue(EnderPearlMode.On)
        .build()
    );

    private final Setting<Boolean> eggsAndBalls = sgItems.add(new BoolSetting.Builder()
        .name("Eggs and Balls")
        .description("Testicles.")
        .defaultValue(true)
        .build()
    );

    public FarThrow() {
        super(Reteor.CATEGORY, "Far Throw", "Launches projectiles further away.");
    }

    @EventHandler
    public void onSendPacket(PacketEvent.Send event) {
        if (mc.player == null) return;

        if (event.packet instanceof PlayerActionC2SPacket packet && arrows.get() && mc.player.getActiveItem().getItem() == Items.BOW) {

            if (packet.getAction() == PlayerActionC2SPacket.Action.RELEASE_USE_ITEM) {
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));

                for (int i = 0; i < spoof.get(); i++) {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(
                        mc.player.getX(), mc.player.getY() + 1e-10, mc.player.getZ(),
                        mc.player.getYaw(), mc.player.getPitch(),
                        false)
                    );

                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(
                        mc.player.getX(), mc.player.getY(), mc.player.getZ(),
                        mc.player.getYaw(), mc.player.getPitch(),
                        true)
                    );
                }
            }
        }
        if (event.packet instanceof PlayerInteractItemC2SPacket packet) {

            Item item = packet.getHand() == Hand.MAIN_HAND ? mc.player.getMainHandStack().getItem() : mc.player.getOffHandStack().getItem();

//            Item item = mc.player.getHeldItem(packet.getHand()).getItem();

            boolean condition1 = (pearls.get().equals(EnderPearlMode.On) || pearls.get().equals(EnderPearlMode.OnShift) && mc.player.input.sneaking)
                && item == Items.ENDER_PEARL;
            boolean condition2 = pots.get() && Arrays.asList(Items.LINGERING_POTION, Items.SPLASH_POTION, Items.EXPERIENCE_BOTTLE).contains(item);
            boolean condition3 = eggsAndBalls.get() && Arrays.asList(Items.EGG, Items.SNOWBALL).contains(item);

            if (condition1 || condition2 || condition3) {
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));

                for (int i = 0; i < spoof.get(); i++) {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(
                        mc.player.getX(), mc.player.getY() + 1e-10, mc.player.getZ(),
                        mc.player.getYaw(), mc.player.getPitch(),
                        false)
                    );

                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(
                        mc.player.getX(), mc.player.getY(), mc.player.getZ(),
                        mc.player.getYaw(), mc.player.getPitch(),
                        true)
                    );
                }
            }
        }
    }

    private enum EnderPearlMode {
        On,
        Off,
        OnShift;
    }
}
