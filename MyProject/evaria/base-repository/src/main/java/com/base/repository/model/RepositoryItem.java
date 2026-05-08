package com.base.repository.model;

import java.util.HashMap;
import java.util.Map;

public class RepositoryItem {

    private String repositoryId;

    private RepositoryInfo repository;

    private RepositoryItemDescriptor itemDescriptor;

    private Map<String, Object> propertyValueMap = new HashMap<>();

    public RepositoryItem(final String repositoryId,
                          RepositoryInfo repository,
                          RepositoryItemDescriptor itemDescriptor,
                          Map<String, Object> propertyValueMap) {
        this.repositoryId = repositoryId;
        this.repository = repository;
        this.itemDescriptor = itemDescriptor;
        this.propertyValueMap = propertyValueMap;
    }

    public Object getPropertyValue(String propertyName) {
        return this.getPropertyValueMap().get(propertyName);
    }

    public void setPropertyValue(String propertyName, Object value) {
        this.getPropertyValueMap().put(propertyName, value);
    }

    public RepositoryItemDescriptor getItemDescriptor() {
        return itemDescriptor;
    }

    public void setItemDescriptor(RepositoryItemDescriptor itemDescriptor) {
        this.itemDescriptor = itemDescriptor;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public RepositoryInfo getRepository() {
        return repository;
    }

    public void setRepository(RepositoryInfo repository) {
        this.repository = repository;
    }

    public Map<String, Object> getPropertyValueMap() {
        return propertyValueMap;
    }

    public void setPropertyValueMap(Map<String, Object> propertyValueMap) {
        this.propertyValueMap = propertyValueMap;
    }
}
