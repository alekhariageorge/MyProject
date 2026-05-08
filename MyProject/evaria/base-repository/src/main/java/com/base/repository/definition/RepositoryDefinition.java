package com.base.repository.definition;

import com.base.repository.model.RepositoryItemDescriptor;

import java.util.Map;

public class RepositoryDefinition {

    private final String name;

    private final Map<String, RepositoryItemDescriptor> itemDescriptors;

    public RepositoryDefinition(String name,
                                Map<String, RepositoryItemDescriptor> itemDescriptors) {
        this.name = name;
        this.itemDescriptors = itemDescriptors;
    }

    public String getName() {
        return name;
    }

    public Map<String, RepositoryItemDescriptor> getItemDescriptors() {
        return itemDescriptors;
    }
}
