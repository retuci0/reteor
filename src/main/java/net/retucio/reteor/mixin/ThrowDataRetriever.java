package net.retucio.reteor.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.retucio.reteor.utils.IThrowData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class ThrowDataRetriever implements IThrowData {
    private NbtCompound throwData;

    @Override
    public NbtCompound getThrowData() {
        if(this.throwData == null) {
            this.throwData = new NbtCompound();
        }
        return throwData;
    }
}
