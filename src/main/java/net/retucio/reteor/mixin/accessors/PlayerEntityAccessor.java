package net.retucio.reteor.mixin.accessors;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public interface PlayerEntityAccessor {

    @Accessor("lastDeathPos")
    Optional<GlobalPos> getLastDeathPos();
}
