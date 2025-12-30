package com.demo.forum.api.admin;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MediaStore {
    private final Map<UUID, MediaAsset> assets = new ConcurrentHashMap<>();

    public List<MediaSummary> store(MultipartFile[] files) {
        List<MediaSummary> summaries = new ArrayList<>();
        if (files == null) {
            return summaries;
        }
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            summaries.add(storeSingle(file));
        }
        return summaries;
    }

    public MediaAsset find(UUID id) {
        return assets.get(id);
    }

    public PageResponse<MediaSummary> list(int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        List<MediaAsset> sorted = assets.values()
                .stream()
                .sorted(Comparator.comparing(MediaAsset::uploadedAt).reversed())
                .toList();
        int fromIndex = Math.min((safePage - 1) * safeSize, sorted.size());
        int toIndex = Math.min(fromIndex + safeSize, sorted.size());
        List<MediaSummary> pageItems = sorted.subList(fromIndex, toIndex)
                .stream()
                .map(asset -> new MediaSummary(asset.id(), asset.filename(), asset.contentType(), asset.uploadedAt()))
                .toList();
        int totalPages = (int) Math.ceil(sorted.size() / (double) safeSize);
        return new PageResponse<>(pageItems, safePage, safeSize, sorted.size(), totalPages);
    }

    private MediaSummary storeSingle(MultipartFile file) {
        UUID id = UUID.randomUUID();
        String contentType = file.getContentType() == null ? "application/octet-stream" : file.getContentType();
        byte[] data = readBytes(file);
        MediaAsset asset = new MediaAsset(id, file.getOriginalFilename(), contentType, data, Instant.now());
        assets.put(id, asset);
        return new MediaSummary(asset.id(), asset.filename(), asset.contentType(), asset.uploadedAt());
    }

    private byte[] readBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException ex) {
            return new byte[0];
        }
    }
}
