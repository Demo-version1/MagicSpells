package net.demo.magicSpells;

import org.bukkit.plugin.java.JavaPlugin;

public final class MagicSpells extends JavaPlugin {

    private static MagicSpells instance;

    @Override
    public void onEnable() {
        getLogger().info("Plugin started!!!");
        getServer().getPluginManager().registerEvents(new PlayerHandler(),this);
    }

    @Override
    public void onDisable() {

    }

    public static MagicSpells getInstance() {
        return instance;
    }
}
