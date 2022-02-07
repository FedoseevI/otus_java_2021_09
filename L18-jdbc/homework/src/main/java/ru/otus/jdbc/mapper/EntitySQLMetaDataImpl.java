package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;
    private final String entityName;
    private final String idFieldName;

    private final static String TEMPLATE_SELECT_ALL = "SELECT %fields% from %tableName%";
    private final static String TEMPLATE_SELECT_BY_ID = "SELECT %fields% from %tableName% where %idField% = ?";
    private final static String TEMPLATE_INSERT = "INSERT into %tableName%(%fields%) values (%valuesMask%)";
    private final static String TEMPLATE_UPDATE = "UPDATE %tableName% SET %setMask% WHERE %idField% = ?";

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        this.entityName = entityClassMetaData.getName();
        this.idFieldName = entityClassMetaData.getIdField().getName();
    }

    private String getAllFieldsString() {
        return entityClassMetaData
                .getAllFields()
                .stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
    }

    private String getAllFieldsWoIdString() {
        return entityClassMetaData
                .getFieldsWithoutId()
                .stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
    }

    private String getInsertFieldsValuesMask() {
        return entityClassMetaData
                .getFieldsWithoutId()
                .stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));
    }

    private String getUpdateFieldsValuesMask() {
        return entityClassMetaData
                .getFieldsWithoutId()
                .stream()
                .map(field -> {
                    return field.getName() + " = ?";
                })
                .collect(Collectors.joining(", "));
    }

    @Override
    public String getSelectAllSql() {
        return TEMPLATE_SELECT_ALL
                .replace("%fields%", getAllFieldsString())
                .replace("%tableName%", entityName);
    }

    @Override
    public String getSelectByIdSql() {
        return TEMPLATE_SELECT_BY_ID
                .replace("%fields%", getAllFieldsString())
                .replace("%tableName%", entityName)
                .replace("%idField%", idFieldName);
    }

    @Override
    public String getInsertSql() {
        return TEMPLATE_INSERT
                .replace("%tableName%", entityName)
                .replace("%fields%", getAllFieldsWoIdString())
                .replace("%valuesMask%", getInsertFieldsValuesMask());
    }

    @Override
    public String getUpdateSql() {
        return TEMPLATE_UPDATE
                .replace("%tableName%", entityName)
                .replace("%idFieldName%", idFieldName)
                .replace("%setMask%", getUpdateFieldsValuesMask());
    }
}
