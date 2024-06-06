package net.retucio.reteor.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.Target;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermanEntity;

import java.util.ArrayList;
import java.util.List;

public class Racist extends Module {

    private int lookTimer;
    private final List<Entity> lookedEndermen = new ArrayList<>();

    public Racist() {
        super(Categories.Player, "racist", "Detroit moment.");
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if (mc.world == null || mc.player == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof EndermanEntity enderman)) continue;

            if (shouldLookAt(enderman)) {
                lookAtEnderman(enderman);

                lookedEndermen.add(enderman);
                lookTimer = 0;
                return;
            }
        }

        lookTimer++;
    }

    private boolean shouldLookAt(EndermanEntity enderman) {
        return !(lookedEndermen.contains(enderman)) && lookTimer >= 10;
    }

    private void lookAtEnderman(EndermanEntity enderman) {
        double yaw = Rotations.getYaw(enderman.getPos());
        double pitch = Rotations.getPitch(enderman, Target.Head);

        mc.player.setYaw((float) yaw);
        mc.player.setPitch((float) pitch);
    }

    @Override
    public void onDeactivate() {
        lookedEndermen.clear();
    }
}
