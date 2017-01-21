package net.rhian.agathe.kit;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import net.rhian.agathe.Agathe;
import net.rhian.agathe.exception.KitException;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.serial.KitSerializer;

public class KitHandler {

    private final IPlayer player;
    private final YamlConfiguration config;
    private final File configFile;
    private final KitSerializer serializer = new KitSerializer();

    public KitHandler(IPlayer player) {
        this.player = player;

        try {
            File players = new File(Agathe.getPlugin().getDataFolder().getPath() + File.separator + "players");
            if (!players.exists()) {
                players.mkdir();
            }
            File uu = new File(Agathe.getPlugin().getDataFolder().getPath() + File.separator + "players" +
                    File.separator + player.getUniqueId());
            if (!uu.exists()) {
                uu.mkdir();
            }
            File f = new File(Agathe.getPlugin().getDataFolder().getPath() + File.separator +
                    "players" + File.separator + player.getUniqueId() + File.separator + "kit.yml");
            if (!f.exists()) {
                f.createNewFile();
            }
            this.configFile = f;
            config = YamlConfiguration.loadConfiguration(f);
        } catch (IOException ex) {
            throw new KitException("Could not save kit for " + player.getName(), ex);
        }
    }

    public void save(Kit kit) {
        config.set("kits."+kit.getName(), serializer.toString(kit));
        saveConfig();
    }

    public Kit load(String name) {
        return serializer.fromString(config.getString("kits."+name));
    }

    public boolean hasKitSaved(String name){
        return config.contains("kits."+name);
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException ex) {
            throw new KitException("Could not save kit config to file", ex);
        }
    }

}
