/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package net.retucio.reteor.modules;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.retucio.reteor.Reteor;
import net.retucio.reteor.mixin.accessors.LivingEntityAccessor;

import java.util.Arrays;
import java.util.OptionalInt;

/*

Basically, to make it work mount any ridable entity (like a horse) and enable the entity desync module, then disable it.
you will be able to move freely client side, being able to fly by just holding down space, but you'll still be mounting
the horse server-side. You can do some stuff, like moving around, but you can't interact with the world until you sync again.
By interacting with the world I mean placing blocks, breaking them, attacking entities, breeding them, or loading chunks.
To sync again, just die, kill the horse, or press shift to dismount the horse.

Credits to Sepukku client for the 1.12 version.

Btw, I haven't tested everything, like I haven't tried to use a saddle, etc. Feel free to explore how to module works
and to use it on your own client.

*/

public class EntityDesync extends Module {

    SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> noDismountPlugin = sgGeneral.add(new BoolSetting.Builder()
        .name("anti-dismount")
        .description("Prevents server plugin from dismounting you while riding")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> dismountEntity = sgGeneral.add(new BoolSetting.Builder()
        .name("dismount-entity")
        .description("Dismounts the riding entity client-side")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> removeEntity = sgGeneral.add(new BoolSetting.Builder()
        .name("remove-entity")
        .description("Removes the riding entity from the world client-side")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> respawnEntity = sgGeneral.add(new BoolSetting.Builder()
        .name("respawn-entity")
        .description("Forces the riding entity's \"isDead\" value to be false on respawn")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> sendMovementPackets = sgGeneral.add(new BoolSetting.Builder()
        .name("send-move-packets")
        .description("Sends VehicleMoveC2SPacket packets for the riding entity")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> forceOnGround = sgGeneral.add(new BoolSetting.Builder()
        .name("force-on-ground")
        .description("Forces the player's onGround attribute to be true when de-syncing")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> setMountPos = sgGeneral.add(new BoolSetting.Builder()
        .name("set-mount-position")
        .description("Updates the riding entity position & bounding-box client-side")
        .defaultValue(true)
        .build()
    );

    private Entity originalRidingEntity;

    public EntityDesync() {
        super(Reteor.CATEGORY, "Entity Desync", "Dismounts you from an entity client-side");
    }

    @EventHandler
    public void onReceivePacket(PacketEvent.Receive event) {
        if (event.packet instanceof EntityPassengersSetS2CPacket) {
            if (hasOriginalRidingEntity() && mc.world != null) {
                EntityPassengersSetS2CPacket packetSetPassengers = (EntityPassengersSetS2CPacket) event.packet;

                if (originalRidingEntity.equals(mc.world.getEntityById(packetSetPassengers.getId()))) {
                    OptionalInt isPlayerAPassenger = Arrays.stream(packetSetPassengers.getPassengerIds()).filter(value -> mc.world.getEntityById(value) == mc.player).findAny();

                    if (!isPlayerAPassenger.isPresent()) {
                        info("You've been dismounted.");
                        toggle();
                    }
                }
            }
        }

        if (event.packet instanceof EntitiesDestroyS2CPacket packetDestroyEntities) {
            boolean isEntityNull = false;
            for (int id : packetDestroyEntities.getEntityIds()) {
                if (id == originalRidingEntity.getId()) {
                    isEntityNull = true;
                }

                else isEntityNull = false;
            }

            if (isEntityNull) {
                info("The current riding entity is now null.");
            }
        }
    }

    @EventHandler
    public void onSendPacket(PacketEvent.Send event) {
        if (this.noDismountPlugin.get()) {
            if (event.packet instanceof PlayerMoveC2SPacket packet && mc.player != null) {
                event.cancel();

                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket(
                    mc.player.getX(), mc.player.getY(), mc.player.getZ(),
                    mc.player.getYaw(), mc.player.getPitch(),
                    mc.player.isOnGround(),
                    true, true) { // it's supposed to be the packet's attributes instead of the player's ones but who cares right
                    @Override
                    public PacketType<? extends PlayerMoveC2SPacket> getPacketId() {
                        return null;
                    }
                });
            }

            if (event.packet instanceof PlayerMoveC2SPacket && !(event.packet instanceof PlayerMoveC2SPacket.LookAndOnGround))
                event.cancel();
        }
    }

    @EventHandler
    public void onPostTick(TickEvent.Post event) {
        if (mc.world != null && mc.player != null) {
            if (!mc.player.isRiding() && hasOriginalRidingEntity()) {
                if (forceOnGround.get())
                    mc.player.setOnGround(true);

                if (setMountPos.get())
                    originalRidingEntity.setPosition(mc.player.getX(), mc.player.getY(), mc.player.getZ());

                if (sendMovementPackets.get())
                    mc.player.networkHandler.sendPacket(new VehicleMoveC2SPacket(originalRidingEntity));
            }
        }
    }

    @Override
    public void onActivate() {
        originalRidingEntity = null;

        if (mc.player != null && mc.world != null) {
            if (mc.player.getRootVehicle() != mc.player) {
                originalRidingEntity = mc.player.getRootVehicle();

                if (dismountEntity.get()) {
                    mc.player.dismountVehicle();
                    info("Dismounted entity.");
                }

                if (this.removeEntity.get()) {
                    mc.world.removeEntity(originalRidingEntity.getId(), Entity.RemovalReason.DISCARDED);
                    info("Removed entity from world.");
                }
            }

            else {
                info("Please mount an entity before enabling this module.");
                toggle();
            }
        }
    }

    @Override
    public void onDeactivate() {
        if (mc.player == null || mc.world == null) return;

        if (hasOriginalRidingEntity()) {
            if (respawnEntity.get())
                ((LivingEntityAccessor) originalRidingEntity).setDead(false);

            if (!mc.player.isRiding()) {
                mc.world.spawnEntity(originalRidingEntity);
                mc.player.startRiding(originalRidingEntity, true);
                info("Spawned and mounted original entity.");
            }

            originalRidingEntity = null;
        }
    }

    private boolean hasOriginalRidingEntity() {
        return originalRidingEntity != null;
    }
}
