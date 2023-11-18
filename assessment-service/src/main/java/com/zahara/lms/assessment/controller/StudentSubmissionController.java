package com.zahara.lms.assessment.controller;

import com.zahara.lms.assessment.dto.StudentSubmissionDTO;
import com.zahara.lms.assessment.model.StudentSubmission;
import com.zahara.lms.assessment.service.StudentSubmissionService;
import com.zahara.lms.shared.controller.BaseController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student-submission")
public class StudentSubmissionController extends BaseController<StudentSubmission, StudentSubmissionDTO, Long> {
    private final StudentSubmissionService service;
    public StudentSubmissionController(StudentSubmissionService service) {
    super(service);
    this.service = service;
    }
    @GetMapping("/student/{id}/all")
    public ResponseEntity<List<StudentSubmissionDTO>> getByStudentId(@PathVariable Long id) {
        return new ResponseEntity<>(service.findByStudentId(id), HttpStatus.OK);
    }
    @GetMapping("/student/{id}")
    public ResponseEntity<Page<StudentSubmissionDTO>> getByStudentId(
            @PathVariable Long id,
            Pageable pageable,
            @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(
                this.service.findByStudentId(id, pageable, search), HttpStatus.OK);
    }
    @GetMapping("/subject/{id}/all")
    public ResponseEntity<List<StudentSubmissionDTO>> getBySubjectId(@PathVariable Long id) {
        return new ResponseEntity<>(service.findBySubjectId(id), HttpStatus.OK);
    }
    @GetMapping("/subject/{id}")
    public ResponseEntity<Page<StudentSubmissionDTO>> getBySubjectId(
            @PathVariable Long id,
            Pageable pageable,
            @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(
                this.service.findBySubjectId(id, pageable, search), HttpStatus.OK);
    }
    @GetMapping("/assessment/{id}/all")
    public ResponseEntity<List<StudentSubmissionDTO>> getByAssessmentId(@PathVariable Long id) {
        return new ResponseEntity<>(service.findByAssessmentId(id), HttpStatus.OK);
    }
    @GetMapping("/assessment/{id}")
    public ResponseEntity<Page<StudentSubmissionDTO>> getByAssessmentId(
            @PathVariable Long id,
            Pageable pageable,
            @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(
                this.service.findByAssessmentId(id, pageable, search), HttpStatus.OK);
    }

}
