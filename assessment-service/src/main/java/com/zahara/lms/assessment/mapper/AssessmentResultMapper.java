package com.zahara.lms.assessment.mapper;

import com.zahara.lms.assessment.dto.AssessmentResultDTO;
import com.zahara.lms.assessment.dto.StudentSubmissionDTO;
import com.zahara.lms.assessment.model.AssessmentResult;
import com.zahara.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssessmentResultMapper extends BaseMapper<AssessmentResult ,AssessmentResultDTO,  Long> {


}