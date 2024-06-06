package net.retucio.reteor.events;

import net.minecraft.util.math.Vec3d;

public class DeathEvent {

    private static final DeathEvent INSTANCE = new DeathEvent();

    Vec3d coords;

    public static DeathEvent get(Vec3d coords) {
        INSTANCE.coords = coords;

        return INSTANCE;
    }
}
