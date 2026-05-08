package com.plp.model;

import com.base.repository.model.RepositoryItem;

public class CatalogDTO {

    private String catalogId;
    private String displayName;
    private String description;

    public CatalogDTO() {}

    public CatalogDTO(RepositoryItem catalog) {
        this.catalogId = catalog.getRepositoryId();
        this.displayName = (String) catalog.getPropertyValue("displayName");
        this.description = (String) catalog.getPropertyValue("description");
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
