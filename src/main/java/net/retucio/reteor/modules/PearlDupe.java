package net.retucio.reteor.modules;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.retucio.reteor.Reteor;

public class PearlDupe extends Module {

    SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> command = sgGeneral.add(new StringSetting.Builder()
        .name("command")
        .description("Command to send.")
        .defaultValue("kill")
        .build()
    );

    public PearlDupe() {
        super(Reteor.CATEGORY, "pearl-dupe", "Sends certain command after a teleport position packet is received.");
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (mc.player == null) return;

        if (event.packet instanceof PlayerPositionLookS2CPacket packet) {
            if (mc.player.age < 20) return;
            if (mc.player.squaredDistanceTo(packet.getX(), packet.getY(), packet.getZ()) > 16.0D) sendCommand(mc.player);

        }
    }

    private void sendCommand(ClientPlayerEntity player) {
        player.networkHandler.sendChatCommand(command.get());
        System.out.println("Pearl duped");
    }
}
