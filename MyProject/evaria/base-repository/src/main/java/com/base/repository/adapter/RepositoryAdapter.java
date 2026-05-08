package com.base.repository.adapter;

import com.base.repository.model.RepositoryInfo;
import com.base.repository.model.RepositoryItem;
import com.base.repository.model.RepositoryItemDescriptor;

import java.util.Map;

public interface RepositoryAdapter {

    void initialize(RepositoryInfo repository,
                    Map<String, RepositoryItemDescriptor> itemDescriptors);

    RepositoryItem findById(String itemDescriptorName, String repositoryId);

    void save(String repositoryId,
              String descriptorName,
              Map<String, Object> propertyValues);

    void remove(String itemDescriptorName, String repositoryId);

    Map<String, Map<String, RepositoryItem>> getRepositoryItems();

    void setRepositoryItems(Map<String, Map<String, RepositoryItem>> repositoryItems);
}
