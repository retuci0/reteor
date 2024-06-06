package net.retucio.reteor.modules;

import net.retucio.reteor.Reteor;

import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.client.gui.screen.DeathScreen;

public class DeathCoords extends Module {

    public DeathCoords() {
        super(Reteor.CATEGORY, "death-coords", "Displays your coordinates when dying.");
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if (mc.player == null) return;

        if (event.screen instanceof DeathScreen) {
            info("You suck! You died at " +
                "X: " + (int) mc.player.getX() + " " +
                "Y: " + (int) mc.player.getY() + " " +
                "Z: " + (int) mc.player.getZ());
        }
    }
}
