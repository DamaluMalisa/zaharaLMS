package com.zahara.lms.assessment.controller;

import com.zahara.lms.assessment.dto.AssessmentTypeDTO;
import com.zahara.lms.assessment.model.AssessmentType;
import com.zahara.lms.assessment.service.AssessmentTypeService;
import com.zahara.lms.shared.controller.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assessment-types")
public class AssessmentTypeController extends BaseController<AssessmentType, AssessmentTypeDTO, Long> {
    private final AssessmentTypeService service;

    public AssessmentTypeController(AssessmentTypeService service) {
        super(service);
        this.service = service;
    }

    @PatchMapping("/{id}/score")
    public ResponseEntity<AssessmentTypeDTO> updateMaxMarks(@PathVariable Long id, @RequestBody AssessmentTypeDTO assessmentTypeDTO) {
        return new ResponseEntity<>(this.service.updateMaxMarks(id, assessmentTypeDTO), HttpStatus.OK);
    }
}