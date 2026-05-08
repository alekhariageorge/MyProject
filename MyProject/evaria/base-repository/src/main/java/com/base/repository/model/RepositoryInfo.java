package com.base.repository.model;

import com.base.repository.adapter.InMemoryRepositoryAdapter;
import com.base.repository.adapter.RepositoryAdapter;
import com.base.repository.definition.RepositoryDefinition;
import com.base.repository.definition.RepositoryDefinitionLoader;
import com.base.repository.feed.FeedItem;
import com.base.repository.feed.FeedResource;
import com.base.repository.feed.FeedResourceResolver;
import com.base.repository.feed.RepositoryFeedImporter;
import com.base.repository.value.StringPreservingRepositoryValueConverter;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class RepositoryInfo {

    private static final String DEFAULT_FEED_PATH = "repository/add-items";

    private String name;

    private String definitionFilePath;

    private Map<String, RepositoryItemDescriptor> itemDescriptors = new LinkedHashMap<>();

    private final RepositoryDefinitionLoader definitionLoader;

    private final FeedResourceResolver feedResourceResolver;

    private final RepositoryFeedImporter feedImporter;

    private final RepositoryAdapter repositoryAdapter;

    public RepositoryInfo(final String repositoryXMLPath) {
        this(repositoryXMLPath, new InMemoryRepositoryAdapter());
    }

    protected RepositoryInfo(final String repositoryXMLPath,
                             RepositoryAdapter repositoryAdapter) {
        this.definitionLoader = new RepositoryDefinitionLoader();
        this.feedResourceResolver = new FeedResourceResolver(getClass().getClassLoader());
        this.feedImporter = new RepositoryFeedImporter(new StringPreservingRepositoryValueConverter());
        this.repositoryAdapter = repositoryAdapter;
        this.name = this.getClass().toString();
        this.definitionFilePath = repositoryXMLPath;
        this.loadRepositoryXml(repositoryXMLPath);
        this.addItems(DEFAULT_FEED_PATH);
    }

    private void loadRepositoryXml(String xmlPath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(xmlPath)) {
            if (inputStream == null) {
                throw new RuntimeException("Repository XML not found: " + xmlPath);
            }

            RepositoryDefinition repositoryDefinition = definitionLoader.load(inputStream);
            this.name = repositoryDefinition.getName();
            this.itemDescriptors = repositoryDefinition.getItemDescriptors();
            this.repositoryAdapter.initialize(this, this.itemDescriptors);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load repository XML", e);
        }
    }

    public void addItems(String feedPath) {
        try {
            FeedResource feedResource = feedResourceResolver.resolve(feedPath);
            for (FeedItem feedItem : feedImporter.importFeed(feedResource, this.itemDescriptors)) {
                addItem(
                        feedItem.getRepositoryId(),
                        feedItem.getDescriptorName(),
                        feedItem.getPropertyValues()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load repository feed: " + feedPath, e);
        }
    }

    public void importItems(String feedPath) {
        addItems(feedPath);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, RepositoryItemDescriptor> getItemDescriptors() {
        return itemDescriptors;
    }

    public void setItemDescriptors(Map<String, RepositoryItemDescriptor> itemDescriptors) {
        this.itemDescriptors = itemDescriptors;
    }

    public String getDefinitionFilePath() {
        return definitionFilePath;
    }

    public void setDefinitionFilePath(String definitionFilePath) {
        this.definitionFilePath = definitionFilePath;
    }

    public RepositoryItem getItemById(String itemDescriptorName, String repositoryId) {
        return this.repositoryAdapter.findById(itemDescriptorName, repositoryId);
    }

    public void addItem(String repositoryId, final String descriptorName, Map<String, Object> propertyValues) {
        this.repositoryAdapter.save(repositoryId, descriptorName, propertyValues);
    }

    public void removeItem(String itemDescriptorName, String repositoryId) {
        this.repositoryAdapter.remove(itemDescriptorName, repositoryId);
    }

    public Map<String, Map<String, RepositoryItem>> getRepositoryItems() {
        return this.repositoryAdapter.getRepositoryItems();
    }

    public void setRepositoryItems(Map<String, Map<String, RepositoryItem>> repositoryItems) {
        this.repositoryAdapter.setRepositoryItems(repositoryItems);
    }
}
