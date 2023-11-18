package com.zahara.lms.assessment.model;

import com.zahara.lms.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Assessment extends BaseEntity<Long> {

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    private String sourceUrl;

    @ManyToOne(optional = false)
    private AssessmentType assessmentType;

    @ManyToOne(optional = false)
    private StudentCategory studentCategory;

    @OneToMany(mappedBy = "assessment")
    private List<StudentSubmission> studentSubmissions;
}
