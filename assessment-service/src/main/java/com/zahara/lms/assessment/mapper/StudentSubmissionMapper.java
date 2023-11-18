package com.zahara.lms.assessment.mapper;

import com.zahara.lms.assessment.dto.StudentDTO;
import com.zahara.lms.assessment.dto.StudentSubmissionDTO;
import com.zahara.lms.assessment.model.StudentSubmission;
import com.zahara.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentSubmissionMapper extends BaseMapper<StudentSubmission, StudentSubmissionDTO, Long> {
    @Mapping(source = "studentId", target = "student")
    StudentSubmissionDTO toDTO(StudentSubmission studentSubmission);

    @Mapping(source = "student.id", target = "studentId")
    StudentSubmission toModel(StudentSubmissionDTO studentSubmissionDTO);

    StudentDTO studentDTOFromId(Long id);
}
