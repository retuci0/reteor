/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package net.retucio.reteor.modules;

import net.retucio.reteor.Reteor;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;

public class PortalInvincibility extends Module {

    public PortalInvincibility() {
        super(Reteor.CATEGORY, "Portal Invincibility", "Cancels teleportation confirmation packets to make you stay in the portal, hence making you invincibile.");
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (event.packet instanceof TeleportConfirmC2SPacket) event.cancel();
    }
}
