package com.base.repository.feed;

public class FeedResource {

    private final String resolvedPath;

    private final byte[] bytes;

    public FeedResource(String resolvedPath, byte[] bytes) {
        this.resolvedPath = resolvedPath;
        this.bytes = bytes;
    }

    public String getResolvedPath() {
        return resolvedPath;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
