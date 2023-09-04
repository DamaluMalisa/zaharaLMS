package com.zahara.lms.subject.mapper;

import com.zahara.lms.shared.mapper.BaseMapper;
import com.zahara.lms.subject.dto.SubjectDTO;
import com.zahara.lms.subject.dto.SubjectNotificationDTO;
import com.zahara.lms.subject.dto.TeacherDTO;
import com.zahara.lms.subject.model.Subject;
import com.zahara.lms.subject.model.SubjectNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubjectNotificationMapper
        extends BaseMapper<SubjectNotification, SubjectNotificationDTO, Long> {
    @Mapping(source = "teacherId", target = "teacher")
    SubjectNotificationDTO toDTO(SubjectNotification subjectNotification);

    @Mapping(source = "teacher.id", target = "teacherId")
    SubjectNotification toModel(SubjectNotificationDTO subjectNotificationDTO);

    TeacherDTO teacherDTOFromId(Long id);

    @Mapping(target = "studyProgram", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "assistant", ignore = true)
    SubjectDTO toDTO(Subject subject);
}
