package com.zahara.lms.subject.mapper;

import com.zahara.lms.shared.mapper.BaseMapper;
import com.zahara.lms.subject.dto.AssignmentDTO;
import com.zahara.lms.subject.dto.AssignmentSubmissionDTO;
import com.zahara.lms.subject.dto.StudentDTO;
import com.zahara.lms.subject.dto.TeacherDTO;
import com.zahara.lms.subject.model.Assignment;
import com.zahara.lms.subject.model.AssignmentSubmission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssignmentMapper extends BaseMapper<Assignment, AssignmentDTO, Long> {
    @Mapping(source = "teacherId", target = "teacher")
    AssignmentDTO toDTO(Assignment assignment);

    @Mapping(source = "teacher.id", target = "teacherId")
    Assignment toModel(AssignmentDTO assignmentDTO);

    TeacherDTO teacherDTOFromId(Long id);
}
