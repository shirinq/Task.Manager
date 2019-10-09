package com.example.taskmanager.Database;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.UUID;

public class UUIDconverter implements PropertyConverter<UUID, String> {
    @Override
    public UUID convertToEntityProperty(String databaseValue) {
        if (databaseValue == null)
            return null;
        return UUID.fromString(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(UUID entityProperty) {
        if (entityProperty == null)
            return null;
        return entityProperty.toString();
    }
}
