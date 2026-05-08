package com.base.repository.value;

public interface RepositoryValueConverter {

    Object convert(String value, Class<?> targetType);
}
