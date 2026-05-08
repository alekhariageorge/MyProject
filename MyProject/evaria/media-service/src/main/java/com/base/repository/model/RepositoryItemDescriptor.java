package com.base.repository.model;

import java.util.HashMap;
import java.util.Map;

public class RepositoryItemDescriptor {

    private String itemDescriptorName;

    private Map<String, Class<?>> propertyDefinitions;

    public RepositoryItemDescriptor(String descriptorName,
                                    Map<String,Class<?>> propertyDefinitions
                                    ) {
        this.itemDescriptorName = descriptorName;
        this.propertyDefinitions = propertyDefinitions;
    }

    public String getItemDescriptorName() {
        return itemDescriptorName;
    }

    public void setItemDescriptorName(String itemDescriptorName) {
        this.itemDescriptorName = itemDescriptorName;
    }

    public Map<String, Class<?>> getPropertyDefinitions() {
        return propertyDefinitions;
    }

    public void setPropertyDefinitions(
            Map<String, Class<?>> propertyDefinitions) {
        this.propertyDefinitions = propertyDefinitions;
    }
}