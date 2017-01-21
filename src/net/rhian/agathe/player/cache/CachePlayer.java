package net.rhian.agathe.player.cache;


import net.rhian.agathe.database.mongo.AutoMongo;

/**
 * Created by Jonah on 6/11/2015.
 */
public abstract class CachePlayer extends AutoMongo {

    public CachePlayer() {
    }

    public abstract String getName();

    public abstract String getUniqueId();

}
