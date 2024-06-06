package net.retucio.reteor;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.retucio.reteor.modules.*;
import meteordevelopment.meteorclient.utils.misc.Version;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class Reteor extends MeteorAddon {

    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Reteor");
    public static final HudGroup HUD_GROUP = new HudGroup("Reteor");

    public static Version VERSION;
    public static ModMetadata MOD_META;

    @Override
    public void onInitialize() {
        LOG.info("Initialized Reteor Addon");

        MOD_META = FabricLoader.getInstance().getModContainer("reteor").orElseThrow().getMetadata();
        String versionString = MOD_META.getVersion().getFriendlyString();
        if (versionString.contains("-")) versionString = versionString.split("-")[0];
        if (versionString.equals("${version}")) versionString = "0.0.0";

        VERSION = new Version(versionString);

        addModules();
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "net.retucio.reteor";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("retuci0", "reteor");
    }

    private void addModules() {
        add(new AntiInvis());
        add(new AntiSpectator());
        add(new AnvilFont());
        add(new AutoTorch());
        add(new DeathCoords());
        add(new Dolphin());
        add(new EntityDesync());
        add(new EntityNametags());
        add(new FarThrow());
        add(new GhostMode());
        add(new HitboxDesync());
        add(new IceSpeed());
        add(new JumpDelay());
        add(new PearlDupe());
        add(new PortalInvincibility());
        add(new ProjectileExploit());
        add(new Racist());
    }

    private void add(Module module) {
        Modules.get().add(module);
    }

    @EventHandler
    private void setTitle(TickEvent.Post event) {
        mc.getWindow().setTitle("Reteor v" + VERSION.toString());
    }
}
