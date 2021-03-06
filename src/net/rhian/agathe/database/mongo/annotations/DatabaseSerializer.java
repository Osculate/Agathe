package net.rhian.agathe.database.mongo.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.rhian.agathe.configuration.AbstractSerializer;


@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface DatabaseSerializer {

    Class<? extends AbstractSerializer> serializer();

}
