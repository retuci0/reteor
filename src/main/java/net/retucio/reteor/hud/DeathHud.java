package net.retucio.reteor.hud;

import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;

import net.minecraft.util.math.BlockPos;

import net.retucio.reteor.Reteor;
import net.retucio.reteor.mixin.accessors.PlayerEntityAccessor;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class DeathHud extends HudElement {

    public static final HudElementInfo<DeathHud> INFO = new HudElementInfo<>(Reteor.HUD_GROUP,
        "death-coords",
        "You last death's coords.",
        DeathHud::new
    );

    public DeathHud() {
        super(INFO);
    }

    @Override
    public void render(HudRenderer renderer) {

        if (mc != null && mc.player != null) {
            BlockPos lastDeathPos = ((PlayerEntityAccessor) mc.player).getLastDeathPos().isPresent()
                ? ((PlayerEntityAccessor) mc.player).getLastDeathPos().get().pos()
                : null;

            String deathDimension = ((PlayerEntityAccessor) mc.player).getLastDeathPos().get()
                .dimension().getValue().toString();

            switch (deathDimension) {
                case "minecraft:overworld" -> deathDimension = "Overworld";
                case "minecraft:the_nether" -> deathDimension = "Nether";
                case "minecraft:the_end" -> deathDimension = "The End";
            }

            if (lastDeathPos != null) {

                String text = "Last Death: "
                    + lastDeathPos.getX() + " "
                    + lastDeathPos.getY() + " "
                    + lastDeathPos.getZ() + " "
                    + "[" + deathDimension + "]";

                setSize(renderer.textWidth(text,true),
                    renderer.textHeight(true));

                renderer.quad(x, y, getWidth(), getHeight(), new Color(0, 0, 0, 0));
                renderer.text(text, x, y, Color.WHITE, true);
            }

            else {
                setInfo(renderer);
            }
        }

        else {
            setInfo(renderer);
        }
    }

    private void setInfo(HudRenderer renderer) {
        setSize(renderer.textWidth("Last Death: X Y Z Dimension", true), renderer.textHeight(true));
        renderer.quad(x, y, getWidth(), getHeight(), Color.LIGHT_GRAY);
        renderer.text("Last Death: X Y Z Dimension", x, y, Color.WHITE, true);
    }
}
