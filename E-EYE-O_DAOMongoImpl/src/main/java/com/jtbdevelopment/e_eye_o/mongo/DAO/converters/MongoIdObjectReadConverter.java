package com.jtbdevelopment.e_eye_o.mongo.DAO.converters;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelper;
import com.mongodb.BasicDBObject;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Date: 1/21/14
 * Time: 5:51 PM
 */
@Component
public class MongoIdObjectReadConverter<T extends IdObject> implements Converter<BasicDBObject, T> {
    @Autowired
    private IdObjectFactory idObjectFactory;
    @Autowired
    private IdObjectReflectionHelper idObjectReflectionHelper;

    @Override
    public T convert(final BasicDBObject source) {
        if (source == null) {
            return null;
        }
        try {
            String type = source.getString("class");

            T entity = idObjectFactory.newIdObject((Class<T>) Class.forName(type));

            Map<String, Method> sets = idObjectReflectionHelper.getAllSetMethods(entity.getClass());
            entity.setId(source.getObjectId("_id").toStringMongod());
            entity.setModificationTimestamp(new DateTime(
                            source.getDate("modificationTimestamp"))
            );

            Class<? extends IdObject> idObjectInterface = idObjectReflectionHelper.getIdObjectInterfaceForClass(entity.getClass());
            BasicDBObject unwrapped = (BasicDBObject) source.get(idObjectInterface.getSimpleName());
            for (String key : unwrapped.keySet()) {
                if (sets.containsKey(key)) {
                    Method set = sets.get(key);
                    Class paramType = set.getParameterTypes()[0];
                    if (IdObject.class.isAssignableFrom(paramType)) {
                        set.invoke(entity, convert((BasicDBObject) unwrapped.get(key)));
                    } else if (DateTime.class.isAssignableFrom(paramType)) {
                        set.invoke(entity, new DateTime(unwrapped.getDate(key)));
                    } else if (LocalDateTime.class.isAssignableFrom(paramType)) {
                        set.invoke(entity, new LocalDateTime(unwrapped.getDate(key)));
                    } else if (LocalDate.class.isAssignableFrom(paramType)) {
                        set.invoke(entity, new LocalDate(unwrapped.getDate(key)));
                    } else if (Map.class.isAssignableFrom(paramType)) {
                        // presume settings
                        Map<String, String> settings = new HashMap<>();
                        BasicDBObject fromDB = (BasicDBObject) unwrapped.get("settings");
                        for (Map.Entry entry : fromDB.entrySet()) {
                            String mapKey = entry.getKey().toString().replace("WASDOT", ".");
                            settings.put(mapKey, entry.getValue().toString());
                        }
                        set.invoke(entity, settings);
                    } else if (Set.class.isAssignableFrom(paramType)) {
                        Set converted = new HashSet();
                        List<BasicDBObject> objects = (List<BasicDBObject>) unwrapped.get(key);
                        for (BasicDBObject dbObject : objects) {
                            converted.add(convert(dbObject));
                        }
                        set.invoke(entity, converted);
                    } else if (Enum.class.isAssignableFrom(paramType)) {
                        set.invoke(entity, Enum.valueOf(paramType, (String) unwrapped.get(key)));
                    } else {
                        set.invoke(entity, unwrapped.get(key));
                    }
                }
            }
            return entity;
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed", e);
        }
    }
}
