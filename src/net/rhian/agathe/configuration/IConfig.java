package net.rhian.agathe.configuration;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import net.rhian.agathe.configuration.annotations.ConfigData;
import net.rhian.agathe.configuration.annotations.ConfigSerializer;
import net.rhian.agathe.serial.LocationSerializer;

@Getter
@Setter
public class IConfig extends Configuration {

    public IConfig(Plugin plugin) {
        super(plugin);
        load();
        save();
    }

    @ConfigData("spawn")
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location spawn = Bukkit.getWorld("world").getSpawnLocation();
    @ConfigData("kitbuilder.spawn")
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location kitBuilderSpawn = Bukkit.getWorld("world").getSpawnLocation();

    @ConfigData("scoreboard.title")
    private String scoreboardTitle = "&6&lPractice";
    @ConfigData("scoreboard.match.duration")
    private String scoreboardMatchDuration = "&eDuration&7: &a";
    @ConfigData("scoreboard.match.pearl")
    private String scoreboardMatchPearl = "&eEnderpearl&7: &a{COOLDOWN}";
}
