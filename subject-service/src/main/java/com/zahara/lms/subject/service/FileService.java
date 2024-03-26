package com.zahara.lms.subject.service;

import com.zahara.lms.shared.exception.ForbiddenException;
import com.zahara.lms.shared.exception.NotFoundException;
import com.zahara.lms.shared.service.ExtendedService;
import com.zahara.lms.subject.client.FacultyFeignClient;
import com.zahara.lms.subject.dto.*;
import com.zahara.lms.subject.dto.FileDTO;
import com.zahara.lms.subject.mapper.FileMapper;
import com.zahara.lms.subject.model.File;
import com.zahara.lms.subject.model.Subject;
import com.zahara.lms.subject.repository.BundleRepository;
import com.zahara.lms.subject.repository.FileRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.zahara.lms.shared.security.SecurityUtils.*;
import static com.zahara.lms.shared.security.SecurityUtils.getTeacherId;

@Service
public class FileService  extends ExtendedService<File, FileDTO, Long> {
    private final FileRepository repository;
    private final FileMapper mapper;
    private final BundleRepository bundleRepository;
    private final FacultyFeignClient facultyFeignClient;

    public FileService(
            FileRepository repository,
            FileMapper mapper,
            BundleRepository bundleRepository,
            FacultyFeignClient facultyFeignClient) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.bundleRepository = bundleRepository;
        this.facultyFeignClient = facultyFeignClient;
    }

    @Override
    @Transactional
    public FileDTO save(FileDTO fileDTO) {
        if (hasAuthority(ROLE_TEACHER)) {
            TeacherDTO teacher = facultyFeignClient.getTeacher(Set.of(getTeacherId())).get(0);
            SubjectDTO subject = fileDTO.getBundle().getSubject();
            if (!subject.getProfessor().getId().equals(teacher.getId())
                    && !subject.getAssistant().getId().equals(teacher.getId())) {
                throw new ForbiddenException(
                        "You are not allowed manage this File");
            }

            if (fileDTO.getTeacher() == null) {
                fileDTO.setTeacher(teacher);
            }
        }

        return super.save(fileDTO);
    }

    @Override
    @Transactional
    public void delete(Set<Long> id) {
        if (hasAuthority(ROLE_TEACHER)) {
            Long teacherId = getTeacherId();
            List<File> files =
                    (List<File>) repository.findAllById(id);
            boolean forbidden =
                    files.stream()
                            .anyMatch(
                                    file -> {
                                        Subject subject = file.getBundle().getSubject();
                                        return !subject.getProfessorId().equals(teacherId)
                                                && !subject.getAssistantId().equals(teacherId);
                                    });
            if (forbidden) {
                throw new ForbiddenException(
                        "You are not allowed to delete these Quiz");
            }
        }

        super.delete(id);
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


    public List<FileDTO> findByBundleId(Long id) {
        if (!bundleRepository.existsById(id)) {
            throw new NotFoundException("bundle not found");
        }

        List<FileDTO> files =
                mapper.toDTO(
                        repository.findByBundleIdAndDeletedFalseOrderByUploadTimestampDesc(id));
        return files.isEmpty()
                ? files
                : this.mapMissingValues(files);
    }

    public Page<FileDTO> findByBundleId(Long id, Pageable pageable, String search) {
        if (!bundleRepository.existsById(id)) {
            throw new NotFoundException("Bundle not found");
        }

        Page<FileDTO> files =
                repository
                        .findByBundleIdContaining(id, pageable, "%" + search + "%")
                        .map(mapper::toDTO);
        return files.getContent().isEmpty()
                ? files
                : new PageImpl<>(
                this.mapMissingValues(files.getContent()),
                pageable,
                files.getTotalElements());
    }
}
