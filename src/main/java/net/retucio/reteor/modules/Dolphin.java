package net.retucio.reteor.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.movement.Jesus;
import meteordevelopment.orbit.EventHandler;
import net.retucio.reteor.Reteor;

public class Dolphin extends Module {

    public Dolphin() {
        super(Reteor.CATEGORY, "Dolphin", "Makes you swim insanely fast.");
    }

    @Override
    public void onActivate() {
        if (Modules.get().get(Jesus.class).isActive()) {
            info("You can't have both Jesus and Dolphin active!");
            toggle();
        }
    }

    @EventHandler
    public void onPreTick(TickEvent.Pre event) {
        if (mc.player == null) return;

        if (Modules.get().get(Jesus.class).isActive()) {
            info("You can't have both Jesus and Dolphin active!");
            toggle();
        }

        if (mc.player.isTouchingWater()) {
            mc.player.setSwimming(true);

            if (mc.player.isSwimming() && mc.player.isSubmergedInWater()) {
                mc.player.jump();
            }
        }
    }
}
