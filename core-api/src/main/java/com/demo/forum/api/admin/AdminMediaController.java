package com.demo.forum.api.admin;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/media")
@RequiredArgsConstructor
public class AdminMediaController {
    private final MediaStore mediaStore;

    @GetMapping
    public PageResponse<MediaSummary> listMedia(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "12") int size) {
        return mediaStore.list(page, size);
    }

    @PostMapping
    public List<MediaSummary> upload(@RequestParam("files") MultipartFile[] files) {
        return mediaStore.store(files);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> load(@PathVariable UUID id) {
        MediaAsset asset = mediaStore.find(id);
        if (asset == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(asset.contentType()));
        if (!asset.contentType().startsWith("image/")) {
            headers.setContentDispositionFormData("attachment", asset.filename());
        }
        return new ResponseEntity<>(asset.data(), headers, HttpStatus.OK);
    }
}
