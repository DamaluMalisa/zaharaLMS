package com.zahara.lms.assessment.service;

import com.zahara.lms.assessment.dto.StudentCategoryDTO;
import com.zahara.lms.assessment.mapper.StudentCategoryMapper;
import com.zahara.lms.assessment.model.StudentCategory;
import com.zahara.lms.assessment.repository.StudentCategoryRepository;
import com.zahara.lms.shared.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class StudentCategoryService extends BaseService<StudentCategory, StudentCategoryDTO, Long> {
    private final StudentCategoryRepository repository;
    private final StudentCategoryMapper mapper;

    public StudentCategoryService(StudentCategoryRepository repository, StudentCategoryMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }
}
