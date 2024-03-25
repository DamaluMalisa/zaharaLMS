package com.zahara.lms.assessment.model;

import com.zahara.lms.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssessmentResult extends BaseEntity<Long> {
    @Column(nullable = false)
    private Long subjectEnrollmentId;

    @ManyToOne(optional = false)
    private StudentSubmission studentSubmission;

    private Integer scores;

    @Lob
    @Column(nullable = false)
    private String feedback;

}
