package com.zahara.lms.assessment.dto;

import com.zahara.lms.shared.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssessmentResultDTO  extends BaseDTO<Long> {

    @NotBlank(message = "Student submission is mandatory")
    private StudentSubmissionDTO studentSubmission;

    @NotBlank(message = "marks is mandatory")
    private Integer score;

    @NotBlank(message = "feedback is mandatory")
    private String feedback;
}
