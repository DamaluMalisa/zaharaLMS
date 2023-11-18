package com.zahara.lms.faculty.model;

import com.zahara.lms.shared.model.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentInfo extends BaseEntity<Long> {

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    private String nextOfKinPhoneNumber;

    @Column(nullable = false)
    private String address;

    private double GPA;

    private String academicStatus;

    private int totalCreditsEarned;

    private int totalCourses;

    private String nationality;

    private String emergencyContact;

    private String birthPlace;

    private String maritalStatus;

    private String scholarshipDetails;

    private String financialAidStatus;

    @OneToOne(optional = false)
    private SecondaryAndOrAdvancedEducation secondaryAndOrAdvancedEducation;

    @OneToOne(optional = false)
    private Student student;
}
