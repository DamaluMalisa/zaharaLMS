package com.zahara.lms.assessment.controller;

import com.zahara.lms.assessment.dto.AssessmentResultDTO;
import com.zahara.lms.assessment.model.AssessmentResult;
import com.zahara.lms.assessment.service.AssessmentResultService;
import com.zahara.lms.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assessment-result")
public class AssessmentResultController extends BaseController<AssessmentResult, AssessmentResultDTO, Long> {
    private final AssessmentResultService service;

    public AssessmentResultController(AssessmentResultService service) {
        super(service);
        this.service = service;
    }
}
