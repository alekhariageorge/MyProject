package com.base.repository.value;

public class StringPreservingRepositoryValueConverter implements RepositoryValueConverter {

    @Override
    public Object convert(String value, Class<?> targetType) {
        return value;
    }
}
