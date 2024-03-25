package com.zahara.lms.assessment.service;

import com.zahara.lms.assessment.client.SubjectFeignClient;
import com.zahara.lms.assessment.dto.*;
import com.zahara.lms.assessment.mapper.AssessmentResultMapper;
import com.zahara.lms.assessment.model.AssessmentResult;
import com.zahara.lms.assessment.model.StudentSubmission;
import com.zahara.lms.assessment.repository.AssessmentRepository;
import com.zahara.lms.assessment.repository.AssessmentResultRepository;
import com.zahara.lms.assessment.repository.StudentSubmissionRepository;
import com.zahara.lms.shared.exception.ForbiddenException;
import com.zahara.lms.shared.exception.NotFoundException;
import com.zahara.lms.shared.service.ExtendedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zahara.lms.shared.security.SecurityUtils.*;

@Service
public class AssessmentResultService extends ExtendedService<AssessmentResult, AssessmentResultDTO, Long> {
    private final AssessmentResultRepository repository;
    private final AssessmentResultMapper mapper;
    private final SubjectFeignClient subjectFeignClient;
    private final StudentSubmissionRepository studentSubmissionRepository;
    private final StudentSubmissionService studentSubmissionService;
    private final AssessmentRepository assessmentRepository;


    public AssessmentResultService(AssessmentResultRepository repository, AssessmentResultMapper mapper, SubjectFeignClient subjectFeignClient, StudentSubmissionRepository studentSubmissionRepository, StudentSubmissionService studentSubmissionService, AssessmentRepository assessmentRepository) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.subjectFeignClient = subjectFeignClient;
        this.studentSubmissionRepository = studentSubmissionRepository;
        this.studentSubmissionService = studentSubmissionService;
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    protected List<AssessmentResultDTO> mapMissingValues(List<AssessmentResultDTO> assessmentResults) {
        map(
                assessmentResults,
                AssessmentResultDTO::getSubjectEnrollment,
                AssessmentResultDTO::setSubjectEnrollment,
                subjectFeignClient::getSubjectEnrollment);

        return assessmentResults;
    }

    public List<AssessmentResultDTO> findByStudentSubmissionId(Long id) {
        if (!studentSubmissionRepository.existsById(id)) {
            throw new NotFoundException("Exam term id not found");
        }

        if (hasAuthority(ROLE_TEACHER)) {
            StudentSubmissionDTO studentSubmission = studentSubmissionService.findById(Set.of(id)).get(0);
            SubjectDTO subject = studentSubmission.getAssessment().getAssessmentType().getSubject();
            System.out.println(subject.getName());

            Long teacherId = getTeacherId();
            if (!teacherId.equals(subject.getProfessor().getId())
                    && !teacherId.equals(subject.getAssistant().getId())) {
                throw new ForbiddenException("You are not allowed to view this exam realization");
            }
        }

        List<AssessmentResultDTO> assessmentResults =
                mapper.toDTO(repository.findBySubjectEnrollmentIdInAndDeletedFalse(Collections.singletonList(id)));
        return assessmentResults.isEmpty() ? assessmentResults : mapMissingValues(assessmentResults);
    }

    public Page<AssessmentResultDTO> findByStudentSubmissionId(Long id, Pageable pageable, String search) {
        if (!studentSubmissionRepository.existsById(id)) {
            throw new NotFoundException("Exam term id not found");
        }

        if (hasAuthority(ROLE_TEACHER)) {
            StudentSubmissionDTO studentSubmission = studentSubmissionService.findById(Set.of(id)).get(0);
            SubjectDTO subject = studentSubmission.getAssessment().getAssessmentType().getSubject();

            Long teacherId = getTeacherId();
            if (!teacherId.equals(subject.getProfessor().getId())
                    && !teacherId.equals(subject.getAssistant().getId())) {
                throw new ForbiddenException("You are not allowed to view this exam realizations");
            }
        }

        Page<AssessmentResultDTO> assessmentResults =
                repository
                        .findByStudentSubmissionIdContaining(id, pageable, "%" + search + "%")
                        .map(mapper::toDTO);
        return assessmentResults.getContent().isEmpty()
                ? assessmentResults
                : new PageImpl<>(
                this.mapMissingValues(assessmentResults.getContent()),
                pageable,
                assessmentResults.getTotalElements());
    }

    public Page<AssessmentResultDTO> findByStudentId(Long id, Pageable pageable, String search) {
        if (hasAuthority(ROLE_STUDENT) && !id.equals(getStudentId())) {
            throw new ForbiddenException("You are not allowed to view these exam realizations.");
        }

        List<Long> subjectEnrollmentIds =
                new ArrayList<>();
        for (SubjectEnrollmentDTO subjectEnrollmentDTO : subjectFeignClient.getSubjectEnrollmentByStudentId(id)) {
            Long subjectEnrollmentDTOId = subjectEnrollmentDTO.getId();
            subjectEnrollmentIds.add(subjectEnrollmentDTOId);
        }

        Page<AssessmentResultDTO> assessmentResults =
                repository
                        .findBySubjectEnrollmentIdsContaining(
                                subjectEnrollmentIds, pageable, "%" + search + "%")
                        .map(mapper::toDTO);

        return assessmentResults.getContent().isEmpty()
                ? assessmentResults
                : new PageImpl<>(
                this.mapMissingValues(assessmentResults.getContent()),
                pageable,
                assessmentResults.getTotalElements());
    }

    @Override
    @Transactional
 public AssessmentResultDTO save(AssessmentResultDTO assessmentResultDTO) {
        if (!hasAuthority(ROLE_TEACHER)
                && (
                !assessmentResultDTO.getStudentSubmission().getAssessment().getAssessmentType().getSubject().getProfessor().getId().equals(getTeacherId())
                        && !assessmentResultDTO.getStudentSubmission().getAssessment().getAssessmentType().getSubject().getAssistant().getId().equals(getTeacherId())
        )) {
            throw new ForbiddenException("You are not allowed to create these assessment results");
        }
        if (this.studentSubmissionRepository.existsById(assessmentResultDTO.getStudentSubmission().getId())) {
            throw new NotFoundException("Student Submission Not Found.");
        }

        SubjectEnrollmentDTO subjectEnrollment = (SubjectEnrollmentDTO) subjectFeignClient.getSubjectEnrollmentByStudentId(assessmentResultDTO.getStudentSubmission().getStudent().getId());
        assessmentResultDTO.setSubjectEnrollment(subjectEnrollment);

        return super.save(assessmentResultDTO);

    }

}
