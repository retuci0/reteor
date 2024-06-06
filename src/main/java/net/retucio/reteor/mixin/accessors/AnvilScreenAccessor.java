package net.retucio.reteor.mixin.accessors;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnvilScreen.class)
public interface AnvilScreenAccessor {

    @Accessor
    TextFieldWidget getNameField();

    @Accessor("nameField")
    public void setName(TextFieldWidget nameField);
}
