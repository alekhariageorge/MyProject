package com.pdp.mediaservice.service;

import com.base.repository.model.RepositoryItem;
import com.pdp.mediaservice.repository.MediaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public RepositoryItem getMedia(final String mediaId) {
        return mediaRepository.findMediaById(mediaId);
    }

    public List<RepositoryItem> getMediaByType(final String mediaType) {
        return mediaRepository.findMediaByType(mediaType);
    }
}
