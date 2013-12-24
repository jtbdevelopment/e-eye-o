package com.jtbdevelopment.e_eye_o.serialization

import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectList
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper
import groovy.json.JsonBuilder
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 12/19/13
 * Time: 7:58 AM
 */
@Component
@SuppressWarnings("unused")
class JSONIdObjectSerializerGImpl implements JSONIdObjectSerializer, JSONIdObjectConstants {

    @Autowired
    IdObjectReflectionHelper idObjectReflectionHelper

    @Override
    String write(final Object entity) {
        def output;
        switch (entity) {
            case IdObject:
                output = writeObjectToMap((IdObject) entity)
                break;
            case PaginatedIdObjectList:
                output = writePaginatedIdObjectList((PaginatedIdObjectList) entity);
                break
            case Collection:
                output = writeEntities((Collection) entity)
                break;
            default:
                throw new IllegalArgumentException("Do not know how to write " + entity.class.canonicalName)
        }
        return new JsonBuilder(output).toPrettyString()
    }

    private Map writePaginatedIdObjectList(final PaginatedIdObjectList paginatedIdObjectList) {
        return [
                (MORE_FIELD): paginatedIdObjectList.moreAvailable,
                (ENTITIES_FIELD): writeEntities(paginatedIdObjectList.entities),
                (PAGE_SIZE): paginatedIdObjectList.pageSize,
                (CURRENT_PAGE): paginatedIdObjectList.currentPage
        ]
    }

    private List writeEntities(final Collection entities) {
        Object notIdObject = entities.find { !(it in IdObject) }
        if (notIdObject != null) {
            throw new IllegalArgumentException("Not able to serialize a collection containing non-IdObjects like " + notIdObject.class.canonicalName)
        }
        return entities.collect { IdObject it -> writeObjectToMap(it) }
    }

    private Map<String, Object> writeObjectToMap(IdObject entity) {
        def entityInterface = idObjectReflectionHelper.getIdObjectInterfaceForClass(entity.class)
        Map<String, IdObjectFieldSettings> gets = idObjectReflectionHelper.getAllFieldPreferences(entityInterface)
        Map<String, Object> output = [(ENTITY_TYPE_FIELD): (Object) entityInterface.canonicalName]
        gets.findAll { String key, IdObjectFieldSettings value -> value.viewable() }.keySet().each { String key ->
            def value = entity."$key"
            switch (value) {
                case LocalDateTime:
                    LocalDateTime dt = (LocalDateTime) value
                    value = [dt.year, dt.monthOfYear, dt.dayOfMonth, dt.hourOfDay, dt.minuteOfHour, dt.secondOfMinute, dt.millisOfSecond]
                    break;
                case DateTime:
                    value = ((DateTime) value).millis
                    break;
                case IdObject:
                    value = writeSubIdObject((IdObject) value)
                    break;
                case { "imageData" == key || "thumbnailImageData" == key }:
                    value = ((Byte[]) value).encodeBase64().toString()
                    break;
                case Set:
                    value = ((Set<IdObject>) value).collect { IdObject idObject -> writeSubIdObject(idObject) }
                    break;
            }
            output.put(key, value);
        }
        output
    }

    private LinkedHashMap<String, String> writeSubIdObject(final IdObject value) {
        [
                (ENTITY_TYPE_FIELD): idObjectReflectionHelper.getIdObjectInterfaceForClass(value.class).canonicalName,
                (ID_FIELD): value.id
        ]
    }

}
