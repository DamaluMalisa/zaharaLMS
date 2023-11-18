package com.zahara.lms.assessment.mapper;

import com.zahara.lms.assessment.dto.AssessmentTypeDTO;
import com.zahara.lms.assessment.dto.SubjectDTO;
import com.zahara.lms.assessment.model.AssessmentType;
import com.zahara.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssessmentTypeMapper extends BaseMapper<AssessmentType, AssessmentTypeDTO, Long> {
    @Mapping(source = "subjectId", target = "subject")
    AssessmentTypeDTO toDTO(AssessmentType assessmentType);

    @Mapping(source = "subject.id", target = "subjectId")
    AssessmentType toModel(AssessmentTypeDTO assessmentTypeDTO);

    SubjectDTO subjectDTOFromId(Long id);
}
