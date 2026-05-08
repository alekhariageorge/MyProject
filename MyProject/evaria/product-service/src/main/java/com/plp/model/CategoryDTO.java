package com.plp.model;

import com.base.repository.model.RepositoryItem;

public class CategoryDTO {

    private String categoryId;
    private String displayName;
    private String description;
    private String longDescription;

    public CategoryDTO() {}

    public CategoryDTO(RepositoryItem category) {
        this.categoryId = category.getRepositoryId();
        this.displayName = (String) category.getPropertyValue("displayName");
        this.description = (String) category.getPropertyValue("description");
        this.longDescription = (String) category.getPropertyValue("longDescription");
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }
}
