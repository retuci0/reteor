package net.retucio.reteor.modules;

import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import meteordevelopment.meteorclient.utils.render.NametagUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.retucio.reteor.Reteor;
import org.joml.Vector3d;

import java.util.Objects;

public class AntiSpectator extends Module {

    SettingGroup sgGeneral = settings.getDefaultGroup();

    private static final Color BACKGROUND = new Color(0, 0, 0, 75);
    private static final Color TEXT = new Color(255, 255, 255);

    private final Setting<SettingColor> sideColor = sgGeneral.add(new ColorSetting.Builder()
        .name("side-color")
        .defaultValue(new SettingColor(255, 0, 0, 100))
        .build()
    );

    private final Setting<SettingColor> lineColor = sgGeneral.add(new ColorSetting.Builder()
        .name("line-color")
        .defaultValue(Color.RED)
        .build()
    );

    Vector3d pos = new Vector3d();

    public AntiSpectator() {
        super(Reteor.CATEGORY, "Anti Spectator", "Reveals players on spectator mode.");
    }

    @EventHandler
    private void onRender(Render2DEvent event) {
        try {
            if (mc.getNetworkHandler().getWorld() == null) return;
        }

        catch (NullPointerException e) {
            // empty catch block
        }

        for (PlayerEntity player : mc.getNetworkHandler().getWorld().getPlayers()) {
            if (EntityUtils.getGameMode(player) == GameMode.SPECTATOR && player != mc.player) {

                Utils.set(pos, player, event.tickDelta);
                pos.add(0, player.getEyeHeight(player.getPose()) + 0.15, 0);

                if (NametagUtils.to2D(pos, 1)) {
                    if (player.getCustomName() != null) renderNametag(player.getCustomName().getString());
                }
//                Box box = player.getBoundingBox();
//                event.renderer.box(box, sideColor.get(), lineColor.get(), ShapeMode.Both, 0);

//                info("SEPCTATOR MODE: "
//                    + player.getX() + " "
//                    + player.getY() + " "
//                    + player.getZ()
//                );
            }
        }
    }

    private void renderNametag(String name) {
        TextRenderer text = TextRenderer.get();

        NametagUtils.begin(pos);
        text.beginBig();

        double w = text.getWidth(name);

        double x = -w / 2;
        double y = -text.getHeight();

        Renderer2D.COLOR.begin();
        Renderer2D.COLOR.quad(x - 1, y - 1, w + 2, text.getHeight() + 2, BACKGROUND);
        Renderer2D.COLOR.render(null);

        text.render(name, x, y, TEXT);

        text.end();
        NametagUtils.end();
    }
}
