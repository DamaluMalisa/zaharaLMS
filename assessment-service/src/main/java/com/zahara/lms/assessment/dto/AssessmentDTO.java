package com.zahara.lms.assessment.dto;

import com.zahara.lms.shared.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssessmentDTO extends BaseDTO<Long> {
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Date Created  is mandatory")
    private LocalDateTime createdDate;

    @NotNull(message = "Due date is mandatory")
    private LocalDateTime dueDate;

    @NotBlank(message = "Source Url is mandatory")
    private String sourceUrl;

    @NotNull(message = "Assessment type is mandatory")
    private AssessmentTypeDTO assessmentType;

    @NotNull(message = "Student Category is mandatory")
    private StudentCategoryDTO studentCategory;





}
