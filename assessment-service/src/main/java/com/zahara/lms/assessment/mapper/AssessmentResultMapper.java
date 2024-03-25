package com.zahara.lms.assessment.mapper;

import com.zahara.lms.assessment.dto.AssessmentResultDTO;
import com.zahara.lms.assessment.dto.StudentSubmissionDTO;
import com.zahara.lms.assessment.dto.SubjectEnrollmentDTO;
import com.zahara.lms.assessment.model.AssessmentResult;
import com.zahara.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssessmentResultMapper extends BaseMapper<AssessmentResult ,AssessmentResultDTO,  Long> {


        @Mapping(source = "subjectEnrollmentId", target = "subjectEnrollment")
        AssessmentResultDTO toDTO(AssessmentResult assessmentResult);

        @Mapping(source = "subjectEnrollment.id", target = "subjectEnrollmentId")
        AssessmentResult toModel(AssessmentResultDTO examDTO);

        SubjectEnrollmentDTO subjectEnrollmentDTOFromId(Long id);




}
