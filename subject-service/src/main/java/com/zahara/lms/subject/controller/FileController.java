package com.zahara.lms.subject.controller;

import com.zahara.lms.shared.controller.BaseController;
import com.zahara.lms.subject.dto.FileDTO;
import com.zahara.lms.subject.model.File;
import com.zahara.lms.subject.service.FileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController
        extends BaseController<File, FileDTO, Long> {
    private final FileService service;

    public FileController(FileService service) {
        super(service);
        this.service = service;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bundleId", required = false) Long bundleId) {
        try {
            String filePath = this.service.uploadFile(file, bundleId);
            return ResponseEntity.ok().body(filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") Long id) {
        try {
            return this.service.downloadFile(id);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Failed to upload file: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/bundle/{id}/all")
    public ResponseEntity<List<FileDTO>> getByBundleId(@PathVariable Long id) {
        return new ResponseEntity<>(this.service.findByBundleId(id), HttpStatus.OK);
    }

    @GetMapping("/bundle/{id}")
    public ResponseEntity<Page<FileDTO>> getByBundleId(
            @PathVariable Long id,
            Pageable pageable,
            @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(
                this.service.findByBundleId(id, pageable, search), HttpStatus.OK);
    }
}
