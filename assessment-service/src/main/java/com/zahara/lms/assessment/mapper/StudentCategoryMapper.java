package com.zahara.lms.assessment.mapper;

import com.zahara.lms.assessment.dto.StudentCategoryDTO;
import com.zahara.lms.assessment.model.StudentCategory;
import com.zahara.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentCategoryMapper extends BaseMapper<StudentCategory, StudentCategoryDTO, Long> {
}
