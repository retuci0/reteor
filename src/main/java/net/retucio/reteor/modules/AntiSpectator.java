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

    public AntiSpectator() {
        super(Reteor.CATEGORY, "anti-spectator", "Reveals players on spectator mode.");
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (mc.world == null) return;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (!player.isInCreativeMode() && player.noClip && !player.isAttackable()) {
                Box box = player.getBoundingBox();
                event.renderer.box(box, sideColor.get(), lineColor.get(), ShapeMode.Both, 0);

                info("SEPCTATOR MODE: "
                    + player.getX() + " "
                    + player.getY() + " "
                    + player.getZ()
                );
            }


//            if (player.isSpectator()) {
//                event.renderer.box(player.getBoundingBox(), sideColor.get(), lineColor.get(), ShapeMode.Lines, 0);
//                Box bb = new Box(player.getBlockPos());
//                event.renderer.box(bb, sideColor.get(), lineColor.get(), ShapeMode.Both, 0);
//                System.out.println(serverPlayer.interactionManager.getGameMode());
//            }
        }
    }
}
