package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    private T getEntityClassObjectFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                var newObject = entityClassMetaData.getConstructor().newInstance();
                entityClassMetaData.getAllFields().forEach(field -> {
                    try {
                        var fieldValue = resultSet.getObject(field.getName());
                        field.set(newObject, fieldValue);
                    } catch (SQLException | IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException("error getting field value");
                    }

                });
                return newObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("error creating Class Object");
        }
        return null;
    }

    private List<T> getAllEntityClassObjectsFromResultSet(ResultSet resultSet) {

        List<T> allClassObjects = new ArrayList<>();
        try {
            while (resultSet.next()) allClassObjects.add(getEntityClassObjectFromResultSet(resultSet));
        } catch (Exception e) {
            throw new RuntimeException("error creating Class Objects");
        }
        return allClassObjects;
    }

    private List<Object> getObjectFieldsInsert(T object) {
        List<Object> objectFields = new ArrayList<>();
        entityClassMetaData.getFieldsWithoutId().forEach(field -> {
            try {
                field.setAccessible(true);
                objectFields.add(field.get(object));
            } catch (Exception e) {
                throw new RuntimeException("error getting Object fields");
            }
        });
        return objectFields;
    }

    private List<Object> getObjectFieldsUpdate(T client) {
        List<Object> objectFields = getObjectFieldsInsert(client);
        try {
            objectFields.add(entityClassMetaData.getIdField().get(client));
        } catch (Exception e) {
            throw new RuntimeException("error getting Object fields");
        }
        return objectFields;
    }


    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                this::getEntityClassObjectFromResultSet);
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection,
                entitySQLMetaData.getSelectAllSql(),
                Collections.emptyList(),
                this::getAllEntityClassObjectsFromResultSet).orElseThrow(() -> new RuntimeException("runtime error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getObjectFieldsInsert(client));
    }

    @Override
    public void update(Connection connection, T client) {
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getObjectFieldsUpdate(client));
    }
}
