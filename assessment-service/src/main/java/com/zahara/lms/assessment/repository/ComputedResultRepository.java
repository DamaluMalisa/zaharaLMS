package com.zahara.lms.assessment.repository;

import com.zahara.lms.assessment.model.ComputedResult;
import com.zahara.lms.assessment.model.AssessmentType;
import com.zahara.lms.assessment.model.StudentCategory;
import com.zahara.lms.shared.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputedResultRepository extends BaseRepository<ComputedResult, Long> {

    @Override
    @Query("select x from #{#entityName} x where x.deleted = false and "
            + "(cast(x.id as string) like :search or cast(x.studentId as string) like :search)")
    Page<ComputedResult> findContaining(Pageable pageable, String search);

    @Query("select x from #{#entityName} x where x.deleted = false and x.studentId = :studentId and "
            + "(cast(x.id as string) like :search)")
    Page<ComputedResult> findByStudentIdContaining(Long studentId, Pageable pageable, String search);

    @Query("select x from #{#entityName} x where x.deleted = false and x.assessmentType.id = :assessmentTypeId and "
            + "(cast(x.id as string) like :search)")
    Page<ComputedResult> findByAssessmentTypeIdContaining(Long assessmentTypeId, Pageable pageable, String search);

    @Query("select x from #{#entityName} x where x.deleted = false and x.studentCategory = :studentCategory and "
            + "(cast(x.id as string) like :search)")
    Page<ComputedResult> findByStudentCategoryContaining(StudentCategory studentCategory, Pageable pageable, String search);

    List<ComputedResult> findByStudentIdAndDeletedFalse(Long studentId);

    List<ComputedResult> findByAssessmentTypeAndDeletedFalse(AssessmentType assessmentType);

    List<ComputedResult> findByStudentCategoryAndDeletedFalse(StudentCategory studentCategory);
}
