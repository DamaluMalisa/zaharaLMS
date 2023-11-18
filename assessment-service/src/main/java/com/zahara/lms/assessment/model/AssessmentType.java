package com.zahara.lms.assessment.model;

import com.zahara.lms.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssessmentType extends BaseEntity<Long> {
    @Column(nullable = false)
    private Long subjectId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer maxMarks;

    @OneToMany(mappedBy = "assessmentType")
    private Set<Assessment> assessments;
}
