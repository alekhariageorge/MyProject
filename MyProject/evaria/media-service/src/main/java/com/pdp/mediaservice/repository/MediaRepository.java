package com.pdp.mediaservice.repository;

import com.base.repository.model.RepositoryInfo;
import com.base.repository.model.RepositoryItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class MediaRepository extends RepositoryInfo {

    public MediaRepository() {
        super("repository/repository-definition.xml");
        addItems("repository/add-items.xml");
    }

    public RepositoryItem findMediaById(String mediaId) {
        RepositoryItem externalMedia = getItemById("media-external", mediaId);
        if (externalMedia != null) {
            return externalMedia;
        }
        return getItemById("media-internal-binary", mediaId);
    }

    public List<RepositoryItem> findMediaByType(String mediaType) {
        List<RepositoryItem> mediaItems = new ArrayList<>();
        collectMediaByType(mediaItems, "media-external", mediaType);
        collectMediaByType(mediaItems, "media-internal-binary", mediaType);
        return mediaItems;
    }

    private void collectMediaByType(List<RepositoryItem> mediaItems,
                                    String descriptorName,
                                    String mediaType) {
        Map<String, RepositoryItem> items = getRepositoryItems().get(descriptorName);
        for (String itemId : items.keySet()) {
            RepositoryItem item = items.get(itemId);
            if (mediaType.equalsIgnoreCase((String) item.getPropertyValue("mediaType"))) {
                mediaItems.add(item);
            }
        }
    }
}
