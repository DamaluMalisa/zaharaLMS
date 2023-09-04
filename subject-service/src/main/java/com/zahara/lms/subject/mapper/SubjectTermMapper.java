package com.zahara.lms.subject.mapper;

import com.zahara.lms.shared.mapper.BaseMapper;
import com.zahara.lms.subject.dto.SubjectDTO;
import com.zahara.lms.subject.dto.SubjectTermDTO;
import com.zahara.lms.subject.dto.TeacherDTO;
import com.zahara.lms.subject.model.Subject;
import com.zahara.lms.subject.model.SubjectTerm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubjectTermMapper extends BaseMapper<SubjectTerm, SubjectTermDTO, Long> {
    @Mapping(source = "teacherId", target = "teacher")
    SubjectTermDTO toDTO(SubjectTerm SubjectTerm);

    @Mapping(source = "teacher.id", target = "teacherId")
    SubjectTerm toModel(SubjectTermDTO SubjectTermDTO);

    TeacherDTO teacherDTOFromId(Long id);

    @Mapping(target = "studyProgram", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "assistant", ignore = true)
    SubjectDTO toDTO(Subject subject);
}
