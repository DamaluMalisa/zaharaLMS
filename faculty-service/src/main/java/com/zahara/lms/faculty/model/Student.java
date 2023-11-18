package com.zahara.lms.faculty.model;

import com.zahara.lms.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Student extends BaseEntity<Long> {
    private Long userId;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String gender;

    private LocalDateTime dateOfBirth;

    @Column(name = "reg_number", length = 45, nullable = false)
    private String registrationNumber;

    @Column(nullable = false)
    private Integer yearOfEnrollment;

    @OneToOne
    private Thesis thesis;

    @ManyToOne(optional = false)
    private StudyProgram studyProgram;

    @OneToOne
    private StudentInfo studentInfo;
}
