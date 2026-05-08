package com.pdp.mediaservice.model;

import com.base.repository.model.RepositoryItem;

public class MediaDTO {

    private String mediaId;
    private String mediaType;
    private String displayName;
    private String description;
    private String url;
    private String path;
    private String mimeType;
    private String altText;
    private String width;
    private String height;

    public MediaDTO() {}

    public MediaDTO(RepositoryItem mediaItem) {
        this.mediaId = mediaItem.getRepositoryId();
        this.mediaType = (String) mediaItem.getPropertyValue("mediaType");
        this.displayName = (String) mediaItem.getPropertyValue("displayName");
        this.description = (String) mediaItem.getPropertyValue("description");
        this.url = (String) mediaItem.getPropertyValue("url");
        this.path = (String) mediaItem.getPropertyValue("path");
        this.mimeType = (String) mediaItem.getPropertyValue("mimeType");
        this.altText = (String) mediaItem.getPropertyValue("altText");
        this.width = (String) mediaItem.getPropertyValue("width");
        this.height = (String) mediaItem.getPropertyValue("height");
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
