package net.retucio.reteor.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import net.retucio.reteor.Reteor;
import net.retucio.reteor.utils.EntityRaycaster;
import net.retucio.reteor.utils.NbtHandler;

public class StrongholdFinder extends Module {

    // mode code in mixins and utils
    // credits to goby56 for actually making this lmao

    public StrongholdFinder() {
        super(Reteor.CATEGORY, "Stronghold Finder", "Calculates the approximate coordinates of the nearest " +
            "stronghold with the intersection of the paths of two ender eyes. To get started, throw an eye of ender while" +
            "looking down.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        EntityRaycaster.tick(mc);
        NbtHandler.tick(mc);
    }
}
