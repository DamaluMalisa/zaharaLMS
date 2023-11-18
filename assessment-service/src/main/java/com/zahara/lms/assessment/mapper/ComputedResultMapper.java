package com.zahara.lms.assessment.mapper;

import com.zahara.lms.assessment.dto.ComputedResultDTO;
import com.zahara.lms.assessment.dto.StudentDTO;
import com.zahara.lms.assessment.model.ComputedResult;
import com.zahara.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComputedResultMapper extends BaseMapper<ComputedResult, ComputedResultDTO, Long> {
    @Mapping(source = "studentId", target = "student")
    ComputedResultDTO toDTO(ComputedResult computedResult);

    @Mapping(source = "student.id", target = "studentId")
    ComputedResult toModel(ComputedResultDTO computedResultDTO);
    StudentDTO studentDTOFromId(Long id);
}
