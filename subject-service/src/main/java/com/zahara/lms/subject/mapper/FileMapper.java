package com.zahara.lms.subject.mapper;

import com.zahara.lms.shared.mapper.BaseMapper;
import com.zahara.lms.subject.dto.FileDTO;
import com.zahara.lms.subject.dto.FileDTO;
import com.zahara.lms.subject.dto.TeacherDTO;
import com.zahara.lms.subject.model.File;
import com.zahara.lms.subject.model.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper extends BaseMapper<File, FileDTO, Long> {
    @Mapping(source = "teacherId", target = "teacher")
    FileDTO toDTO(File file);

    @Mapping(source = "teacher.id", target = "teacherId")
    File toModel(FileDTO fileDTO);

    TeacherDTO teacherDTOFromId(Long id);
}