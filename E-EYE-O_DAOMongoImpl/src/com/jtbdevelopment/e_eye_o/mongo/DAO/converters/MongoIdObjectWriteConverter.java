package com.jtbdevelopment.e_eye_o.mongo.DAO.converters;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Date: 1/21/14
 * Time: 5:51 PM
 */
@Component
public class MongoIdObjectWriteConverter implements Converter<IdObject, DBObject> {
    @Autowired
    IdObjectReflectionHelper idObjectReflectionHelper;

    @Override
    public DBObject convert(final IdObject source) {
        try {
            Class<? extends IdObject> idObjectInterface = idObjectReflectionHelper.getIdObjectInterfaceForClass(source.getClass());
            BasicDBObject wrapper = new BasicDBObject("class", idObjectInterface.getCanonicalName());

            Map<String, Method> gets = idObjectReflectionHelper.getAllGetMethods(source.getClass());
            wrapper.put("_id", new ObjectId(source.getId()));
            wrapper.put("modificationTimestamp", source.getModificationTimestamp().toDate());
            if (source instanceof AppUserOwnedObject) {
                wrapper.put("appUserId", new ObjectId(((AppUserOwnedObject) source).getAppUser().getId()));
            }
            BasicDBObject dbObject = new BasicDBObject();
            wrapper.put(idObjectInterface.getSimpleName(), dbObject);
            for (Map.Entry<String, Method> get : gets.entrySet()) {
                Method method = get.getValue();
                String key = get.getKey();
                Class<?> returnType = method.getReturnType();
                if (IdObject.class.isAssignableFrom(returnType)) {
                    dbObject.put(key, convert((IdObject) method.invoke(source)));
                } else if (DateTime.class.isAssignableFrom(returnType)) {
                    DateTime dateTime = (DateTime) method.invoke(source);
                    dbObject.put(key, dateTime.toDate());
                } else if (LocalDateTime.class.isAssignableFrom(returnType)) {
                    LocalDateTime localDateTime = (LocalDateTime) method.invoke(source);
                    dbObject.put(key, localDateTime.toDate());
                } else if (LocalDate.class.isAssignableFrom(returnType)) {
                    LocalDate localDate = (LocalDate) method.invoke(source);
                    dbObject.put(key, localDate.toDate());
                } else if (Map.class.isAssignableFrom(returnType)) {
                    //  Presume Settings
                    BasicDBObject settings = new BasicDBObject();
                    for (Map.Entry<String, String> entry : ((Map<String, String>) method.invoke(source)).entrySet()) {
                        String mapKey = entry.getKey().replace(".", "WASDOT");
                        settings.put(mapKey, entry.getValue());
                    }
                    dbObject.put(key, settings);
                } else if (Set.class.isAssignableFrom(returnType)) {
                    BasicDBList list = new BasicDBList();
                    for (IdObject idObject : (Set<? extends IdObject>) method.invoke(source)) {
                        list.add(convert(idObject));
                    }
                    dbObject.put(key, list);
                } else if (Enum.class.isAssignableFrom(returnType)) {
                    dbObject.put(key, method.invoke(source).toString());
                } else {
                    dbObject.put(key, method.invoke(source));
                }
            }
            return wrapper;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed", e);
        }
    }
}
