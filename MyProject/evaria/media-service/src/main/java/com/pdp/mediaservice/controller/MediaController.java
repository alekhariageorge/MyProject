package com.pdp.mediaservice.controller;

import com.base.repository.model.RepositoryItem;
import com.pdp.mediaservice.model.MediaDTO;
import com.pdp.mediaservice.service.MediaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping("/{mediaId}")
    public MediaDTO getMedia(@PathVariable String mediaId) {
        RepositoryItem media = mediaService.getMedia(mediaId);
        return new MediaDTO(media);
    }

    @GetMapping("/type/{mediaType}")
    public List<MediaDTO> getMediaByType(@PathVariable String mediaType) {
        return mediaService.getMediaByType(mediaType)
                .stream()
                .map(MediaDTO::new)
                .toList();
    }
}
