package com.zahara.lms.assessment.mapper;

import com.zahara.lms.assessment.dto.AssessmentDTO;
import com.zahara.lms.assessment.dto.AssessmentTypeDTO;
import com.zahara.lms.assessment.dto.StudentCategoryDTO;
import com.zahara.lms.assessment.dto.SubjectDTO;
import com.zahara.lms.assessment.model.Assessment;
import com.zahara.lms.assessment.model.AssessmentType;
import com.zahara.lms.assessment.model.StudentCategory;
import com.zahara.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssessmentMapper extends BaseMapper<Assessment, AssessmentDTO, Long> {
    @Mapping(source = "subjectId", target = "subject")
    AssessmentTypeDTO toDTO(AssessmentType assessmentType);

    @Mapping(source = "subject.id", target = "subjectId")
    AssessmentType toModel(AssessmentTypeDTO assessmentTypeDTO);
    SubjectDTO subjectDTOFromId(Long id);

    StudentCategoryDTO toDTO(StudentCategory studentCategory);
}