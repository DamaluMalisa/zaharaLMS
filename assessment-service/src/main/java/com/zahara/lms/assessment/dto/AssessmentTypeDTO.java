package com.zahara.lms.assessment.dto;

import com.zahara.lms.shared.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssessmentTypeDTO extends BaseDTO<Long> {
    @NotNull(message = "Subject is mandatory")
    private SubjectDTO subject;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Max Marks is mandatory")
    private Integer maxMarks;
}