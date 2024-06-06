/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package net.retucio.reteor.modules;

import net.retucio.reteor.Reteor;

import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.render.NametagUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

import org.joml.Vector3d;

public class EntityNametags extends Module {
    private static final Color BACKGROUND = new Color(0, 0, 0, 75);
    private static final Color TEXT = new Color(255, 255, 255);

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgPos = settings.createGroup("Position");

    private final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder()
        .name("scale")
        .description("The scale of the text.")
        .defaultValue(1.3)
        .min(0)
        .build()
    );

    private final Setting<Double> posX = sgPos.add(new DoubleSetting.Builder()
        .name("x")
        .description("X")
        .range(0, 1)
        .sliderRange(0, 100)
        .defaultValue(0)
        .build()
    );

    private final Setting<Double> posY = sgPos.add(new DoubleSetting.Builder()
        .name("y")
        .description("Y")
        .range(0, 1)
        .sliderRange(0, 100)
        .defaultValue(0.15)
        .build()
    );

    private final Setting<Double> posZ = sgPos.add(new DoubleSetting.Builder()
        .name("z")
        .description("Z")
        .range(0, 1)
        .sliderRange(0, 100)
        .defaultValue(0)
        .build()
    );

    private final Vector3d pos = new Vector3d();

    public EntityNametags() {
        super(Reteor.CATEGORY, "entity-nametags", "Always renders entities' custom name labels, no matter whether your hovering it or not.");
    }

    @EventHandler
    private void onRender2D(Render2DEvent event) {
        if (mc.world == null) return;

        for (Entity entity : mc.world.getEntities()) {
            Text entityName;

            entityName = entity.getCustomName();

            if (entityName != null) {
                Utils.set(pos, entity, event.tickDelta);
                pos.add(posX.get(), entity.getEyeHeight(entity.getPose()) + posY.get(), posZ.get());

                if (NametagUtils.to2D(pos, scale.get())) {
                    renderNametag(entityName.getString());
                }
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
