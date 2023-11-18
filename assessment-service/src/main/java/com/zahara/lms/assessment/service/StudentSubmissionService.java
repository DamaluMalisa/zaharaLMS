package com.zahara.lms.assessment.service;

import com.zahara.lms.assessment.client.FacultyFeignClient;
import com.zahara.lms.assessment.dto.*;
import com.zahara.lms.assessment.mapper.StudentSubmissionMapper;
import com.zahara.lms.assessment.model.StudentSubmission;
import com.zahara.lms.assessment.repository.AssessmentRepository;
import com.zahara.lms.assessment.repository.StudentSubmissionRepository;
import com.zahara.lms.shared.exception.ForbiddenException;
import com.zahara.lms.shared.exception.NotFoundException;
import com.zahara.lms.shared.service.ExtendedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

import static com.zahara.lms.shared.security.SecurityUtils.*;

@Service
public class StudentSubmissionService extends ExtendedService<StudentSubmission, StudentSubmissionDTO, Long> {
    private final StudentSubmissionRepository repository;
    private final StudentSubmissionMapper mapper;
    private final FacultyFeignClient facultyFeignClient;
    private final AssessmentRepository assessmentRepository;


    public StudentSubmissionService(StudentSubmissionRepository repository, StudentSubmissionMapper mapper, FacultyFeignClient facultyFeignClient, AssessmentRepository assessmentRepository) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.facultyFeignClient = facultyFeignClient;
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    protected List<StudentSubmissionDTO> mapMissingValues(
            List<StudentSubmissionDTO> studentSubmissions) {
        map(
                studentSubmissions,
                StudentSubmissionDTO::getStudent,
                StudentSubmissionDTO::setStudent,
                facultyFeignClient::getStudent);

        return studentSubmissions;
    }


    @Override
    @Transactional
    public StudentSubmissionDTO save(StudentSubmissionDTO studentSubmissionDTO) {
        if (hasAuthority(ROLE_STUDENT)) {
            StudentDTO student = facultyFeignClient.getStudent(Set.of(getStudentId())).get(0);
            studentSubmissionDTO.setStudent(student);
            if (!this.assessmentRepository.existsByIdNotDeleted(studentSubmissionDTO.getAssessment().getId())) {
                throw new NotFoundException("ID deleted");
            }
        }
        return super.save(studentSubmissionDTO);
    }

    public List<StudentSubmissionDTO> findByStudentId(Long id) {
        if ((hasAuthority(ROLE_STUDENT) && !id.equals(getStudentId())) || hasAuthority(ROLE_TEACHER)) {
            throw new ForbiddenException("You are not allowed to view this student submissions");
        }
        List<StudentSubmissionDTO> studentSubmissions =
                mapper.toDTO(repository.findByStudentIdAndDeletedFalse(id));
        return studentSubmissions.isEmpty() ? studentSubmissions : this.mapMissingValues(studentSubmissions);
    }

    public Page<StudentSubmissionDTO> findByStudentId(Long id, Pageable pageable, String search) {
        if ((hasAuthority(ROLE_STUDENT) && !id.equals(getStudentId())) || hasAuthority(ROLE_TEACHER)) {
            throw new ForbiddenException("You are not allowed to view this student submissions");
        }
        Page<StudentSubmissionDTO> studentSubmissions =
                repository
                        .findByStudentIdContaining(id, pageable, "%" + search + "%")
                        .map(mapper::toDTO);

        return studentSubmissions.getContent().isEmpty()
                ? studentSubmissions
                : new PageImpl<>(
                this.mapMissingValues(studentSubmissions.getContent()),
                pageable,
                studentSubmissions.getTotalElements());
    }

    public List<StudentSubmissionDTO> findByAssessmentId(Long id) {
        if (hasAuthority(ROLE_TEACHER) && !id.equals(getTeacherId())) {
            throw new ForbiddenException("You are not allowed to view this student submissions");
        }
        List<StudentSubmissionDTO> studentSubmissions =
                mapper.toDTO(repository.findByAssessmentIdAndDeletedFalse(id));
        return studentSubmissions.isEmpty() ? studentSubmissions : this.mapMissingValues(studentSubmissions);
    }

    public Page<StudentSubmissionDTO> findByAssessmentId(Long id, Pageable pageable, String search) {
        if (hasAuthority(ROLE_TEACHER) && !id.equals(getTeacherId())) {
            throw new ForbiddenException("You are not allowed to view this student submissions");
        }
        Page<StudentSubmissionDTO> studentSubmissions =
                repository
                        .findByAssessmentIdContaining(id, pageable, "%" + search + "%")
                        .map(mapper::toDTO);

        return studentSubmissions.getContent().isEmpty()
                ? studentSubmissions
                : new PageImpl<>(
                this.mapMissingValues(studentSubmissions.getContent()),
                pageable,
                studentSubmissions.getTotalElements());
    }

    public List<StudentSubmissionDTO> findBySubjectId(Long id) {
        if ((hasAuthority(ROLE_TEACHER) && !id.equals(getTeacherId())) || hasAuthority(ROLE_ADMIN) && !id.equals(getAdminId())) {
            throw new ForbiddenException("You are not allowed to view this student submissions");
        }
        List<StudentSubmissionDTO> studentSubmissions =
                mapper.toDTO(repository.findByAssessmentAssessmentTypeSubjectIdAndDeletedFalse(id));
        return studentSubmissions.isEmpty() ? studentSubmissions : this.mapMissingValues(studentSubmissions);
    }

    public Page<StudentSubmissionDTO> findBySubjectId(Long id, Pageable pageable, String search) {
        if ((hasAuthority(ROLE_TEACHER) && !id.equals(getTeacherId())) || hasAuthority(ROLE_ADMIN) && !id.equals(getAdminId())) {
            throw new ForbiddenException("You are not allowed to view this student submissions");
        }
        Page<StudentSubmissionDTO> studentSubmissions =
                repository
                        .findByAssessmentAssessmentTypeSubjectIdContaining(id, pageable, "%" + search + "%")
                        .map(mapper::toDTO);

        return studentSubmissions.getContent().isEmpty()
                ? studentSubmissions
                : new PageImpl<>(
                this.mapMissingValues(studentSubmissions.getContent()),
                pageable,
                studentSubmissions.getTotalElements());
    }
}