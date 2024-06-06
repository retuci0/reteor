package net.retucio.reteor.mixin;

import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.retucio.reteor.modules.AntiInvis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(at = { @At("HEAD") }, method = "isInvisibleTo(Lnet/minecraft/entity/player/PlayerEntity;)Z", cancellable = true)
    private void onIsInvisibleCheck(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (Modules.get().get(AntiInvis.class).isActive()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
    private void onIsInvisible(CallbackInfoReturnable<Boolean> cir) {
        if (Modules.get().get(AntiInvis.class).isActive()) {
            cir.setReturnValue(false);
        }
    }
}
