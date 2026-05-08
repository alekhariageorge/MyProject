package com.base.repository.feed;

import java.io.InputStream;

public class FeedResourceResolver {

    private static final String[] FEED_EXTENSIONS = {".xml", ".csv", ".xls", ".xlsx"};

    private final ClassLoader classLoader;

    public FeedResourceResolver(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public FeedResource resolve(String feedPath) {
        if (!hasFileExtension(feedPath)) {
            for (String extension : FEED_EXTENSIONS) {
                FeedResource feedResource = tryResolve(feedPath + extension);
                if (feedResource != null) {
                    return feedResource;
                }
            }
        }

        FeedResource feedResource = tryResolve(feedPath);
        if (feedResource != null) {
            return feedResource;
        }

        throw new RuntimeException("Repository feed not found: " + feedPath);
    }

    private FeedResource tryResolve(String feedPath) {
        try (InputStream inputStream = classLoader.getResourceAsStream(feedPath)) {
            if (inputStream == null) {
                return null;
            }
            return new FeedResource(feedPath, inputStream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load repository feed: " + feedPath, e);
        }
    }

    private boolean hasFileExtension(String feedPath) {
        int lastSlashIndex = Math.max(feedPath.lastIndexOf('/'), feedPath.lastIndexOf('\\'));
        int lastDotIndex = feedPath.lastIndexOf('.');
        return lastDotIndex > lastSlashIndex;
    }
}
