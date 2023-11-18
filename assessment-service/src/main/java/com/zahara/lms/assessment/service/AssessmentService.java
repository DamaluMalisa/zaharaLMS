package com.zahara.lms.assessment.service;

import com.zahara.lms.assessment.client.SubjectFeignClient;
import com.zahara.lms.assessment.dto.AssessmentDTO;
import com.zahara.lms.assessment.dto.AssessmentTypeDTO;
import com.zahara.lms.assessment.dto.SubjectDTO;
import com.zahara.lms.assessment.mapper.AssessmentMapper;
import com.zahara.lms.assessment.model.Assessment;
import com.zahara.lms.assessment.repository.AssessmentRepository;
import com.zahara.lms.assessment.repository.AssessmentTypeRepository;
import com.zahara.lms.assessment.repository.StudentCategoryRepository;
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
import java.util.stream.Collectors;

import static com.zahara.lms.shared.security.SecurityUtils.*;


@Service
public class AssessmentService extends ExtendedService<Assessment, AssessmentDTO, Long> {

    private final AssessmentRepository repository;
    private final AssessmentMapper mapper;
    private final SubjectFeignClient subjectFeignClient;
    private final AssessmentTypeService assessmentTypeService;
    private final AssessmentTypeRepository assessmentTypeRepository;
    private final StudentCategoryRepository studentCategoryRepository;

    public AssessmentService(AssessmentRepository repository, AssessmentMapper mapper, AssessmentTypeService assessmentTypeService, AssessmentTypeRepository assessmentTypeRepository,  StudentCategoryRepository studentCategoryRepository,  SubjectFeignClient subjectFeignClient) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.subjectFeignClient = subjectFeignClient;
        this.assessmentTypeService = assessmentTypeService;
        this.assessmentTypeRepository = assessmentTypeRepository;
        this.studentCategoryRepository = studentCategoryRepository;
    }

    @Override
    protected List<AssessmentDTO> mapMissingValues(List<AssessmentDTO> assessments) {
        map(
                assessments,
                assessment -> assessment.getAssessmentType().getSubject(),
                (assessment, subject) -> assessment.getAssessmentType().setSubject(subject),
                subjectFeignClient::getSubject);

        return assessments;
    }


    @Override
    @Transactional
    public AssessmentDTO save(AssessmentDTO assessmentDTO) {
        if (hasAuthority(ROLE_TEACHER)) {
            Long teacherId = getTeacherId();
            Long assessmentTypeId = assessmentDTO.getAssessmentType().getId();
            Long studentCategoryId = assessmentDTO.getStudentCategory().getId();

            if (!this.assessmentTypeRepository.existsById(assessmentTypeId) &&
                    !this.studentCategoryRepository.existsById(studentCategoryId)) {
                throw new NotFoundException("ID not found");
            }

            List<AssessmentTypeDTO> assessmentTypes = assessmentTypeService.findById(Set.of(assessmentTypeId));

            assessmentTypes.stream()
                    .filter(assessmentType ->
                            !assessmentType.getSubject().getProfessor().getId().equals(teacherId) &&
                                    !assessmentType.getSubject().getAssistant().getId().equals(teacherId))
                    .findAny()
                    .ifPresent(assessmentType -> {
                        throw new ForbiddenException("You are not allowed to create an assessment for this subject");
                    });
        }
        return super.save(assessmentDTO);
    }


    @Override
    @Transactional
    public void delete(Set<Long> id) {
        if (hasAuthority(ROLE_TEACHER)) {
            Long teacherId = getTeacherId();
            List<Assessment> assessments = (List<Assessment>) repository.findAllById(id);
            List<SubjectDTO> subjects =
                    subjectFeignClient.getSubject(
                            assessments.stream().map(assessment -> assessment.getAssessmentType().getSubjectId()).collect(Collectors.toSet()));

            boolean forbidden =
                    subjects.stream()
                            .anyMatch(
                                    subject ->
                                            !subject.getProfessor().getId().equals(teacherId)
                                                    && !subject.getAssistant()
                                                    .getId()
                                                    .equals(teacherId));
            if (forbidden) {
                throw new ForbiddenException("You are not allowed to delete these Assessments");
            }
        }

        super.delete(id);
    }


    public List<AssessmentDTO> findBySubjectId(Long id) {
        List<AssessmentDTO> assessments = mapper.toDTO(repository.findByAssessmentTypeSubjectIdAndDeletedFalse(id));
        return assessments.isEmpty() ? assessments : this.mapMissingValues(assessments);
    }



    public Page<AssessmentDTO> findBySubjectId(Long id, Pageable pageable, String search) {
        Page<AssessmentDTO> assessments =
                repository
                        .findByAssessmentTypeSubjectIdContaining(id, pageable, "%" + search + "%")
                        .map(mapper::toDTO);
        return assessments.getContent().isEmpty()
                ? assessments
                : new PageImpl<>(
                this.mapMissingValues(assessments.getContent()),
                pageable,
                assessments.getTotalElements());
    }

    public List<AssessmentDTO> findByAssessmentTypeId(Long id) {
        List<AssessmentDTO> assessments = mapper.toDTO(repository.findByAssessmentTypeIdAndDeletedFalse(id));
        return assessments.isEmpty() ? assessments : this.mapMissingValues(assessments);
    }

    public Page<AssessmentDTO> findByAssessmentTypeId(Long id, Pageable pageable, String search) {
        Page<AssessmentDTO> assessments =
                repository
                        .findByAssessmentTypeIdContaining(id, pageable, "%" + search + "%")
                        .map(mapper::toDTO);
        return assessments.getContent().isEmpty()
                ? assessments
                : new PageImpl<>(
                this.mapMissingValues(assessments.getContent()),
                pageable,
                assessments.getTotalElements());
    }

    public List<AssessmentDTO> findByStudentCategoryId(Long id) {
        List<AssessmentDTO> assessments = mapper.toDTO(repository.findByStudentCategoryIdAndDeletedFalse(id));
        return assessments.isEmpty() ? assessments : this.mapMissingValues(assessments);
    }

    public Page<AssessmentDTO> findByStudentCategoryId(Long id, Pageable pageable, String search) {
        Page<AssessmentDTO> assessments =
                repository
                        .findByStudentCategoryIdContaining(id, pageable, "%" + search + "%")
                        .map(mapper::toDTO);
        return assessments.getContent().isEmpty()
                ? assessments
                : new PageImpl<>(
                this.mapMissingValues(assessments.getContent()),
                pageable,
                assessments.getTotalElements());
    }
}
