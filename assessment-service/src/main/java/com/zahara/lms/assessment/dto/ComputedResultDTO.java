package com.zahara.lms.assessment.dto;

import com.zahara.lms.assessment.model.StudentCategory;
import com.zahara.lms.shared.dto.BaseDTO;
import lombok.*;

import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ComputedResultDTO extends BaseDTO<Long> {
    @NotBlank(message = "Student must be mandatory")
    private StudentDTO student;

    @NotBlank(message = "Assessment results must be mandatory")
    private AssessmentResultDTO assessmentResultDTO;

    @NotBlank(message = "Assessment type is mandatory")
    private AssessmentTypeDTO assessmentType;

    @NotBlank(message = "Student category must be mandatory")
    private StudentCategory studentCategory;

}
