package com.zahara.lms.subject.service;

import com.zahara.lms.shared.exception.NotFoundException;
import com.zahara.lms.shared.service.ExtendedService;
import com.zahara.lms.subject.client.FacultyFeignClient;
import com.zahara.lms.subject.dto.FileDTO;
import com.zahara.lms.subject.dto.TeacherDTO;
import com.zahara.lms.subject.mapper.BundleMapper;
import com.zahara.lms.subject.mapper.FileMapper;
import com.zahara.lms.subject.model.Bundle;
import com.zahara.lms.subject.model.File;
import com.zahara.lms.subject.repository.BundleRepository;
import com.zahara.lms.subject.repository.FileRepository;
import com.zahara.lms.subject.repository.SubjectRepository;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.zahara.lms.shared.security.SecurityUtils.getTeacherId;
import static com.zahara.lms.subject.util.Utility.generateUniqueFileName;

@Service
public class FileService  extends ExtendedService<File, FileDTO, Long> {

    @Value("${file.upload-dir}")
    private String uploadDir;
    private final Tika tika = new Tika();
    private final FileRepository repository;
    private final FileMapper mapper;
    private final BundleMapper bundleMapper;
    private final BundleRepository bundleRepository;
    private final SubjectRepository subjectRepository;
    private final FacultyFeignClient facultyFeignClient;



    public FileService(
            FileRepository repository,
            FileMapper mapper,
            BundleRepository bundleRepository,
            SubjectRepository subjectRepository,
            FacultyFeignClient facultyFeignClient,
            BundleMapper bundleMapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.bundleRepository = bundleRepository;
        this.subjectRepository = subjectRepository;
        this.facultyFeignClient = facultyFeignClient;
        this.bundleMapper = bundleMapper;
    }

    @Transactional
    public String uploadFile(MultipartFile file, Long bundleId) throws IOException {

        FileDTO fileDTO = new FileDTO();
        if (bundleId != null) {
            Bundle bundle = bundleRepository.findById(bundleId)
                    .orElseThrow(() -> new NotFoundException("Bundle not found with ID: " + bundleId));
            fileDTO.setBundle(bundleMapper.toDTO(bundle));
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            fileName = generateUniqueFileName();
        }

        String contentType = file.getContentType();
        if (contentType == null || contentType.isBlank() || contentType.equals("application/octet-stream")) {
            contentType = tika.detect(file.getInputStream());
        }

        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath);


        fileDTO.setFileName(fileName);
        fileDTO.setContentType(contentType);
        fileDTO.setFilePath(filePath.toString());
        fileDTO.setUploadTimestamp(LocalDateTime.now());
        fileDTO.setTeacher((TeacherDTO) facultyFeignClient.getTeacher(Collections.singleton(getTeacherId())));

        File fileEntity = mapper.toModel(fileDTO);
        repository.save(fileEntity);

        return filePath.toString();
    }



    public ResponseEntity<byte[]> downloadFile(Long id) throws IOException {
        File fileEntity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("File not found with ID: " + id));

        FileDTO fileDTO = mapper.toDTO(fileEntity);

        Path path = Paths.get(fileDTO.getFilePath());
        byte[] fileContent = Files.readAllBytes(path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileDTO.getFileName());

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    @Override
    protected List<FileDTO> mapMissingValues(List<FileDTO> files) {
        map(
                files,
                FileDTO::getTeacher,
                FileDTO::setTeacher,
                facultyFeignClient::getTeacher);

        return files;
    }



    public List<FileDTO> findBySubjectId(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new NotFoundException("Bundle not found");
        }

        List<FileDTO> pages =
                mapper.toDTO(
                        repository.findBySubjectIdAndDeletedFalseOrderByUploadTimestampDesc(id));
        return pages.isEmpty()
                ? pages
                : this.mapMissingValues(pages);
    }

    public Page<FileDTO> findBySubjectId(Long id, Pageable pageable, String search) {
        if (!subjectRepository.existsById(id)) {
            throw new NotFoundException("Bundle not found");
        }

        Page<FileDTO> pages =
                repository
                        .findBySubjectIdContaining(id, pageable, "%" + search + "%")
                        .map(mapper::toDTO);
        return pages.getContent().isEmpty()
                ? pages
                : new PageImpl<>(
                this.mapMissingValues(pages.getContent()),
                pageable,
                pages.getTotalElements());
    }

    public List<FileDTO> findByBundleId(Long id) {
        if (!bundleRepository.existsById(id)) {
            throw new NotFoundException("bundle not found");
        }

        List<FileDTO> pages =
                mapper.toDTO(
                        repository.findByBundleIdAndDeletedFalseOrderByUploadTimestampDesc(id));
        return pages.isEmpty()
                ? pages
                : this.mapMissingValues(pages);


    }

    public Page<FileDTO> findByBundleId(Long id, Pageable pageable, String search) {
        if (!bundleRepository.existsById(id)) {
            throw new NotFoundException("Bundle not found");
        }

        Page<FileDTO> pages =
                repository
                        .findByBundleIdContaining(id, pageable, "%" + search + "%")
                        .map(mapper::toDTO);
        return pages.getContent().isEmpty()
                ? pages
                : new PageImpl<>(
                this.mapMissingValues(pages.getContent()),
                pageable,
                pages.getTotalElements());

    }
}
