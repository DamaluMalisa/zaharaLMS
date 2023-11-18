package com.zahara.lms.assessment.service;

import com.zahara.lms.assessment.client.SubjectFeignClient;
import com.zahara.lms.assessment.dto.SubjectDTO;
import com.zahara.lms.assessment.dto.AssessmentTypeDTO;
import com.zahara.lms.assessment.mapper.AssessmentTypeMapper;
import com.zahara.lms.assessment.model.AssessmentType;
import com.zahara.lms.assessment.repository.AssessmentTypeRepository;
import com.zahara.lms.shared.exception.ForbiddenException;
import com.zahara.lms.shared.service.ExtendedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.Set;

import static com.zahara.lms.shared.security.SecurityUtils.*;

@Service
public class AssessmentTypeService extends ExtendedService<AssessmentType, AssessmentTypeDTO, Long> {
    private final AssessmentTypeRepository repository;
    private final AssessmentTypeMapper mapper;
    private final SubjectFeignClient subjectFeignClient;

    public AssessmentTypeService(
            AssessmentTypeRepository repository,
            AssessmentTypeMapper mapper,
            SubjectFeignClient subjectFeignClient) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.subjectFeignClient = subjectFeignClient;
    }

    @Override
    protected List<AssessmentTypeDTO> mapMissingValues(List<AssessmentTypeDTO> assessmentTypes) {
        map(
                assessmentTypes,
                AssessmentTypeDTO::getSubject,
                AssessmentTypeDTO::setSubject,
                subjectFeignClient::getSubject);

        return assessmentTypes;
    }





    @Transactional
    public AssessmentTypeDTO updateMaxMarks(Long id, AssessmentTypeDTO assessmentTypeDTO) {
        AssessmentTypeDTO assessmentType = findById(Set.of(id)).get(0);

        if (hasAuthority(ROLE_TEACHER)) {
            SubjectDTO subject = assessmentType.getSubject();
            Long teacherId = getTeacherId();
            if (!subject.getProfessor().getId().equals(teacherId)
                    && !subject.getAssistant().getId().equals(teacherId)) {
                throw new ForbiddenException(
                        "You are not allowed to update Maximum marks for this assessment type");
            }
        }

        assessmentType.setMaxMarks(assessmentTypeDTO.getMaxMarks());
        AssessmentType updatedAssessmentType = repository.save(mapper.toModel(assessmentType));
        return mapper.toDTO(updatedAssessmentType);
    }

}
