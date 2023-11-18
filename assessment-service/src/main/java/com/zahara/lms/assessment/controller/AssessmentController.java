package com.zahara.lms.assessment.controller;

import com.zahara.lms.assessment.dto.AssessmentDTO;
import com.zahara.lms.assessment.model.Assessment;
import com.zahara.lms.assessment.service.AssessmentService;
import com.zahara.lms.shared.controller.BaseController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments")
public class AssessmentController extends BaseController<Assessment, AssessmentDTO, Long> {
    private final AssessmentService service;

    public AssessmentController(AssessmentService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/subject/{id}/all")
    public ResponseEntity<List<AssessmentDTO>> getBySubjectId(@PathVariable Long id) {
        return new ResponseEntity<>(service.findBySubjectId(id), HttpStatus.OK);
    }

    @GetMapping("/subject/{id}")
    public ResponseEntity<Page<AssessmentDTO>> getBySubjectId(@PathVariable Long id, Pageable pageable, @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(service.findBySubjectId(id, pageable, search), HttpStatus.OK);
    }

    @GetMapping("/assessment-type/{id}/all")
    public ResponseEntity<List<AssessmentDTO>> getByAssessmentTypeId(@PathVariable Long id) {
        return new ResponseEntity<>(service.findByAssessmentTypeId(id), HttpStatus.OK);
    }

    @GetMapping("/assessment-type/{id}")
    public ResponseEntity<Page<AssessmentDTO>> getByAssessmentTypeId(@PathVariable Long id, Pageable pageable, @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(service.findByAssessmentTypeId(id, pageable, search), HttpStatus.OK);
    }

    @GetMapping("/student-category/{id}/all")
    public ResponseEntity<List<AssessmentDTO>> getByStudentCategoryId(@PathVariable Long id) {
        return new ResponseEntity<>(service.findByStudentCategoryId(id), HttpStatus.OK);
    }

    @GetMapping("/student-category/{id}")
    public ResponseEntity<Page<AssessmentDTO>> getByStudentCategoryId(@PathVariable Long id, Pageable pageable, @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(service.findByStudentCategoryId(id, pageable, search), HttpStatus.OK);
    }
}
