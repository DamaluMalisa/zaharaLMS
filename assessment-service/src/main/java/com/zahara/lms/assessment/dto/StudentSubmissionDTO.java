package com.zahara.lms.assessment.dto;

import com.zahara.lms.shared.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentSubmissionDTO extends BaseDTO<Long> {
    @NotNull(message = "Assessment is mandatory")
    private AssessmentDTO assessment;

    private StudentDTO student;

    @NotNull(message = "submission date is mandatory")
    private LocalDateTime submissionDate;

    @NotBlank(message = "Submission Material is mandatory ")
    private String submissionMaterial;
}
