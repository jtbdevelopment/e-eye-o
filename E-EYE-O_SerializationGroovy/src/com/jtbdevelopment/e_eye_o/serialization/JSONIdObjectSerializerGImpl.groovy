package com.jtbdevelopment.e_eye_o.serialization

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO
import com.jtbdevelopment.e_eye_o.DAO.helpers.PhotoHelper
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory
import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectList
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.codehaus.groovy.runtime.EncodingGroovyMethods
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 12/19/13
 * Time: 7:58 AM
 */
@Component
class JSONIdObjectSerializerGImpl implements JSONIdObjectSerializer {

    @Autowired
    IdObjectReflectionHelper idObjectReflectionHelper

    @Autowired
    IdObjectFactory idObjectFactory

    @Autowired
    ReadOnlyDAO readOnlyDAO

    @Autowired
    PhotoHelper photoHelper;

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

    @Override
    public <T> T readAsObjects(final String input) {
        def parsed = readRaw(input);
        def returnValue;
        switch (parsed) {
            case { isPaginatedMap(parsed) }:
                returnValue = [(MORE_FIELD): parsed[MORE_FIELD]]
                returnValue.put(ENTITIES_FIELD, readList(parsed[ENTITIES_FIELD]))
                break;
            case Map:
                returnValue = readMap(parsed)
                break;
            case List:
                returnValue = readList(parsed)
                break;
            default:
                throw new RuntimeException("Unknown object type " + parsed.class.canonicalName)
        }
        return returnValue
    }

    @Override
    public <T> T readRaw(final String input) {
        return (T) new JsonSlurper().parseText(input)
    }

    private boolean isPaginatedMap(final input) {
        return input in Map && input.containsKey(MORE_FIELD) && input.containsKey(ENTITIES_FIELD)
    }

    private <T extends IdObject> T readMap(final Map<String, Object> map) {
        String entityType = map[ENTITY_TYPE_FIELD];
        T returnValue = idObjectFactory.newIdObject((Class<T>) Class.forName(entityType))
        map.findAll { it.key != ENTITY_TYPE_FIELD && it.key != "thumbnailImageData" }.each {
            String key, Object value ->
                Class fieldType = returnValue.metaClass.properties.find { it.name == key }.type

                switch (value) {
                    case { LocalDateTime == fieldType }:
                        returnValue."$key" = new LocalDateTime(value[0], value[1], value[2], value[3], value[4], value[5], value[6])
                        break;
                    case { DateTime == fieldType }:
                        DateTime time = new DateTime((Long) value)
                        returnValue."$key" = time
                        break;
                    case { key.equals("imageData") }:
                        photoHelper.setPhotoImages(returnValue, EncodingGroovyMethods.decodeBase64(value));
                        break;
                    case { String == fieldType && key.equals("mimeType") }:
                        returnValue."$key" = value
                        photoHelper.reprocessForMimeType(returnValue)
                        break;
                    case { Set.isAssignableFrom(fieldType) }:
                        def values = [] as Set;
                        value.each { values.add(readSubObject(it)) }
                        returnValue."$key" = values
                        break;
                    case Map:
                        returnValue."$key" = readSubObject(value);
                        break;
                    default:
                        returnValue."$key" = value
                }
        }

        return returnValue
    }

    private IdObject readSubObject(value) {
        String id = value.get(ID_FIELD)
        Class<? extends IdObject> type = Class.forName((String) value.get(ENTITY_TYPE_FIELD));
        IdObject loaded = readOnlyDAO.get(type, id)
        loaded
    }

    private List<? extends IdObject> readList(final List<Map<String, Object>> values) {
        values.collect({ readMap(it) })
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
