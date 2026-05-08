package com.base.repository.feed;

import java.util.Map;

public class FeedItem {

    private final String descriptorName;

    private final String repositoryId;

    private final Map<String, Object> propertyValues;

    public FeedItem(String descriptorName,
                    String repositoryId,
                    Map<String, Object> propertyValues) {
        this.descriptorName = descriptorName;
        this.repositoryId = repositoryId;
        this.propertyValues = propertyValues;
    }

    public String getDescriptorName() {
        return descriptorName;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public Map<String, Object> getPropertyValues() {
        return propertyValues;
    }
}
