package com.zahara.lms.assessment.controller;

import com.zahara.lms.assessment.dto.StudentCategoryDTO;
import com.zahara.lms.assessment.model.StudentCategory;
import com.zahara.lms.assessment.service.StudentCategoryService;
import com.zahara.lms.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student-category")
public class StudentCategoryController extends BaseController<StudentCategory, StudentCategoryDTO, Long> {
    private final StudentCategoryService service;

    public StudentCategoryController(StudentCategoryService service) {
        super(service);
        this.service = service;
    }
}
