package com.pdp.model;

import java.util.List;

public class Product {
    private String repositoryId;
    private String displayName;
    private String description;
    private Boolean hasColorVariants;
    private List<Sku> childSkus;

    public Product() {}

    public Product(String productId,
                   String displayName,
                   String description,
                   Boolean hasColorVariants,
                   List<Sku> childSkus) {
        this.repositoryId = productId;
        this.displayName = displayName;
        this.description = description;
        this.hasColorVariants = hasColorVariants;
        this.childSkus = childSkus;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
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

    public Boolean getHasColorVariants() {
        return hasColorVariants;
    }

    public void setHasColorVariants(Boolean hasColorVariants) {
        this.hasColorVariants = hasColorVariants;
    }

    public List<Sku> getChildSkus() {
        return childSkus;
    }

    public void setChildSkus(List<Sku> childSkus) {
        this.childSkus = childSkus;
    }
}
