package net.retucio.reteor.modules;/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

import net.retucio.reteor.mixin.accessors.AnvilScreenAccessor;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.retucio.reteor.modules.AnvilFont.CustomFont.CharMap;

public class AnvilFont extends Module {

    SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<FontMode> fontMode = sgGeneral.add(new EnumSetting.Builder<FontMode>()
        .name("font-mode")
        .description("Which font style to use.")
        .defaultValue(FontMode.Uppercase)
        .build()
    );

    String text;

    private static final List<CustomFont> fonts = Arrays.asList(

        new CustomFont(
            CharMap.range('!', 0xFF01, 95)),

        new CustomFont( // uppercase
            CharMap.single('a', '\u1D00'), CharMap.single('b', '\u0299'), CharMap.range('c', 0x1d04, 2), CharMap.single('e', '\u1d07'),
            CharMap.single('f', '\ua730'), CharMap.single('g', '\u0262'), CharMap.single('h', '\u029c'), CharMap.single('i', '\u026a'),
            CharMap.range('j', 0x1D0a, 2), CharMap.single('l', '\u029f'), CharMap.single('m', '\u1d0d'), CharMap.single('n', '\u0274'),
            CharMap.single('o', '\u1D0f'), CharMap.single('p', '\u1d29'), CharMap.single('r', '\u0280'), CharMap.single('s', '\ua731'),
            CharMap.range('t', 0x1D1b, 2), CharMap.range('v', 0x1d20, 2), CharMap.single('z', '\u1d22'),
            CharMap.single('A', '\u1D00'), CharMap.single('B', '\u0299'), CharMap.range('C', 0x1d04, 2), CharMap.single('E', '\u1d07'),
            CharMap.single('F', '\ua730'), CharMap.single('G', '\u0262'), CharMap.single('H', '\u029c'), CharMap.single('I', '\u026a'),
            CharMap.range('J', 0x1D0a, 2), CharMap.single('L', '\u029f'), CharMap.single('M', '\u1d0d'), CharMap.single('N', '\u0274'),
            CharMap.single('O', '\u1D0f'), CharMap.single('P', '\u1d29'), CharMap.single('R', '\u0280'), CharMap.single('S', '\ua731'),
            CharMap.range('T', 0x1D1b, 2), CharMap.range('V', 0x1d20, 2), CharMap.single('z', '\u1d22')),

        new CustomFont( // circled
            CharMap.range('1', 0x2461, 9),
            CharMap.range('A', 0x24B6, 26),
            CharMap.range('a', 0x24D0, 26)),

        new CustomFont( // cursed
            CharMap.single('a', '\u039b'), CharMap.single('c', '\u1455'), CharMap.single('e', '\u03A3'), CharMap.single('h', '\u0389'),
            CharMap.single('l', '\u14aa'), CharMap.single('n', '\u041f'), CharMap.single('o', '\u04e8'), CharMap.single('r', '\u042f'),
            CharMap.single('s', '\u01a7'), CharMap.single('t', '\u01ac'), CharMap.single('u', '\u0426'), CharMap.single('w', '\u0429'),
            CharMap.single('A', '\u039b'), CharMap.single('C', '\u1455'), CharMap.single('E', '\u03A3'), CharMap.single('H', '\u0389'),
            CharMap.single('L', '\u14aa'), CharMap.single('N', '\u041f'), CharMap.single('O', '\u04e8'), CharMap.single('R', '\u042f'),
            CharMap.single('S', '\u01a7'), CharMap.single('T', '\u01ac'), CharMap.single('U', '\u0426'), CharMap.single('W', '\u0429')),

        new CustomFont( // greek
            CharMap.single('a', '\u03b1'), CharMap.single('b', '\u0432'), CharMap.single('d', '\u2202'), CharMap.single('e', '\u0454'),
            CharMap.single('f', '\u0192'), CharMap.single('h', '\u043d'), CharMap.single('i', '\u03b9'), CharMap.single('j', '\u05e0'),
            CharMap.single('k', '\u043a'), CharMap.single('l', '\u2113'), CharMap.single('m', '\u043c'), CharMap.single('n', '\u03b7'),
            CharMap.single('o', '\u03c3'), CharMap.single('p', '\u03c1'), CharMap.single('r', '\u044f'), CharMap.single('s', '\u0455'),
            CharMap.single('t', '\u0442'), CharMap.single('u', '\u03c5'), CharMap.single('v', '\u03bd'), CharMap.single('w', '\u03c9'),
            CharMap.single('x', '\u03c7'), CharMap.single('y', '\u0443'),
            CharMap.single('A', '\u03b1'), CharMap.single('B', '\u0432'), CharMap.single('D', '\u2202'), CharMap.single('E', '\u0454'),
            CharMap.single('F', '\u0192'), CharMap.single('H', '\u043d'), CharMap.single('I', '\u03b9'), CharMap.single('J', '\u05e0'),
            CharMap.single('K', '\u043a'), CharMap.single('L', '\u2113'), CharMap.single('M', '\u043c'), CharMap.single('N', '\u03b7'),
            CharMap.single('O', '\u03c3'), CharMap.single('P', '\u03c1'), CharMap.single('R', '\u044f'), CharMap.single('S', '\u0455'),
            CharMap.single('T', '\u0442'), CharMap.single('U', '\u03c5'), CharMap.single('V', '\u03bd'), CharMap.single('W', '\u03c9'),
            CharMap.single('X', '\u03c7'), CharMap.single('Y', '\u0443'))
    );

    public AnvilFont() {
        super(Categories.Misc, "anvil-font", "Lets you customize an item name's font while renaming it on an anvil");
    }

    @EventHandler
    public void onPreTick(TickEvent.Pre event) {

        Screen screen;

        if (mc.currentScreen != null)
            screen = mc.currentScreen;

        else {
            return;
        }

        if (screen instanceof AnvilScreen) {
            TextFieldWidget nameField = ((AnvilScreenAccessor) screen).getNameField();

            if (fontMode.get() != FontMode.Normal) {
                text = fonts.get(fontMode.get().ordinal()).replace(nameField.getText());
            }

            else {
                text = nameField.getText();
            }

            if (!text.equals(nameField.getText())) { ;
                nameField.setText(text);
            }

            ((AnvilScreenAccessor) screen).setName(nameField);
        }
    }

    static class CustomFont {

        private HashMap<Character, Character> allMaps = new HashMap<>();

        public CustomFont(CharMap... maps) {
            for (CharMap map : maps) {
                allMaps.putAll(map.getMap());
            }
        }

        public String replace(String startString) {
            for (Entry<Character, Character> e : allMaps.entrySet()) {
                startString = startString.replace(e.getKey(), e.getValue());
            }

            return startString;
        }

        static class CharMap {

            private HashMap<Character, Character> map = new HashMap<>();

            private CharMap(char... mappings) {
                for (int i = 0; i < mappings.length - 1; i += 2) {
                    map.put(mappings[i], mappings[i + 1]);
                }
            }

            public static CharMap single(char from, char to) {
                return new CharMap(from, to);
            }

            public static CharMap range(char start, int start1, int amount) {
                char[] chars = new char[amount * 2];

                for (int i = 0; i < amount; i++) {
                    chars[i * 2] = (char) (start + i);
                    chars[i * 2 + 1] = (char) (start1 + i);
                }

                return new CharMap(chars);
            }

            public HashMap<Character, Character> getMap() {
                return map;
            }
        }
    }

    private enum FontMode {
        Normal,
        Uppercase,
        Circled,
        Cursed,
        Greek;
    }
}
