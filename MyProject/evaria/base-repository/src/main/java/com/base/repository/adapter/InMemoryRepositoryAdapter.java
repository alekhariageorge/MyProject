package com.base.repository.adapter;

import com.base.repository.model.RepositoryInfo;
import com.base.repository.model.RepositoryItem;
import com.base.repository.model.RepositoryItemDescriptor;

import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryRepositoryAdapter implements RepositoryAdapter {

    private RepositoryInfo repository;

    private Map<String, RepositoryItemDescriptor> itemDescriptors = new LinkedHashMap<>();

    private Map<String, Map<String, RepositoryItem>> repositoryItems = new LinkedHashMap<>();

    @Override
    public void initialize(RepositoryInfo repository,
                           Map<String, RepositoryItemDescriptor> itemDescriptors) {
        this.repository = repository;
        this.itemDescriptors = itemDescriptors;
        this.repositoryItems = new LinkedHashMap<>();

        for (String descriptorName : itemDescriptors.keySet()) {
            this.repositoryItems.put(descriptorName, new LinkedHashMap<>());
        }
    }

    @Override
    public RepositoryItem findById(String itemDescriptorName, String repositoryId) {
        Map<String, RepositoryItem> items = this.repositoryItems.get(itemDescriptorName);
        if (items == null) {
            return null;
        }
        return items.get(repositoryId);
    }

    @Override
    public void save(String repositoryId,
                     String descriptorName,
                     Map<String, Object> propertyValues) {
        RepositoryItemDescriptor itemDescriptor = this.itemDescriptors.get(descriptorName);
        if (itemDescriptor == null) {
            throw new RuntimeException("Unknown item descriptor: " + descriptorName);
        }

        RepositoryItem item = new RepositoryItem(repositoryId, repository, itemDescriptor, propertyValues);
        Map<String, RepositoryItem> items = this.repositoryItems.computeIfAbsent(
                descriptorName,
                key -> new LinkedHashMap<>()
        );
        items.put(repositoryId, item);
    }

    @Override
    public void remove(String itemDescriptorName, String repositoryId) {
        Map<String, RepositoryItem> items = this.repositoryItems.get(itemDescriptorName);
        if (items != null) {
            items.remove(repositoryId);
        }
    }

    @Override
    public Map<String, Map<String, RepositoryItem>> getRepositoryItems() {
        return repositoryItems;
    }

    @Override
    public void setRepositoryItems(Map<String, Map<String, RepositoryItem>> repositoryItems) {
        this.repositoryItems = repositoryItems;
    }
}
