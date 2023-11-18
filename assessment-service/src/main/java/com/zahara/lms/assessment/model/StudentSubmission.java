package com.zahara.lms.assessment.model;

import com.zahara.lms.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentSubmission extends BaseEntity<Long> {
    @ManyToOne(optional = false)
    private Assessment assessment;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private LocalDateTime submissionDate;

    @Column(nullable = false)
    private String submissionMaterial;

    @OneToMany(mappedBy = "studentSubmission")
    private List<AssessmentResult> assessmentResult;
}
